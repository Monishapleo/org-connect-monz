package com.user.service;

import com.user.common.InvalidException;
import com.user.config.JwtUtil;
import com.user.enums.ErrorCode;
import com.user.model.Organisation;
import com.user.model.User;
import com.user.repository.OrganisationRepository;
import com.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.user.util.Utils.getCurrentISTTime;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganisationRepository organisationRepository;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(Map<String, Object> requestBody) {

        String orgId = requestBody.get("orgId").toString();

        // Check organisation exists
        Organisation org = organisationRepository.findByOrgId(orgId);
        if (org == null) {
            throw new InvalidException(ErrorCode.ORG_DOESNT_EXISTS,ErrorCode.ORG_DOESNT_EXISTS.message);
        }
        long id=sequenceGeneratorService.generateSequenceLong(User.SEQUENCE_NAME);
        String userId="USER-"+id;
        String email = (String) requestBody.get("email");

        if (userRepository.existsByEmail(email)) {
            throw new InvalidException(ErrorCode.EMAIL_ALREADY_EXISTS,ErrorCode.EMAIL_ALREADY_EXISTS.message);
        }

        User user = new User();

        user.setId(id);
        user.setUserId(userId);
        user.setName((String) requestBody.get("name"));
        user.setEmail((String) requestBody.get("email"));

        // Encode password
        String rawPassword = (String) requestBody.get("password");
        if (rawPassword != null) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }

        user.setOrgId((String) requestBody.get("orgId"));
        //user.setRole((String) requestBody.getOrDefault("role", "MEMBER")); // default role
        user.setTeams((List<String>) requestBody.get("teams"));
        user.setStatus((String) requestBody.getOrDefault("status", "OFFLINE"));

        // Set timestamps
        user.setCreatedAt(getCurrentISTTime());
        user.setUpdatedAt(getCurrentISTTime());

        return userRepository.save(user);
    }


    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public List<User> getUsersByOrg(String orgId) {
        return userRepository.findByOrgId(orgId);
    }

    public User updateUser(String id, Map<String, Object> updateRequest) {
        return userRepository.findById(id).map(user -> {

            if (updateRequest.containsKey("name") && updateRequest.get("name") != null) {
                user.setName(updateRequest.get("name").toString());
            }

           /* if (updateRequest.containsKey("role") && updateRequest.get("role") != null) {
                user.setRole(updateRequest.get("role").toString());
            }*/

            if (updateRequest.containsKey("teams") && updateRequest.get("teams") != null) {
                // Cast carefully
                Object teamsObj = updateRequest.get("teams");
                if (teamsObj instanceof List<?>) {
                    user.setTeams(((List<?>) teamsObj).stream()
                            .map(Object::toString)
                            .collect(Collectors.toList()));
                }
            }

            // Optional: update password
            if (updateRequest.containsKey("password") && updateRequest.get("password") != null) {
                String rawPassword = updateRequest.get("password").toString();
                user.setPassword(passwordEncoder.encode(rawPassword));
            }

            user.setUpdatedAt(getCurrentISTTime());
            return userRepository.save(user);
        }).orElse(null);
    }


    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    // Extract userId from token and validate org
    public String getUserIdFromToken(String token, String orgId) {
        Claims claims = jwtUtil.validateToken(token); // decode JWT
        String userId = claims.getSubject();
        String tokenOrgId = claims.get("orgId", String.class);

        if (!orgId.equals(tokenOrgId)) return null;

        User user = userRepository.findByOrgIdAndUserId(orgId, userId);
        return (user != null) ? userId : null;
    }

    public boolean validateUserLogin(String orgId, String userId, String password) {

        User user = userRepository.findByOrgIdAndUserId(orgId, userId);

        if (user == null) return false;

        return passwordEncoder.matches(password, user.getPassword());
    }
}
