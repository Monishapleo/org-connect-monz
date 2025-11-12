package com.orgconnect.service;

import com.orgconnect.model.Organisation;
import com.orgconnect.repository.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.orgconnect.util.Utils.getCurrentISTTime;

@Service
public class OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    public Organisation createOrganisation(String orgName) {
        String token = UUID.randomUUID().toString(); // Random token

        Long id= sequenceGeneratorService.generateSequenceLong(Organisation.SEQUENCE_NAME);
        String orgId = "ORG-" + id;
        LocalDateTime timeUpdate=getCurrentISTTime();
        Organisation org = new Organisation(id,orgId, orgName, token,timeUpdate,timeUpdate);
        return organisationRepository.save(org);
    }
    public Page<Organisation> getOrganisations(String orgId, String orgName, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return organisationRepository.searchOrganisations(orgId, orgName, from, to, pageable);
    }
    public boolean isValidToken(String token) {
        // Here simple check in DB. You can replace with JWT validation logic.
        return organisationRepository.existsByToken(token);
    }
}

