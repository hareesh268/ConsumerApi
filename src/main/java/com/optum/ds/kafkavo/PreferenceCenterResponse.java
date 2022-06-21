package com.optum.ds.kafkavo;

import lombok.Data;

@Data
public class PreferenceCenterResponse {

    private String uuid;
    private String phoneNumber;
    private String phoneExtension;
    private String oldPhoneNumber;
    private String oldPhoneExtension;
    private String timestamp;

}
