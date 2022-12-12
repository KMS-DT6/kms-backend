package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.constants.KmsConstant;
import com.backend.kmsproject.common.enums.KmsRole;
import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.model.dto.HistoryBookingDTO;
import com.backend.kmsproject.model.entity.BookingEntity;
import com.backend.kmsproject.model.entity.BookingOtherServiceEntity;
import com.backend.kmsproject.model.entity.SubFootballPitchEntity;
import com.backend.kmsproject.model.entity.UserEntity;
import com.backend.kmsproject.repository.dsl.UserDslRepository;
import com.backend.kmsproject.repository.jpa.BookingOtherServiceRepository;
import com.backend.kmsproject.repository.jpa.BookingRepository;
import com.backend.kmsproject.repository.jpa.SubFootballPitchRepository;
import com.backend.kmsproject.repository.jpa.UserRepository;
import com.backend.kmsproject.request.booking.CreateBookingRequest;
import com.backend.kmsproject.request.myaccount.UpdateMyAccountRequest;
import com.backend.kmsproject.response.ErrorResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.booking.GetBookingResponse;
import com.backend.kmsproject.response.booking.ListHistoryBookingResponse;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;
import com.backend.kmsproject.security.KmsPrincipal;
import com.backend.kmsproject.service.BookingService;
import com.backend.kmsproject.util.DatetimeUtils;
import com.backend.kmsproject.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SubFootballPitchRepository subFootballPitchRepository;
    private final UserDslRepository userDslRepository;
    private final BookingOtherServiceRepository bookingOtherServiceRepository;

    public void validFormatField(Map<String, String> errors, CreateBookingRequest request, KmsPrincipal principal) {
        if (request.getBookDay() == null) {
            errors.put("bookday", ErrorCode.MISSING_VALUE.name());
        } else if(request.getBookDay().isBefore(LocalDate.now())){
            errors.put("bookday", ErrorCode.INVALID_VALUE.name());
        }
        if (principal.getRole().equals(KmsRole.FOOTBALL_PITCH_ROLE.getRole()) && request.getUserid() == null) {
            errors.put("userid", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getSubFootballPitchId() == null){
            errors.put("subFootBallPitchId", ErrorCode.MISSING_VALUE.name());
        }
        if(request.getTimeEnd() == null){
            errors.put("timeEnd", ErrorCode.MISSING_VALUE.name());
        }
        if(request.getTimeStart() == null){
            errors.put("timeStart", ErrorCode.MISSING_VALUE.name());
        }
        if(request.getTimeStart()!=null && request.getTimeEnd()!=null){
            if(request.getTimeStart().isAfter(request.getTimeEnd())){
                errors.put("timeStart and TimeEnd", ErrorCode.INVALID_VALUE.name());
            }
        }
    }

    public void validExistField(Map<String, String> errors, CreateBookingRequest request, KmsPrincipal  principal) {
        if (principal.getRole().equals(KmsRole.FOOTBALL_PITCH_ROLE.getRole()) && !errors.containsKey("userid")) {
            Optional<UserEntity> user = userRepository.findById(request.getUserid());
            if (user.isEmpty()) {
                errors.put("userid", ErrorCode.NOT_FOUND.name());
            }
        }
        if (!errors.containsKey("subFootBallPitchId")) {
            Optional<SubFootballPitchEntity> subFootballPitch = subFootballPitchRepository.findById(request.getSubFootballPitchId());
            if (subFootballPitch.isEmpty()) {
                errors.put("subFootB00allPitchId", ErrorCode.NOT_FOUND.name());
            } else if(principal.getRole().equals(KmsRole.FOOTBALL_PITCH_ROLE.getRole())) {
                List<UserEntity> users = userDslRepository.listFootballPitchAdmin(subFootballPitch.get().getFootballPitch().getFootballPitchId());
                int s =0;
                for (UserEntity u:users
                     ) {
                    if(u.getUserId().equals(principal.getUserId())){
                        s++;
                        break;
                    }
                }
                if(s==0){
                    errors.put("subFootB00allPitchId", ErrorCode.INVALID_VALUE.name());
                }
            }

        }
    }
    @Override
    public OnlyIdResponse createBooking(CreateBookingRequest request) {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        Map<String, String> errors = new HashMap<>();
        validFormatField(errors, request, principal);
        validExistField(errors, request, principal);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        BookingEntity booking = new BookingEntity();
        booking.setBookDay(request.getBookDay());
        if(principal.getRole().equals(KmsRole.CUSTOMER_ROLE.getRole())){
            booking.setCustomer(userRepository.findById(principal.getUserId()).get());
            booking.setStatus(Boolean.FALSE);
        } else {
            booking.setCustomer(userRepository.findById(request.getUserid()).get());
            booking.setStatus(Boolean.TRUE);
        }
        booking.setTimeStart(request.getTimeStart());
        booking.setTimeEnd(request.getTimeEnd());
        booking.setIsPaid(Boolean.FALSE);
        booking.setSubFootballPitch(subFootballPitchRepository.findById(request.getSubFootballPitchId()).get());
        booking.setCreatedBy(principal.getUserId());
        booking.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        int hours = (int) Duration.between(booking.getTimeStart(), booking.getTimeEnd()).toHours();
        booking.setTotalPrice(booking.getSubFootballPitch().getPricePerHour() * hours);
        bookingRepository.save(booking);

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(booking.getBookingId())
                .build();
    }

    @Override
    public GetBookingResponse getBooking(Long idBooking) {
        Optional<BookingEntity> booking = bookingRepository.findById(idBooking);
        if(booking.isEmpty()){
            throw new NotFoundException("not found idBooking");
        }
        List<BookingOtherServiceEntity> bookingOtherServices = bookingOtherServiceRepository.findByBookingId(booking.get().getBookingId());
        return GetBookingResponse.builder()
                .setSuccess(true)
                .setBookingDTO(toBuilder(booking.get(),bookingOtherServices))
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
}
