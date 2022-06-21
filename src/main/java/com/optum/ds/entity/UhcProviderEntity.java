package com.optum.ds.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "uhcProvider")
public class UhcProviderEntity {

    @Id
    private String id;
    private String mpin;
    private String uhcProviderId;
    private String firstName;
    private String lastName;

    @CreatedDate
    private LocalDateTime createdDt;

    @LastModifiedDate
    private LocalDateTime updatedDt;

}
