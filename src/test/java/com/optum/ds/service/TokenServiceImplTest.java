package com.optum.ds.service;

import com.optum.ds.exception.DSError;
import com.optum.ds.kafkavo.TokenResponse;
import com.optum.ds.util.RestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceImplTest {
    @InjectMocks
    private TokenServiceImpl mockService;

    @Mock
    private EhCacheService ehCacheService;

    @Mock
    private RestUtil restUtil;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void before(){
        ReflectionTestUtils.setField(mockService, "irsClientId", "irsClientId");
        ReflectionTestUtils.setField(mockService, "irsClientSecret", "irsClientSecret");
        ReflectionTestUtils.setField(mockService, "tokenUrl", "tokenUrl");
    }

    @Test
   public void testgetToken()
   {
       TokenBuilder builder = getTokenBuilder();
       ResponseEntity<TokenResponse> responseEntity = getApiRespnse(HttpStatus.OK);
       ResponseEntity<Object> res = new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
       Mockito.when(ehCacheService.getElement (Mockito.any())).thenReturn("");
       Mockito.when(restUtil.getRestTemplate ()).thenReturn(restTemplate);
       //Mockito.doNothing().when(ehCacheService).addElement(Mockito.anyString(),Mockito.any(),Mockito.anyString());
       Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(),ArgumentMatchers.any(HttpMethod.class),
               ArgumentMatchers.<HttpEntity<?>> any(),ArgumentMatchers.<Class<Object>> any())).thenReturn(res);
       String accessToken = mockService.getIrsToken();
       String token = mockService.getToken(builder);
       assertNotNull(token);
       assertNotNull(accessToken);
       assertEquals("th1342fsg",token);
   }

    @Test(expected = DSError.class)
    public void testgetToken_withFailedStatus() throws RuntimeException
    {
        TokenBuilder builder = getTokenBuilder();
        ResponseEntity<TokenResponse> responseEntity = getApiRespnse(HttpStatus.OK);
        ResponseEntity<Object> res = new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
        Mockito.when(ehCacheService.getElement (Mockito.any())).thenReturn("");
        Mockito.when(restUtil.getRestTemplate ()).thenReturn(restTemplate);
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(),ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>> any(),ArgumentMatchers.<Class<Object>> any())).thenThrow(new RuntimeException("Failed to process"));
        String accessToken = mockService.getIrsToken();
        String token = mockService.getToken(builder);
    }

    private ResponseEntity<TokenResponse> getApiRespnse(HttpStatus status){
        TokenResponse response = null;
        ResponseEntity<TokenResponse> result = null  ;
        response = new TokenResponse();
        response.setAccess_token("th1342fsg");
        response.setExpires_in("10");
        return new ResponseEntity<TokenResponse>(response,status);
    }

    private TokenBuilder getTokenBuilder() {
        TokenBuilder tokenBuilder = new TokenBuilder();
        tokenBuilder.setScope("api://86e0105a-7bca-4822-b14c-110c74bcba26/.default");
        tokenBuilder.setType("irs");
        tokenBuilder.setCacheKey("gujghjv");
        tokenBuilder.setClientId("ee609226-0e75-4389-af2d-3cf43e12acd2");
        tokenBuilder.setEndpoint("https://apim-qa-dv.azure-api.net/register-api/security/irs/tin/v1.0");
        tokenBuilder.setTokenRequestFaultCode("100");
        return tokenBuilder;
    }
}
