package com.orgconnect.controller;

import com.orgconnect.common.APIResponse;
import com.orgconnect.enums.ErrorCode;
import com.orgconnect.enums.StatusCode;
import com.orgconnect.model.Organisation;
import com.orgconnect.service.OrganisationService;
import com.orgconnect.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.orgconnect.util.Utils.showError;
import static com.orgconnect.util.Utils.showSuccessObj;

@RestController
@RequestMapping("/api/org")
public class OrganisationController {

    @Autowired
    private OrganisationService organisationService;
    @Autowired
    Utils utils;

    @PostMapping("/create-org")
    public ResponseEntity<?> createOrganisation(@RequestParam String orgName) {
        if(orgName == null || orgName.trim().isEmpty()) {
            return showError(null,ErrorCode.ORG_NAME_EMPTY.message, StatusCode.BADREQUEST.code, ErrorCode.ORG_NAME_EMPTY);
        }

        Organisation org = organisationService.createOrganisation(orgName);
        return showSuccessObj(org,"Organisation created successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<?> listOrganisations(
            @RequestParam(required = false) String orgId,
            @RequestParam(required = false) String orgName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {

        LocalDateTime from = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime to = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        // Pageable object
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        // Fetch data using repository/service
        Page<Organisation> orgPage = organisationService.getOrganisations(orgId, orgName, from, to, pageable);

        if (orgPage.isEmpty()) {
            return Utils.showError(null,
                    ErrorCode.NO_DATA_FOUND.message,
                    StatusCode.SUCCESS.code,
                    ErrorCode.NO_DATA_FOUND);
        }

        // Build enriched response if needed, here just returning org data
        List<Map<String, Object>> enrichedList = orgPage.getContent().stream().map(org -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("orgId", org.getOrgId());
            map.put("orgName", org.getOrgName());
            map.put("token", org.getToken());
            map.put("createdDate", org.getCreatedDate());
            map.put("updatedDate", org.getUpdatedDate());
            return map;
        }).toList();

        APIResponse response = new APIResponse();
        response.setCode(StatusCode.SUCCESS.code);
        response.setStatus(true);
        response.setMsg("Organisations fetched successfully");
        response.setData(enrichedList);
        response.setTotalPages(orgPage.getTotalPages());
        response.setTotalElements(orgPage.getTotalElements());
        response.setPageNumber(orgPage.getNumber());
        response.setPageData(orgPage.getNumberOfElements());

        return ResponseEntity.ok(response);
    }

}

