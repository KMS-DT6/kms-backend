package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.constants.KmsConstant;
import com.backend.kmsproject.model.entity.BookingEntity;
import com.backend.kmsproject.model.entity.UserEntity;
import com.backend.kmsproject.repository.jpa.BookingRepository;
import com.backend.kmsproject.repository.jpa.UserRepository;
import com.backend.kmsproject.request.booking.CreateBookingRequest;
import com.backend.kmsproject.request.myaccount.UpdateMyAccountRequest;
import com.backend.kmsproject.response.ErrorResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.security.KmsPrincipal;
import com.backend.kmsproject.service.BookingService;
import com.backend.kmsproject.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public void validFormatField(Map<String, String> errors, CreateBookingRequest request) {
        if (request.getBookDay() == null) {
            errors.put("bookday", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getUserid() == null) {
            errors.put("userid", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getSubFootballPitchId() == null){
            errors.put("subFootB00allPitchId", ErrorCode.MISSING_VALUE.name());
        }
        if(request.getTimeEnd() == null){
            errors.put("timeEnd", ErrorCode.MISSING_VALUE.name());
        }
        if(request.getTimeStart() == null){
            errors.put("timeStart", ErrorCode.MISSING_VALUE.name());
        }
        if(request.getTimeStart()!=null && request.getTimeEnd()!=null){
            if(request.getTimeStart().isAfter(request.getTimeEnd())){
                errors.put("time", ErrorCode.INVALID_VALUE.name());
            }
        }
    }

    public void validExistField(Map<String, String> errors, CreateBookingRequest request) {
        if (!errors.containsKey("userid")) {
            Optional<UserEntity> user = userRepository.findById(request.getUserid());
            if (user.isPresent()) {
                errors.put("userid", ErrorCode.NOT_FOUND.name());
            }
        }
        if (!errors.containsKey("subFootB00allPitchId")) {
            Optional<UserEntity> user = userRepository.findById(request.getUserid());
            if (user.isPresent()) {
                errors.put("subFootB00allPitchId", ErrorCode.NOT_FOUND.name());
            }
        }
    }
    @Override
    public OnlyIdResponse createBooking(CreateBookingRequest request) {
        Map<String, String> errors = new HashMap<>();
        validFormatField(errors, request);
        validFormatField(errors, request);
        validExistField(errors,request);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        BookingEntity booking = new BookingEntity();
        booking.setBookDay(request.getBookDay());
        booking.setCustomer(userRepository.findById(request.getUserid()).get());
        booking.setTimeStart(request.getTimeStart());
        booking.setTimeEnd(request.getTimeEnd());
        booking.setIsPaid(false);
        booking.setStatus(false);
        booking.setCreatedBy(principal.getUserId());
        booking.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        return null;
    }
}
