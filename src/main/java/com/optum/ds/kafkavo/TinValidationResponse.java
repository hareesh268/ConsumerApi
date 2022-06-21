package com.optum.ds.kafkavo;

import lombok.Data;

@Data
public class TinValidationResponse {

    public TinValidationResponse(){}

    private String responseCode;

    private String cleansedOrgName;

    private String orgName;

    private Key key;

    private String appId ;

}
