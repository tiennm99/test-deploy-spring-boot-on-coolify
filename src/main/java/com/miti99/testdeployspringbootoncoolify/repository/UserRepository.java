package com.miti99.testdeployspringbootoncoolify.repository;

import com.miti99.testdeployspringbootoncoolify.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
