package com.optum.ds.kafkavo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CorpOwnerLetterGeneration {

    @JsonProperty("PROV_ID")
    private String provId;

    @JsonProperty("PRO_FST_NM")
    private String proFstNm;

    @JsonProperty("PRO_LST_NM")
    private String proLstNm;

    @JsonProperty("ADR_ID")
    private String adrId;

    @JsonProperty("ADR_ADR_LN_1_TXT")
    private String adrAdrLn1Txt;

    @JsonProperty("ADR_CTY_NM")
    private String adrCtyNm;

    @JsonProperty("ADR_ST_CD")
    private String adrStCd;

    @JsonProperty("ADR_ZIP_CD")
    private String adrZipCd;

    @JsonProperty("ADR_ZIP_PLS_4_CD")
    private String adrZipPls4Cd;

    @JsonProperty("MKT_CTY_NM")
    private String mktCityNm;


}
