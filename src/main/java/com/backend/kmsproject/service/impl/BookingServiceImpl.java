package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.constants.KmsConstant;
import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.model.dto.BookingDTO;
import com.backend.kmsproject.model.dto.HistoryBookingDTO;
import com.backend.kmsproject.model.entity.*;
import com.backend.kmsproject.repository.dsl.BookingDslRepository;
import com.backend.kmsproject.repository.dsl.UserDslRepository;
import com.backend.kmsproject.repository.jpa.*;
import com.backend.kmsproject.request.booking.CreateBookingRequest;
import com.backend.kmsproject.request.booking.GetListBookingRequest;
import com.backend.kmsproject.request.bookingotherservice.BookingOtherServiceRequest;
import com.backend.kmsproject.response.ErrorResponse;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.booking.GetBookingResponse;
import com.backend.kmsproject.response.booking.ListBookingResponse;
import com.backend.kmsproject.response.booking.ListHistoryBookingResponse;
import com.backend.kmsproject.security.KmsPrincipal;
import com.backend.kmsproject.service.BookingService;
import com.backend.kmsproject.util.DatetimeUtils;
import com.backend.kmsproject.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SubFootballPitchRepository subFootballPitchRepository;
    private final UserDslRepository userDslRepository;
    private final BookingOtherServiceRepository bookingOtherServiceRepository;
    private final BookingDslRepository bookingDslRepository;
    private final OtherServiceRepository otherServiceRepository;

    public void validFormatField(Map<String, String> errors, CreateBookingRequest request) {
        if (request.getBookDay() == null) {
            errors.put("bookDay", ErrorCode.MISSING_VALUE.name());
        } else if (request.getBookDay().isBefore(LocalDate.now())) {
            errors.put("bookDay", ErrorCode.INVALID_VALUE.name());
        }
        if (request.getSubFootballPitchId() == null) {
            errors.put("subFootBallPitchId", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getTimeEnd() == null) {
            errors.put("timeEnd", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getTimeStart() == null) {
            errors.put("timeStart", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getTimeStart() != null && request.getTimeEnd() != null) {
            if (request.getTimeStart().isAfter(request.getTimeEnd())) {
                errors.put("time", ErrorCode.INVALID_VALUE.name());
            } else {
                long hours = Duration.between(request.getTimeStart(), request.getTimeEnd()).toHours();
                if (hours != 1 && hours != 2) {
                    errors.put("time", ErrorCode.INVALID_VALUE.name());
                }
            }
        }
    }

    public void validExistField(Map<String, String> errors, CreateBookingRequest request) {
        if (!errors.containsKey("subFootBallPitchId")) {
            Optional<SubFootballPitchEntity> subFootballPitch = subFootballPitchRepository.findById(request.getSubFootballPitchId());
            if (subFootballPitch.isEmpty()) {
                errors.put("subFootBallPitchId", ErrorCode.NOT_FOUND.name());
            }
        }
        if (!errors.containsKey("subFootBallPitchId") && request.getBookingOtherService() != null && request.getBookingOtherService().size() > 0) {
            for (BookingOtherServiceRequest b : request.getBookingOtherService()
            ) {
                Optional<OtherServiceEntity> otherService = otherServiceRepository.findById(b.getOtherServiceId());
                SubFootballPitchEntity subFootballPitch = subFootballPitchRepository.findById(request.getSubFootballPitchId()).get();
                if (otherService.isEmpty()) {
                    errors.put("bookingOtherService", ErrorCode.NOT_FOUND.name());
                    break;
                } else if (!otherService.get().getFootballPitch().getFootballPitchId()
                        .equals(subFootballPitch.getFootballPitch().getFootballPitchId())) {
                    errors.put("bookingOtherService", ErrorCode.NOT_FOUND.name());
                    break;
                }
            }
        }
    }

    @Override
    public OnlyIdResponse createBooking(CreateBookingRequest request) {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        Map<String, String> errors = new HashMap<>();
        validFormatField(errors, request);
        validExistField(errors, request);
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
        booking.setCustomer(userRepository.findById(principal.getUserId()).get());
        booking.setStatus(KmsConstant.WAITING);
        booking.setTimeStart(request.getTimeStart());
        booking.setTimeEnd(request.getTimeEnd());
        booking.setIsPaid(Boolean.FALSE);
        booking.setSubFootballPitch(subFootballPitchRepository.findById(request.getSubFootballPitchId()).get());
        booking.setCreatedBy(principal.getUserId());
        booking.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        int hours = (int) Duration.between(booking.getTimeStart(), booking.getTimeEnd()).toHours();
        booking.setTotalPrice(booking.getSubFootballPitch().getPricePerHour() * hours);
        bookingRepository.save(booking);
        if (request.getBookingOtherService() != null && request.getBookingOtherService().size() > 0) {
            List<BookingOtherServiceEntity> bookingOtherServiceEntities = new ArrayList<>();
            for (BookingOtherServiceRequest b : request.getBookingOtherService()
            ) {
                BookingOtherServiceEntity bookingOtherService = new BookingOtherServiceEntity();
                bookingOtherService.setBooking(booking);
                bookingOtherService.setOtherService(otherServiceRepository.findById(b.getOtherServiceId()).get());
                bookingOtherService.setQuantity(b.getQuantity());
                bookingOtherServiceEntities.add(bookingOtherService);
            }
            bookingOtherServiceRepository.saveAll(bookingOtherServiceEntities);
        }

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(booking.getBookingId())
                .build();
    }

    @Override
    public GetBookingResponse getBooking(Long idBooking) throws AccessDeniedException {
        Optional<BookingEntity> booking = bookingRepository.findById(idBooking);
        if (booking.isEmpty()) {
            throw new NotFoundException("Not found booking");
        }
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        if (!checkAuthority(booking.get(), principal)) {
            throw new AccessDeniedException("Access Denied");
        }
        List<BookingOtherServiceEntity> bookingOtherServices = bookingOtherServiceRepository.findByBookingId(booking.get().getBookingId());
        return GetBookingResponse.builder()
                .setSuccess(true)
                .setBookingDTO(toBuilder(booking.get(), bookingOtherServices))
                .build();
    }

    public boolean checkAuthority(BookingEntity booking, KmsPrincipal principal) {
        if (principal.isCustomer() &&
                !booking.getCustomer().getUserId().equals(principal.getUserId())) {
            return false;
        } else if (principal.isFootballPitchAdmin()) {
            List<UserEntity> users = userDslRepository.listFootballPitchAdmin(booking.getSubFootballPitch().getFootballPitch().getFootballPitchId());
            int s = 0;
            for (UserEntity u : users
            ) {
                if (u.getUserId().equals(principal.getUserId())) {
                    s++;
                    break;
                }
            }
            if (s == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public NoContentResponse deleteBooking(Long idBooking) throws AccessDeniedException {
        Optional<BookingEntity> booking = bookingRepository.findById(idBooking);
        if (booking.isEmpty()) {
            throw new NotFoundException("not found idBooking");
        }
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        if (!booking.get().getCustomer().getUserId().equals(principal.getUserId())) {
            throw new AccessDeniedException("access deined");
        }
        long hour = Duration.between(LocalTime.now(), booking.get().getTimeStart()).toHours();
        int date = LocalDate.now().compareTo(booking.get().getBookDay());
        if (date == 0 && hour < 6) {
            throw new AccessDeniedException("Can not delete booking");
        }
        booking.get().setStatus(KmsConstant.CANCELED);
        bookingRepository.save(booking.get());
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    @Override
    public OnlyIdResponse updateBooking(CreateBookingRequest request, Long id) throws AccessDeniedException {
        Optional<BookingEntity> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new NotFoundException("Not found booking");
        }
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        Map<String, String> errors = new HashMap<>();
        if (!booking.get().getCustomer().getUserId().equals(principal.getUserId())) {
            throw new AccessDeniedException("Access Denied");
        }
        long hour = Duration.between(LocalTime.now(), booking.get().getTimeStart()).toHours();
        int d = LocalDate.now().compareTo(booking.get().getBookDay());
        if (d == 0 && hour < 6) {
            throw new AccessDeniedException("Can not delete booking");
        }
        validFormatField(errors, request);
        validExistField(errors, request);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        booking.get().setBookDay(request.getBookDay());
        booking.get().setTimeStart(request.getTimeStart());
        booking.get().setTimeEnd(request.getTimeEnd());
        booking.get().setSubFootballPitch(subFootballPitchRepository.findById(request.getSubFootballPitchId()).get());
        int hours = (int) Duration.between(booking.get().getTimeStart(), booking.get().getTimeEnd()).toHours();
        booking.get().setTotalPrice(booking.get().getSubFootballPitch().getPricePerHour() * hours);
        booking.get().setModifiedBy(principal.getUserId());
        booking.get().setModifiedDate(new Timestamp(System.currentTimeMillis()));
        booking.get().setStatus(KmsConstant.WAITING);
        if (request.getBookingOtherService() != null && request.getBookingOtherService().size() > 0) {
            List<BookingOtherServiceEntity> list = bookingOtherServiceRepository.findByBookingId(booking.get().getBookingId());
            bookingOtherServiceRepository.deleteAll(list);
            List<BookingOtherServiceEntity> bookingOtherServiceEntities = new ArrayList<>();
            for (BookingOtherServiceRequest b : request.getBookingOtherService()
            ) {
                BookingOtherServiceEntity bookingOtherService = new BookingOtherServiceEntity();
                bookingOtherService.setBooking(booking.get());
                bookingOtherService.setOtherService(otherServiceRepository.findById(b.getOtherServiceId()).get());
                bookingOtherService.setQuantity(b.getQuantity());
                bookingOtherServiceEntities.add(bookingOtherService);
            }
            bookingOtherServiceRepository.saveAll(bookingOtherServiceEntities);
        }
        bookingRepository.save(booking.get());
        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(booking.get().getBookingId())
                .build();
    }

    @Override
    public ListBookingResponse getListBooking(GetListBookingRequest request) {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        List<BookingEntity> bookings = bookingDslRepository.listBookingByFootBallPitch(request, principal.getFootballPitchId());
        List<BookingDTO> historyBookings = new ArrayList<>();
        bookings.forEach(b -> {
            List<BookingOtherServiceEntity> bookingOtherServices = bookingOtherServiceRepository.findByBookingId(b.getBookingId());
            historyBookings.add(toBuilder(b, bookingOtherServices));
        });
        return ListBookingResponse.builder()
                .setSuccess(true)
                .setBookingDTOS(historyBookings)
                .build();
    }

    @Override
    public NoContentResponse acceptBooking(Long idBooking) throws AccessDeniedException {
        Optional<BookingEntity> booking = bookingRepository.findById(idBooking);
        if (booking.isEmpty()) {
            throw new NotFoundException("Not found booking");
        }
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        if (!checkAuthority(booking.get(), principal)) {
            throw new AccessDeniedException("Access Denied");
        }
        booking.get().setStatus(KmsConstant.ACCEPTED);
        bookingRepository.save(booking.get());
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    private BookingDTO toBuilder(BookingEntity booking, List<BookingOtherServiceEntity> bookingOtherServices) {
        BookingDTO.BookingDTOBuilder builder = BookingDTO.builder();
        builder.setBookingId(booking.getBookingId())
                .setStatus(booking.getStatus())
                .setIsPaid(Boolean.TRUE.equals(booking.getIsPaid()) ? Boolean.TRUE : Boolean.FALSE)
                .setBookDate(DatetimeUtils.formatLocalDate(booking.getBookDay()))
                .setTimeStart(DatetimeUtils.formatLocalTime(booking.getTimeStart()))
                .setTimeEnd(DatetimeUtils.formatLocalTime(booking.getTimeEnd()))
                .setPricePitch(booking.getSubFootballPitch().getPricePerHour())
                .setTotalPriceIncludeService(totalPriceIncludeOtherService(booking, bookingOtherServices))
                .setFootballPitch(BookingDTO.FootballPitch.builder()
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
                .setOtherService(BookingDTO.OtherService.builder()
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
                        .build())
                .setCustomer(BookingDTO.UserDto.builder()
                        .setFirstName(booking.getCustomer().getFirstName())
                        .setLastName(booking.getCustomer().getLastName())
                        .setUserId(booking.getCustomer().getUserId())
                        .setPhoneNumber(booking.getCustomer().getPhoneNumber())
                        .build())
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
