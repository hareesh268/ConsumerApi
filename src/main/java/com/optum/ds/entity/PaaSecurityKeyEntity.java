package com.optum.ds.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "paaSecurityKey")
@Data
public class PaaSecurityKeyEntity {

    @Id
    private String id;
    private String uuid;
    private String uhcProviderId;
    private String corpName;
    private String securityKey;
    private String status;
    private String requesterFirstName;
    private String requesterLastName;
    private String requesterEmail;

    @CreatedDate
    private LocalDateTime createdDt;

    @LastModifiedDate
    private LocalDateTime updatedDt;
}