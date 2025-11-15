package com.user.repository;

import com.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByOrgId(String orgId);

    User findByOrgIdAndUserId(String orgId, String user_id);

    boolean existsByEmail(String email);

    boolean existsByOrgIdAndUserId(String orgId, String user_id);

}
