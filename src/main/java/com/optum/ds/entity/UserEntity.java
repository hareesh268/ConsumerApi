package com.optum.ds.entity;

import com.optum.ds.vo.model.JobFunction;
import com.optum.ds.vo.model.MultiTinOrg;
import com.optum.ds.vo.model.ProviderOrg;
import com.optum.ds.vo.model.UserStatus;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "user")
@Data
public class UserEntity {

    @Id
    private String id;

    private String uuid;
    private String optumId;
    
    private String orgType; // Billing | Provider | Vendor
    private String userType; // PA | IA | SU
    private UserStatus userStatus;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String phoneExt;
    private String oneHealthToken;
    
    private List<JobFunction> jobFunctions;

    //UHC Provider
    private List<ProviderOrg> tinInfo;

    private List<MultiTinOrg> multiTinInfo;

    //Need to be renamed to orgId
    private String uhcProviderId;

    //rename to intOrgId
    private String uhcIntProviderId;

    private String assignedAdmin;
    
    //data migration required fields 
    private String funcRoleSeqNbr;
    private String acssPrflSeqNbr;
    private String jobFunctionCode;
    private String npi;
    private String legacyId;
    
    @CreatedDate
    private LocalDateTime createdDt;
    @LastModifiedDate
    private LocalDateTime updatedDt;

    private LocalDateTime lastLoginDt;

    private LocalDateTime statusChangeDt;
    private LocalDateTime resetDt;

    private String statusChangeReason;
    private String resetReasonCode;
}
