package com.optum.ds.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.optum.ds.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties( { "cause","stackTrace","message","localizedMessage","suppressed" })
public class DSError extends RuntimeException {

    private String code;
    private String description;
    private String timestamp;

    @JsonIgnore
    private HttpStatus httpStatus;

    public DSError(String code ) {
        this.code = code;
    }

    public DSError(String code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
        this.timestamp = Constants.errorTimeStamp();
    }
    
}
