package dev.ctrlspace.provenai.backend.adapters;


import dev.ctrlspace.provenai.backend.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GendoxQueryAdapter {

    private RestTemplate restTemplate;

    private String domain;
    private String contextPath;
    private String semanticSearchPath;
    private String completionPath;

    private AuthenticationService authenticationService;


    @Autowired
    public GendoxQueryAdapter(RestTemplate restTemplate,
                              AuthenticationService authenticationService,
                                @Value("${proven-ai.domains.gendox.base-url}") String domain,
                                @Value("${proven-ai.domains.gendox.context-path}") String contextPath,
                                @Value("${proven-ai.domains.gendox.apis.semantic-search}") String semanticSearchPath,
                                @Value("${proven-ai.domains.gendox.apis.completion}") String completionPath ){
        this.restTemplate = restTemplate;
        this.authenticationService = authenticationService;
        this.domain = domain;
        this.contextPath = contextPath;
        this.semanticSearchPath = semanticSearchPath;
        this.completionPath = completionPath;
    }

    /**
     * This method perform semantic search to Gendox with the Keycloak Client user, which is the GENDOX_SUPER_ADMIN user.
     * @param question
     * @param projectId
     * @param size
     * @return
     */
    public String superAdminSearch(String question, String projectId, String size) {

        String adminJwt = authenticationService.getClientTokenString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + adminJwt);

        MessageRequest message = new MessageRequest(question);

        HttpEntity<MessageRequest> entity = new HttpEntity<>(message, headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(domain + contextPath + semanticSearchPath)
                .queryParam("projectId", projectId)
                .queryParam("size", size);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class);

        return responseEntity.getBody();
    }

    public String superAdminCompletion(String question) {

        return "responseEntity.getBody()";
    }


    record MessageRequest(String value) {
    }


}
