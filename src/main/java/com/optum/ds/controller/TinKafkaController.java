package com.optum.ds.controller;

import com.optum.ds.exception.DSError;
import com.optum.ds.kafkavo.TinValidationResponse;
import com.optum.ds.service.TinValidateService;
import com.optum.ds.util.Constants;
import com.optum.ds.vo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TinKafkaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TinKafkaController.class);

    @Autowired
    public TinValidateService tinValidateService;

    @PostMapping(value = "/api/kafka/test/v1.0", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> postTinValidate(@Valid @RequestBody TinValidationResponse request) {
        BaseResponse baseResponse = new BaseResponse();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            tinValidateService.processTin(request);
            stopWatch.stop();
            LOGGER.info("TinKafkaController consumer Successful | for tin:{} | Duration(in MS):{}", request.getKey().getTin(), stopWatch.getTotalTimeMillis());
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            DSError errorResponse = new DSError("Error", Constants.TECH_ERROR_DESCRIPTION, HttpStatus.INTERNAL_SERVER_ERROR);
            stopWatch.stop();
            LOGGER.error("Exception occured while consuming message to topic for tin {}, Response Time {} , Exception {} , Error Message {}:" ,request.getKey().getTin(),stopWatch.getTotalTimeMillis(),e.getMessage(),e);
            baseResponse.setError(errorResponse);
            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
