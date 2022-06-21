package com.optum.ds.config;

import com.optum.ds.util.Constants;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
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
public class PESConsumerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PESConsumerConfig.class.getCanonicalName());
    private static final List<String> PASS_KEYS =
            Arrays.asList(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, SslConfigs.SSL_KEY_PASSWORD_CONFIG);

    @Value("${aws.bootstrapServerConfig}")
    private String bootstrapServers;

    @Value("${aws.groupId}")
    private String groupId;

    @Value("${aws.userName}")
    private String userName;

    @Value("${aws.password}")
    private String password;

    @Bean
    public Map<String, Object> PESConsumerConfigs()
    {
        Map<String, Object> props = new HashMap<>();

        try {

            LOGGER.info("DS-PES-CONSUMER | KafkaConsumerConfig Started.");
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            props.put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, "TLSv1.2");
            props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" + userName + "\" password=\"" + password + "\";");

            // Printing kafka properties received from config file
            String configProps = props.entrySet().stream().map(entry -> String.format("%1$s: %2$s", entry.getKey(),
                    PASS_KEYS.contains(entry.getKey()) ? StringUtils.isEmpty(entry.getValue()) ? entry.getValue() : Constants.PASS_WRAPPER
                            : entry.getValue())).collect(Collectors.joining(","));

            LOGGER.warn(Constants.FUNCTIONALITY_SPLUNK_LOGGING, "Kafka properties received from config: \n {}", configProps);
            LOGGER.info("DS-PES-CONSUMER | KafkaConsumerConfig Completed.");

        }catch(Exception ex) {
            LOGGER.error("DS-PES-CONSUMER | Exception in KafkaConsumerConfig method ex:{}",ex.getMessage(),ex);
        }
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> PESConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(PESConsumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> PESKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(PESConsumerFactory());
        return factory;
    }

}
