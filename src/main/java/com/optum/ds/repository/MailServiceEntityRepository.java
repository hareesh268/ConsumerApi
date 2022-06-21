
package com.optum.ds.repository;

import com.optum.ds.entity.MailServiceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailServiceEntityRepository extends MongoRepository<MailServiceEntity, String> {
    long countByTinAndType(String tin, String Type);
}
