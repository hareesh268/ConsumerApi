package com.optum.ds.vo.model;

import lombok.Data;

import java.util.List;

@Data
public class MultiTinOrg {

    private String uhcProviderId;
    private List<ProviderOrg> tinInfo;

}
