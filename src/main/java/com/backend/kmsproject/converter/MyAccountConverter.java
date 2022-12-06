package com.backend.kmsproject.converter;

import com.backend.kmsproject.model.dto.MyAccountDTO;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.user.MyAccountResponse;
import com.backend.kmsproject.util.DatetimeUtils;
import org.springframework.stereotype.Component;

@Component
public class MyAccountConverter extends CommonConverter {
    public Response<MyAccountDTO> getSuccess(MyAccountResponse response) {
        return Response.<MyAccountDTO>builder()
                .setSuccess(true)
                .setData(MyAccountDTO.builder()
                        .setUser(response.getUser())
                        .setAuthorities(response.getAuthorities())
                        .build())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }
}
