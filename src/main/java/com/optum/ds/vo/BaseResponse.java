package com.optum.ds.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.optum.ds.exception.DSError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DSError error;

}
