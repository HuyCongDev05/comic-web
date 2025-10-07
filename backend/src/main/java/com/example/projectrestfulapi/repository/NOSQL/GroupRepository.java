package com.example.projectrestfulapi.repository.NOSQL;

import com.example.projectrestfulapi.domain.NOSQL.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
}
