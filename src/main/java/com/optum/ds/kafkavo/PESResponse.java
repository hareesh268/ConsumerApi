package com.optum.ds.kafkavo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PESResponse {

    public PESResponse(){}

    private String correlationIdentifier;

    @JsonProperty("CORP_OWNER_LETTER_GENERATION")
    private List<CorpOwnerLetterGeneration> corpOwnerLetterGenerationList;
}
