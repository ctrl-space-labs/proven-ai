package dev.ctrlspace.provenai.iscc;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;



public class IsccCodeGeneratorApi {
    public static final RestTemplate restTemplate = new RestTemplate();

    private String isccCodeGenerationApi;

    public IsccCodeGeneratorApi(String isccCodeGenerationApi) {
        this.isccCodeGenerationApi = isccCodeGenerationApi;
    }

    public HttpHeaders buildHeader(String base64EncodedFileName) {
        HttpHeaders headers = new HttpHeaders();
        //header for X-Upload-Filename
        headers.add("X-Upload-Filename", base64EncodedFileName);
        return headers;
    }

    private String encodeFileName(String originalFileName) {
        return Base64.getEncoder().encodeToString(originalFileName.getBytes(StandardCharsets.UTF_8));
    }



    /**
     *
     * @param fileBytes
     * @param originalDocumentName
     * @return
     */
    public IsccCodeResponse getDocumentUniqueIdentifier(byte[] fileBytes, String originalDocumentName) {
        String base64EncodedDocumentName = encodeFileName(originalDocumentName);

        ResponseEntity<IsccCodeResponse> responseEntity = restTemplate.postForEntity(
                isccCodeGenerationApi,
                new HttpEntity<>(fileBytes, buildHeader(base64EncodedDocumentName)),
                IsccCodeResponse.class);


        return responseEntity.getBody();
    }


}
