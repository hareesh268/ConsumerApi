package com.optum.ds.service;

import com.optum.ds.exception.DSError;
import com.optum.ds.kafkavo.TokenResponse;
import com.optum.ds.util.Constants;
import com.optum.ds.util.RestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class.getCanonicalName());

    @Value("${ds.irs.clientId}")
    private String irsClientId ;

    @Value("${ds.irs.clientSecret}")
    private String irsClientSecret ;

    @Value("${ds.irs.tokenUrl}")
    private String tokenUrl ;

    @Autowired
    EhCacheService ehCacheService;

    @Autowired
    RestUtil restUtil;

    public String getIrsToken(){
        TokenBuilder tokenBuilder = new TokenBuilder();
        tokenBuilder.setClientId(irsClientId);
        tokenBuilder.setClientSecret(irsClientSecret);
        tokenBuilder.setEndpoint(tokenUrl);
        tokenBuilder.setCacheKey("IRS_TOKEN");
        return getToken(tokenBuilder);
    }

    public String getToken(TokenBuilder tokenBuilder) {
        String token = null;
        token = ehCacheService.getElement(tokenBuilder.getCacheKey());
        if (StringUtils.isBlank(token)) {
            int timeOut = 0;
            String tokenArrayString = null;
            tokenArrayString = invoke(tokenBuilder);
            String[] tokenArray = tokenArrayString.split(Constants.STR_SEPERATOR);
            if (tokenArray != null && tokenArray.length > 1) {
                token = tokenArray[0];
                timeOut = Integer.parseInt(tokenArray[1]);
                if (timeOut > 20) {
                    timeOut = timeOut - 20;
                } else {
                    timeOut = 10;
                }
            }
            ehCacheService.addElement(token,timeOut,tokenBuilder.getCacheKey());
        }
        return token;
    }

    public String invoke(TokenBuilder tokenBuilder) throws DSError {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();

            bodyMap.add(Constants.TOKEN_GRANT_TYPE, Constants.TOKEN_CLIENT_CREDENTIALS);
            bodyMap.add(Constants.TOKEN_CLIENT_ID, tokenBuilder.getClientId());
            bodyMap.add(Constants.TOKEN_CLIENT_SECRET, tokenBuilder.getClientSecret());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyMap, headers);
            RestTemplate restTemplate = restUtil.getRestTemplate();
            LOGGER.warn("New token generating for, Endpoint: {}, ClientId: {}, Tokentype: {} ", tokenBuilder.getEndpoint(), tokenBuilder.getClientId(), tokenBuilder.getCacheKey());
            ResponseEntity<TokenResponse> response   = restTemplate.exchange(tokenBuilder.getEndpoint(), HttpMethod.POST, request, TokenResponse.class);
            return response.getBody().getAccess_token() + Constants.STR_SEPERATOR + response.getBody().getExpires_in();

        } catch(Exception ex){
            LOGGER.error("Exception in Token service for token type: "+ tokenBuilder.getCacheKey(),ex);
            throw new DSError(Constants.TOKEN_SERVICE_FAILED_FAULT_CODE, Constants.TECH_ERROR_DESCRIPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
