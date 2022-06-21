package com.optum.ds.kafkavo;

import lombok.Data;

@Data
public class TokenResponse {

    private String access_token;
    private String token_type;
    private String expires_in;
    private String ext_expires_in;
}
