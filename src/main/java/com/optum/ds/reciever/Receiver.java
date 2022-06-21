package com.optum.ds.reciever;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optum.ds.kafkavo.TinValidationResponse;
import com.optum.ds.service.TinValidateService;
import com.optum.ds.util.Constants;
import com.optum.ds.util.RestUtil;
import com.optum.ds.vo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
@Service
public class Receiver {

    @Autowired
    RestUtil restUtil1;

    @Autowired
    private TinValidateService tinValidateService;

    @Autowired
    ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class.getCanonicalName());

    @KafkaListener(topics = "${kafka.srcTopic}", containerFactory = "kafkaListenerContainerFactory")
    public void updateTinValidate(String tinValidationResponse) {
        BaseResponse baseResponse = new BaseResponse();
        TinValidationResponse tinValidationResponseVO = new TinValidationResponse();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            LOGGER.warn("Kafka consumer message:{}" ,tinValidationResponse);
            if (tinValidationResponse != null) {
                tinValidationResponseVO = objectMapper.readValue(tinValidationResponse, TinValidationResponse.class);
                if(Constants.APP_ID.equals(tinValidationResponseVO.getAppId())){
                    tinValidateService.processTin(tinValidationResponseVO);
                }
            }
            stopWatch.stop();
            LOGGER.warn("Kafka consumer message process time:{}" ,stopWatch.getTotalTimeMillis());

        } catch (Exception e) {
            stopWatch.stop();
            LOGGER.error("Exception occured while consuming message to topic , Response Time {} , Exception {} , Error Message {}:", stopWatch.getTotalTimeMillis(), e.getMessage(), e);
        }
    }
}
