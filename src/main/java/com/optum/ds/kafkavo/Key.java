package com.optum.ds.kafkavo;

import lombok.Data;

@Data
public class Key {

    private String tin;

    private String correlationId;
}
