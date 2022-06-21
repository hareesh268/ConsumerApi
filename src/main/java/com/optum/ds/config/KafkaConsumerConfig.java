package com.optum.ds.config;

import com.optum.ds.util.Constants;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerConfig.class.getCanonicalName());
    private static final List<String> PASS_KEYS =
            Arrays.asList(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, SslConfigs.SSL_KEY_PASSWORD_CONFIG);

    @Value("${bootstrap.server.config}")
    private String bootstrapServers;

    @Value("${security.protocol.config}")
    private String securityProtocol;

    @Value("${ssl.truststore.location.config}")
    private String sslTruststoreLocation;

    @Value("${ssl.truststore.password.config}")
    private String sslTruststorePassword;

    @Value("${ssl.keystore.location.config}")
    private String sslKeystoreLocation;

    @Value("${ssl.keystore.password.config}")
    private String sslKeystorePassword;

    @Value("${ssl.key.password.config}")
    private String sslKeyPassword;

    @Value("${group.id.config}")
    private String groupId;

    @Value("${auto.offset.reset.config}")
    private String autoOffsetReset;

    @Value("${kafka.max.poll.records}")
    private Integer maxPollRecords;

    @Value("${kafka.autoCommit.interval.ms}")
    private Integer autoCommitInterval;

    @Value("${kafka.partition.assignment.strategy}")
    private String partitionStrategy;

    @Value("${kafka.key.deserializer}")
    private String keyDeserializer;

    @Value("${kafka.value.deserializer}")
    private String valueDeserializer;

    @Bean
    public Map<String, Object> consumerConfigs()
    {
        Map<String, Object> props = new HashMap<>();

        try {

            LOGGER.info("DS-TINVALIDATE-CONSUMER | KafkaConsumerConfig Started.");
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, sslTruststoreLocation);
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslTruststorePassword);
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, sslKeystoreLocation);
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, sslKeystorePassword);
            props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, sslKeyPassword);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
            props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
            props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, partitionStrategy);
            
            // Printing kafka properties received from config file
            String configProps = props.entrySet().stream().map(entry -> String.format("%1$s: %2$s", entry.getKey(),
                    PASS_KEYS.contains(entry.getKey()) ? StringUtils.isEmpty(entry.getValue()) ? entry.getValue() : Constants.PASS_WRAPPER
                            : entry.getValue())).collect(Collectors.joining(","));

            LOGGER.warn(Constants.FUNCTIONALITY_SPLUNK_LOGGING, "Kafka properties received from config: \n {}", configProps);
            LOGGER.info("DS-TINVALIDATE-CONSUMER | KafkaConsumerConfig Completed.");
        }catch(Exception ex)
        {
            LOGGER.error("DS-TINVALIDATE-CONSUMER | Exception in KafkaConsumerConfig method ex:{}",ex.getMessage(), ex);
        }
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
