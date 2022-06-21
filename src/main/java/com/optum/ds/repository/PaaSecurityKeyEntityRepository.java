package com.optum.ds.repository;

import com.optum.ds.entity.PaaSecurityKeyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaaSecurityKeyEntityRepository extends MongoRepository<PaaSecurityKeyEntity, String> {

    List<PaaSecurityKeyEntity> findByUhcProviderIdAndStatus(String uhcProviderId,String status);

}