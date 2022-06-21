package com.optum.ds.service;

import com.optum.ds.kafkavo.Key;
import com.optum.ds.kafkavo.TinValidationResponse;
import com.optum.ds.service.TinValidateServiceImpl;
import com.optum.ds.service.TokenService;
import com.optum.ds.vo.BaseResponse;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TinValidateServiceImplTest {

    @InjectMocks
    private TinValidateServiceImpl mockService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TokenService tokenService;

    @Before
    public void before(){
        ReflectionTestUtils.setField(mockService, "irsUpdateUrl", "irsUpdateUrl");
    }

    @Test
    public void testProcessTin_withSuccessStatus() throws Exception
    {
        TinValidationResponse response = buildResponse("6");
        ResponseEntity<BaseResponse> responseEntity = getApiRespnse(HttpStatus.OK);
        ResponseEntity<Object> res = new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
        Mockito.when(tokenService.getIrsToken ()).thenReturn("token");
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(),ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>> any(),ArgumentMatchers.<Class<Object>> any())).thenReturn(res);
        mockService.processTin(response);
        verify(restTemplate,times(1)).exchange(ArgumentMatchers.anyString(),ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>> any(),ArgumentMatchers.<Class<Object>> any());
    }

    @Test
    public void testProcessTin_withFailedStatus() throws Exception
    {
        TinValidationResponse response = buildResponse("2");
        ResponseEntity<BaseResponse> responseEntity = getApiRespnse(HttpStatus.OK);
        ResponseEntity<Object> res = new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
        Mockito.when(tokenService.getIrsToken ()).thenReturn("token");
        Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(),ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>> any(),ArgumentMatchers.<Class<Object>> any())).thenReturn(res);
        mockService.processTin(response);
        verify(restTemplate,times(1)).exchange(ArgumentMatchers.anyString(),ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>> any(),ArgumentMatchers.<Class<Object>> any());
    }

    private ResponseEntity<BaseResponse> getApiRespnse(HttpStatus status){
        BaseResponse response = null;
        ResponseEntity<BaseResponse> result = null  ;
        response = new BaseResponse();
        return new ResponseEntity<BaseResponse>(response,status);
    }

    private TinValidationResponse buildResponse(String responseCode)
    {
        TinValidationResponse response = new TinValidationResponse();
        response.setAppId("2");
        response.setResponseCode(responseCode);
        Key key = new Key();
        key.setTin("521601006");
        response.setKey(key);
        return response;
    }
}

