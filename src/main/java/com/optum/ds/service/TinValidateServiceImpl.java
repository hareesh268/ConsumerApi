package com.optum.ds.service;

import com.optum.ds.kafkavo.TinValidationRequest;
import com.optum.ds.kafkavo.TinValidationResponse;
import com.optum.ds.util.Constants;
import com.optum.ds.vo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TinValidateServiceImpl implements TinValidateService {

    @Value("${ds.irs.url}")
    private String irsUpdateUrl;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TinValidateServiceImpl.class.getCanonicalName());

    @Override
    public void processTin(TinValidationResponse tinValidationResponse) throws Exception {
        String token = tokenService.getIrsToken();
        TinValidationRequest tinValidationRequest = new TinValidationRequest();

        if (Constants.STATUS_SUCCESS.contains(tinValidationResponse.getResponseCode())) {
            tinValidationRequest.setSuccess(true);
        } else {
            tinValidationRequest.setSuccess(false);
        }
        tinValidationRequest.setTin(tinValidationResponse.getKey().getTin());
        tinValidationRequest.setOutcome(tinValidationResponse.getResponseCode());
        tinValidationRequest.setOrgName((tinValidationResponse.getOrgName()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(tinValidationRequest, headers);
        ResponseEntity<BaseResponse> elResponse1 = restTemplate.exchange(irsUpdateUrl, HttpMethod.POST, requestEntity, BaseResponse.class);
        LOGGER.warn("TinValidateServiceImpl:processTin{}", elResponse1.getStatusCode());
    }
}
