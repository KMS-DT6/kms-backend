package com.backend.kmsproject.service;

import com.backend.kmsproject.request.otherservice.CreateUpdateOtherServiceRequest;
import com.backend.kmsproject.request.otherservice.GetListOtherServiceRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.otherservice.GetOtherServiceResponse;
import com.backend.kmsproject.response.otherservice.ListOtherServiceResponse;

public interface OtherService {
    ListOtherServiceResponse getListOtherService(GetListOtherServiceRequest request);

    GetOtherServiceResponse getOtherService(Long otherServiceId);

    OnlyIdResponse createOtherService(CreateUpdateOtherServiceRequest request);

    OnlyIdResponse updateOtherService(Long otherServiceId, CreateUpdateOtherServiceRequest request);

    NoContentResponse deleteOtherService(Long otherServiceId);
}
