package com.user.repository;

import com.user.model.Organisation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrganisationRepository extends MongoRepository<Organisation, Long> {
    Organisation findByOrgId(String orgId);
    @Query("""
        {
          $and: [
            ?#{ [0] == null ? { "$expr": true } : { 'orgId': [0] } },
            ?#{ [1] == null ? { "$expr": true } : { 'orgName': { $regex: [1], $options: 'i' } } },
            ?#{ [2] == null ? { "$expr": true } : { 'createdDate': { $gte: [2] } } },
            ?#{ [3] == null ? { "$expr": true } : { 'createdDate': { $lte: [3] } } }
          ]
        }
        """)
    Page<Organisation> searchOrganisations(
            String orgId,
            String orgName,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Pageable pageable
    );

    boolean existsByOrgIdAndToken(String orgId, String token);

}

