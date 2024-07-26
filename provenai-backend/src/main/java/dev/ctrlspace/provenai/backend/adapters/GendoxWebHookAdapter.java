package dev.ctrlspace.provenai.backend.adapters;

import dev.ctrlspace.provenai.backend.authentication.AuthenticationService;
import dev.ctrlspace.provenai.backend.model.dtos.DocumentInstanceSectionDTO;
import dev.ctrlspace.provenai.backend.model.dtos.EventPayloadDTO;
import dev.ctrlspace.provenai.backend.model.dtos.WebHookEventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class GendoxWebHookAdapter {
    private RestTemplate restTemplate;

    private String domain;
    private String contextPath;

    private String weHookPath;

    private AuthenticationService authenticationService;

    @Autowired
    public GendoxWebHookAdapter(RestTemplate restTemplate,
                              AuthenticationService authenticationService,
                              @Value("${proven-ai.domains.gendox.base-url}") String domain,
                              @Value("${proven-ai.domains.gendox.context-path}") String contextPath,
                              @Value("${proven-ai.domains.gendox.apis.web-hook}") String weHookPath){
        this.restTemplate = restTemplate;
        this.authenticationService = authenticationService;
        this.domain = domain;
        this.contextPath = contextPath;
        this.weHookPath = weHookPath;
    }

    public ResponseEntity<WebHookEventResponse> gendoxWebHookEvent(String messageType, EventPayloadDTO eventPayload) {
        String adminJwt = authenticationService.getClientTokenString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + adminJwt);

        HttpEntity<EventPayloadDTO> requestEntity = new HttpEntity<>(eventPayload, headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(domain + contextPath + weHookPath)
                .queryParam("messageType", messageType);

        ParameterizedTypeReference<WebHookEventResponse> typeRef = new ParameterizedTypeReference<WebHookEventResponse>() {};

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                requestEntity,
                typeRef);
    }

}
