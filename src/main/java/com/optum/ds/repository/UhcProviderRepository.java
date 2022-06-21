
package com.optum.ds.repository;

import com.optum.ds.entity.UhcProviderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UhcProviderRepository extends MongoRepository<UhcProviderEntity, String> {

   List<UhcProviderEntity> findByMpin(String mpin);

}
