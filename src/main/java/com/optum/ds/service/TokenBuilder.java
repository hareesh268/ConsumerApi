package com.optum.ds.service;

import lombok.Data;

@Data
public class TokenBuilder {
    private String clientId;
    private String clientSecret;
    private String endpoint;
    private String cacheKey;
    private String type;
    private String scope;
    private String tokenRequestFaultCode; // can always add a faultCode class object here if need more faultCodes
}
