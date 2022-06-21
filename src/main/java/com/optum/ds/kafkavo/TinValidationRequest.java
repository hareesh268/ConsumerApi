package com.optum.ds.kafkavo;

import lombok.Data;

@Data
public class TinValidationRequest {

    public TinValidationRequest(){}

    private String tin;

    private boolean success;

    private String outcome;

    private String orgName;
}
