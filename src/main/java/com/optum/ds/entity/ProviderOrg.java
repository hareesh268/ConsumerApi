package com.optum.ds.entity;


import lombok.Data;

import java.util.List;

@Data
public class ProviderOrg {
    private String tin;
    private List<Permission> permissions;
    private String status;

}
