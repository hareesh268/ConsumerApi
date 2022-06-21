//package com.optum.ds.reciever;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.optum.ds.entity.UserEntity;
//import com.optum.ds.kafkavo.PreferenceCenterResponse;
//import com.optum.ds.repository.UserEntityRepository;
//import com.optum.ds.util.Constants;
//import com.optum.ds.util.RestUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StopWatch;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class PreferenceCenterReceiver {
//
//    @Value("${kafkaConsumer.ppcTopic.status}")
//    private String ppcConsumerStatus;
//
//    @Autowired
//    RestUtil restUtil;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    UserEntityRepository userEntityRepository;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(PreferenceCenterReceiver.class.getCanonicalName());
//
//    @KafkaListener(topics = "${kafkaTest.topic}", containerFactory = "alphaKafkaListenerContainerFactory")
//    public void updatePhoneNumbers(String response) {
//
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//
//        try {
//            LOGGER.warn("Kafka consumer message:{}" ,response);
//            if (Constants.ACTIVE.equalsIgnoreCase(ppcConsumerStatus) && response != null) {
//                PreferenceCenterResponse preferenceCenterResponse = objectMapper.readValue(response, PreferenceCenterResponse.class);
//
//                List<UserEntity> userEntityList = userEntityRepository.findByUuid(preferenceCenterResponse.getUuid());
//                if (!CollectionUtils.isEmpty(userEntityList)) {
//                    List<UserEntity> updatePhoneDetails = userEntityList.stream().map(user -> {
//                        if (!StringUtils.isEmpty(preferenceCenterResponse.getPhoneNumber())) {
//                            user.setPhone(preferenceCenterResponse.getPhoneNumber());
//                        }
//                        if (!StringUtils.isEmpty(preferenceCenterResponse.getPhoneExtension())) {
//                            user.setPhoneExt(preferenceCenterResponse.getPhoneExtension());
//                        }
//                        return user;
//                    }).collect(Collectors.toList());
//                    userEntityRepository.saveAll(updatePhoneDetails);
//                }
//            }
//            stopWatch.stop();
//            LOGGER.warn("Kafka alpha consumer message process time:{}" ,stopWatch.getTotalTimeMillis());
//        } catch (Exception e) {
//            stopWatch.stop();
//            LOGGER.error("Exception occured while consuming message to topic , Response Time {} , Exception {} , Error Message {}:", stopWatch.getTotalTimeMillis(), e.getMessage(), e);
//        }
//    }
//}
