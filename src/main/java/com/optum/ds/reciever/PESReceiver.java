package com.optum.ds.reciever;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optum.ds.kafkavo.PESResponse;
import com.optum.ds.service.ProviderValidationService;
import com.optum.ds.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

@Service
public class PESReceiver {

    @Autowired
    private ProviderValidationService providerValidationService;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${pes.topic.consume}")
    private String pesConsumeToggle;

    private static final Logger LOGGER = LoggerFactory.getLogger(PESReceiver.class.getCanonicalName());

    @KafkaListener(topics = {"${aws.topic}"}, containerFactory = "PESKafkaListenerContainerFactory")
    public void updateTinValidate(String pesResponse) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            if(Constants.ACTIVE.equalsIgnoreCase(pesConsumeToggle)) {
                LOGGER.warn("Kafka PES consumer message:{}", pesResponse);
                if (pesResponse != null) {
                    PESResponse pesResponseVO = objectMapper.readValue(pesResponse, PESResponse.class);
                    if(!CollectionUtils.isEmpty(pesResponseVO.getCorpOwnerLetterGenerationList())) {
                        providerValidationService.providerValidation(pesResponseVO);
                    }
                    else{
                        LOGGER.error("Empty list returned from PES from topic {}","${aws.topic}");
                    }
                }
                stopWatch.stop();
                LOGGER.warn("Kafka PES consumer message process time:{}", stopWatch.getTotalTimeMillis());
            }
        } catch (Exception e) {
            stopWatch.stop();
            LOGGER.error("Exception while consuming message from topic {} , Response Time {} , Error Message {}:","${aws.topic}", stopWatch.getTotalTimeMillis(), e.getMessage(), e);
        }
    }
}

