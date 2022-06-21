package com.optum.ds.repository;

import com.optum.ds.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEntityRepository extends MongoRepository<UserEntity, String> {
    List<UserEntity> findByUuid(String uuid);
    Long countByUhcProviderId(String uhcProviderId);
}
