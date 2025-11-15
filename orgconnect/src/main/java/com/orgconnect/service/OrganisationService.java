package com.orgconnect.service;

import com.orgconnect.common.InvalidException;
import com.orgconnect.enums.ErrorCode;
import com.orgconnect.model.Organisation;
import com.orgconnect.repository.OrganisationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.orgconnect.util.Utils.getCurrentISTTime;
import static org.hibernate.internal.util.StringHelper.isBlank;

@Service
public class OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    public Organisation createOrganisation(Map<String, Object> request) {
        String token = UUID.randomUUID().toString(); // Random token

        long id = sequenceGeneratorService.generateSequenceLong(Organisation.SEQUENCE_NAME);
        String orgId = "ORG-" + id;
        LocalDateTime timeUpdate = getCurrentISTTime();
        Organisation org = new Organisation();
        org.setId(id);
        org.setOrgId(orgId);
        org.setOrgName((String) request.get("orgName"));
        org.setToken(token);
        org.setPhoneNo((String) request.get("phoneNo"));
        org.setEmail((String) request.get("email"));
        org.setAddress((Map<String, Object>) request.get("address"));
        org.setCreatedDate(timeUpdate);
        org.setUpdatedDate(timeUpdate);

        return organisationRepository.save(org);
    }

    public Organisation updateOrganisation(String orgId, Map<String, Object> updatedData, Organisation existing) {
        // Update required fields only
        if (updatedData.get("orgName") != null) {
            existing.setOrgName((String) updatedData.get("orgName"));
        }
        if (updatedData.get("address") != null) {
            existing.setAddress((Map<String, Object>) updatedData.get("address"));
        }
        if (updatedData.get("phoneNo") != null) {
            existing.setPhoneNo((String) updatedData.get("phoneNo"));
        }
        if (updatedData.get("email") != null) {
            existing.setEmail((String) updatedData.get("email"));
        }

        existing.setUpdatedDate(getCurrentISTTime());

        return organisationRepository.save(existing);
    }

    public Page<Organisation> getOrganisations(String orgId, String orgName, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return organisationRepository.searchOrganisations(orgId, orgName, from, to, pageable);
    }

    public boolean isValidToken(String orgId, String token) {
        if (isBlank(orgId)) {
            throw new InvalidException(ErrorCode.ORG_ID_MISSING, ErrorCode.ORG_ID_MISSING.message);
        }
        if (isBlank(token)) {
            throw new InvalidException(ErrorCode.MISSING_TOKEN, ErrorCode.MISSING_TOKEN.message);
        }
        // Here simple check in DB. You can replace with JWT validation logic.
        return organisationRepository.existsByOrgIdAndToken(orgId, token);
    }

    public void validateAuth(HttpServletRequest req) {
        if (!isValidToken(req.getHeader("ORG-ID"), req.getHeader("Authorization"))) {
            throw new InvalidException(ErrorCode.UNAUTHORIZED_ACCESS, ErrorCode.UNAUTHORIZED_ACCESS.message);
        }
    }

}

