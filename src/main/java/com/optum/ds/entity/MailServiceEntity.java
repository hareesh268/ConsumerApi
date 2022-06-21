package com.optum.ds.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "mailService")
@Data
public class MailServiceEntity {

    @Id
    private String id;

    private String type;
    private String tin;
    private String mPin;
    private String securityKey;

    //Requester
    private String requesterFirstName;
    private String requesterLastName;
    private String requesterOrgName;
    private String requesterEmail;
    private String requesterPhone;

    //Approver
    private String approvalOrgName;
    private String approvalFirstName;
    private String approvalLastName;

    private String approvalAddressLine1;
    private String approvalAddressLine2;
    private String approvalCity;
    private String approvalState;
    private String approvalZipCode;

    @CreatedDate
    private LocalDateTime createdDt;
}
