package dev.ctrlspace.provenai.backend.adapters;

import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GendoxProfileAdapter {

    private RestTemplate restTemplate;

    private String domain;
    private String contextPath;
    private String userProfilePath;


    @Autowired
    public GendoxProfileAdapter(RestTemplate restTemplate,
                                @Value("${proven-ai.domains.gendox.base-url}") String domain,
                                @Value("${proven-ai.domains.gendox.context-path}") String contextPath,
                                @Value("${proven-ai.domains.gendox.apis.user-profile}") String userProfilePath) {
        this.restTemplate = restTemplate;
        this.domain = domain;
        this.contextPath = contextPath;
        this.userProfilePath = userProfilePath;
    }


    public UserProfile getUserProfile(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + jwt);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserProfile> responseEntity = restTemplate.exchange(
                domain + contextPath + userProfilePath,
                HttpMethod.GET,
                entity,
                UserProfile.class);

        return responseEntity.getBody();
    }
}
