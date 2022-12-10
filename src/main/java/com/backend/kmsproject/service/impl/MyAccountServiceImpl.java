package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.constants.KmsConstant;
import com.backend.kmsproject.common.enums.KmsRole;
import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.mapper.UserMapper;
import com.backend.kmsproject.model.dto.HistoryBookingDTO;
import com.backend.kmsproject.model.entity.*;
import com.backend.kmsproject.repository.dsl.BookingDslRepository;
import com.backend.kmsproject.repository.jpa.AddressRepository;
import com.backend.kmsproject.repository.jpa.BookingOtherServiceRepository;
import com.backend.kmsproject.repository.jpa.UserRepository;
import com.backend.kmsproject.request.myaccount.ChangePasswordRequest;
import com.backend.kmsproject.request.myaccount.GetListHistoryBookingRequest;
import com.backend.kmsproject.request.myaccount.UpdateMyAccountRequest;
import com.backend.kmsproject.response.ErrorResponse;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.booking.ListHistoryBookingResponse;
import com.backend.kmsproject.response.user.MyAccountResponse;
import com.backend.kmsproject.security.KmsPrincipal;
import com.backend.kmsproject.service.MyAccountService;
import com.backend.kmsproject.util.DatetimeUtils;
import com.backend.kmsproject.util.RequestUtils;
import com.backend.kmsproject.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyAccountServiceImpl implements MyAccountService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BookingOtherServiceRepository bookingOtherServiceRepository;
    private final BookingDslRepository bookingDslRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MyAccountResponse getMyAccount() {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        UserEntity user = userRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new NotFoundException("Not found user"));

        MyAccountResponse.MyAccountResponseBuilder builder = MyAccountResponse.builder();
        builder.setUser(UserMapper.entity2dto(user));
        List<String> authorities = new ArrayList<>();
        authorities.addAll(Arrays.asList(KmsConstant.LOGIN, KmsConstant.LOGOUT, KmsConstant.UPDATE_INFORMATION));

        if (KmsRole.ADMIN_ROLE.getRole().equalsIgnoreCase(user.getRole().getRoleName())) {
            authorities.addAll(Arrays.asList(KmsConstant.MANAGE_SYSTEM_ADMIN, KmsConstant.MANAGE_FOOTBALL_PITCH, KmsConstant.MANAGE_FOOTBALL_PITCH_ADMIN));
        } else if (KmsRole.FOOTBALL_PITCH_ROLE.getRole().equalsIgnoreCase(user.getRole().getRoleName())) {
            authorities.addAll(Arrays.asList(KmsConstant.MANAGE_FOOTBALL_PITCH_ADMIN, KmsConstant.MANAGE_SUB_FOOTBALL_PITCH,
                    KmsConstant.MANAGE_OTHER_SERVICE, KmsConstant.MANAGE_CUSTOMER, KmsConstant.MANAGE_BOOKING));
        } else {
            authorities.addAll(Arrays.asList(KmsConstant.BOOKING));
        }

        builder.setAuthorities(authorities);
        return builder.setSuccess(true).build();
    }

    @Override
    public OnlyIdResponse updateMyAccount(UpdateMyAccountRequest request) {
        Map<String, String> errors = new HashMap<>();
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        validFormatField(errors, request);
        validExistField(errors, principal.getUserId(), request);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        UserEntity user = userRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new NotFoundException("Not found user"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPhoneNumber(RequestUtils.blankIfNull(request.getPhoneNumber()));
        AddressEntity address;
        if (user.getAddress() == null && StringUtils.hasText(request.getAddress())) {
            address = new AddressEntity();
            address.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            address.setCreatedBy(principal.getUserId());
        } else {
            address = addressRepository.findById(user.getAddress().getAddressId())
                    .orElseThrow(() -> new NotFoundException("Not found address"));
        }
        address.setAddress(request.getAddress());
        address.setDistrict(RequestUtils.blankIfNull(request.getDistrict()));
        address.setCity(RequestUtils.blankIfNull(request.getCity()));
        address.setModifiedBy(principal.getUserId());
        address.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        addressRepository.save(address);
        user.setAddress(address);
        userRepository.save(user);
        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(user.getUserId())
                .setName(user.getFirstName() + " " + user.getLastName())
                .build();
    }

    @Override
    public NoContentResponse changeMyPassword(ChangePasswordRequest request) {
        Map<String, String> errors = new HashMap<>();
        Map<String, String> mapPassword = new HashMap<>() {{
            put("currentPassword", request.getCurrentPassword());
            put("newPassword", request.getNewPassword());
            put("confirmPassword", request.getConfirmPassword());
        }};
        mapPassword.keySet().forEach(p -> {
            validFormatPassword(errors, p, mapPassword.get(p));
        });
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        UserEntity user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new NotFoundException("Not found user"));
        validChangePassword(errors, request, user.getPassword());
        if (!errors.isEmpty()) {
            return NoContentResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    @Override
    public ListHistoryBookingResponse getListHistoryBooking(GetListHistoryBookingRequest request) {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        List<HistoryBookingDTO> historyBookings = new ArrayList<>();
        List<BookingEntity> bookings = bookingDslRepository.listBookingByUserId(request, principal.getUserId());
        bookings.forEach(b -> {
            List<BookingOtherServiceEntity> bookingOtherServices = bookingOtherServiceRepository.findByBookingId(b.getBookingId());
            historyBookings.add(toBuilder(b, bookingOtherServices));
        });
        return ListHistoryBookingResponse.builder()
                .setSuccess(true)
                .setHistoryBookings(historyBookings)
                .build();
    }

    private HistoryBookingDTO toBuilder(BookingEntity booking, List<BookingOtherServiceEntity> bookingOtherServices) {
        HistoryBookingDTO.HistoryBookingDTOBuilder builder = HistoryBookingDTO.builder();
        builder.setBookingId(booking.getBookingId())
                .setStatus(Boolean.TRUE.equals(booking.getStatus()) ? Boolean.TRUE : Boolean.FALSE)
                .setIsPaid(Boolean.TRUE.equals(booking.getIsPaid()) ? Boolean.TRUE : Boolean.FALSE)
                .setBookDate(DatetimeUtils.formatLocalDate(booking.getBookDay()))
                .setTimeStart(DatetimeUtils.formatLocalTime(booking.getTimeStart()))
                .setTimeEnd(DatetimeUtils.formatLocalTime(booking.getTimeEnd()))
                .setPricePitch(booking.getSubFootballPitch().getPricePerHour())
                .setTotalPriceIncludeService(totalPriceIncludeOtherService(booking, bookingOtherServices))
                .setFootballPitch(HistoryBookingDTO.FootballPitch.builder()
                        .setFootballPitchName(booking.getSubFootballPitch().getFootballPitch().getFootballPitchName())
                        .setSubFootballPitchName(booking.getSubFootballPitch().getSubFootballPitchName())
                        .setAddress(HistoryBookingDTO.FootballPitch.Address.builder()
                                .setAddress(booking.getSubFootballPitch().getFootballPitch().getAddress() != null ?
                                        booking.getSubFootballPitch().getFootballPitch().getAddress().getAddress() : "")
                                .setDistrict(booking.getSubFootballPitch().getFootballPitch().getAddress() != null ?
                                        booking.getSubFootballPitch().getFootballPitch().getAddress().getDistrict() : "")
                                .setCity(booking.getSubFootballPitch().getFootballPitch().getAddress() != null ?
                                        booking.getSubFootballPitch().getFootballPitch().getAddress().getCity() : "")
                                .build())
                        .build())
                .setOtherService(HistoryBookingDTO.OtherService.builder()
                        .setTotalPriceOtherService(totalPriceOtherService(bookingOtherServices))
                        .setTotalItems(bookingOtherServices.size())
                        .setItems(bookingOtherServices.stream()
                                .map(bos -> HistoryBookingDTO.OtherService.Item.builder()
                                        .setName(bos.getOtherService().getOtherServiceName())
                                        .setPrice(bos.getOtherService().getPricePerHour() != null ? bos.getOtherService().getPricePerHour()
                                                : bos.getOtherService().getPricePerOne())
                                        .setQuantity(bos.getQuantity())
                                        .build())
                                .collect(Collectors.toList()))
                        .build());
        ;
        return builder.build();
    }

    private Double totalPriceOtherService(List<BookingOtherServiceEntity> bookingOtherServices) {
        Double totalPrice = 0D;
        for (BookingOtherServiceEntity bookingOtherService : bookingOtherServices) {
            totalPrice += bookingOtherService.getOtherService().getPricePerHour() != null ? bookingOtherService.getOtherService().getPricePerHour()
                    : bookingOtherService.getOtherService().getPricePerOne();
            if (bookingOtherService.getOtherService().getPricePerOne() != null) {
                totalPrice = totalPrice * bookingOtherService.getQuantity();
            }
        }
        return totalPrice;
    }

    private Double totalPriceIncludeOtherService(BookingEntity booking, List<BookingOtherServiceEntity> bookingOtherServices) {
        int hours = (int) Duration.between(booking.getTimeStart(), booking.getTimeEnd()).toHours();
        return booking.getSubFootballPitch().getPricePerHour() * hours + totalPriceOtherService(bookingOtherServices);
    }

    private void validFormatPassword(Map<String, String> errors, String keyPassword, String valuePassword) {
        if (!StringUtils.hasText(valuePassword)) {
            errors.put(keyPassword, ErrorCode.MISSING_VALUE.name());
        } else if (valuePassword.length() < KmsConstant.PASSWORD_MIN_SIZE) {
            errors.put(keyPassword, ErrorCode.TOO_SHORT.name());
        } else if (valuePassword.length() > KmsConstant.PASSWORD_MAX_SIZE) {
            errors.put(keyPassword, ErrorCode.TOO_LONG.name());
        }
    }

    public void validChangePassword(Map<String, String> errors, ChangePasswordRequest request, String myCurrentPassword) {
        if (!errors.containsKey("currentPassword") && !passwordEncoder.matches(request.getCurrentPassword(), myCurrentPassword)) {
            errors.put("currentPassword", ErrorCode.NOT_FOUND.name());
        }
        if (!errors.containsKey("newPassword") && !errors.containsKey("confirmPassword")
                && !request.getConfirmPassword().equals(request.getNewPassword())) {
            errors.put("confirmPassword", ErrorCode.INVALID_VALUE.name());
        }
    }

    public void validFormatField(Map<String, String> errors, UpdateMyAccountRequest request) {
        if (!StringUtils.hasText(request.getFirstName())) {
            errors.put("firstName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getLastName())) {
            errors.put("lastName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getUsername())) {
            errors.put("username", ErrorCode.MISSING_VALUE.name());
        } else if (request.getUsername().length() > KmsConstant.USERNAME_MAX_SIZE) {
            errors.put("username", ErrorCode.TOO_LONG.name());
        } else if (request.getUsername().length() < KmsConstant.USERNAME_MIN_SIZE) {
            errors.put("username", ErrorCode.TOO_SHORT.name());
        }
    }

    public void validExistField(Map<String, String> errors, Long userId, UpdateMyAccountRequest request) {
        if (!errors.containsKey("username")) {
            Optional<UserEntity> user = userRepository.findByUsername(request.getUsername());
            if (user.isPresent() && !user.get().getUserId().equals(userId)) {
                errors.put("username", ErrorCode.ALREADY_EXIST.name());
            }
        }
        if (StringUtils.hasText(request.getPhoneNumber())) {
            Optional<UserEntity> user = userRepository.findByPhoneNumber(request.getPhoneNumber());
            if (user.isPresent() && !user.get().getUserId().equals(userId)) {
                errors.put("phoneNumber", ErrorCode.ALREADY_EXIST.name());
            }
        }
    }

}
