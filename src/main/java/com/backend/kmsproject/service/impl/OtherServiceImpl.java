package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.request.otherservice.CreateUpdateOtherServiceRequest;
import com.backend.kmsproject.request.otherservice.GetListOtherServiceRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.otherservice.GetOtherServiceResponse;
import com.backend.kmsproject.response.otherservice.ListOtherServiceResponse;
import com.backend.kmsproject.service.OtherService;
import org.springframework.stereotype.Service;

@Service
public class OtherServiceImpl implements OtherService {
    @Override
    public ListOtherServiceResponse getListOtherService(GetListOtherServiceRequest request) {
        return null;
    }

    @Override
    public GetOtherServiceResponse getOtherService(Long otherServiceId) {
        return null;
    }

    @Override
    public OnlyIdResponse createOtherService(CreateUpdateOtherServiceRequest request) {
        return null;
    }

    @Override
    public OnlyIdResponse updateOtherService(Long otherServiceId, CreateUpdateOtherServiceRequest request) {
        return null;
    }

    @Override
    public NoContentResponse deleteOtherService(Long otherServiceId) {
        return null;
    }
}
