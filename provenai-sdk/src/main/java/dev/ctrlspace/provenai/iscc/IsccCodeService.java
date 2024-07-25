package dev.ctrlspace.provenai.iscc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;



/**
 * This service is used to generate a ISCC code for a document
 * ISCC code an identification system for digital assets
 * (including encodings of text, images, audio, video or other content across all media-sectors)
 *
 * @see <a href="https://iscc.codes/">ISCC Documentation</a>
 * @see <a href="https://www.iso.org/standard/77899.html">ISCC ISO Page</a>
 */

public class IsccCodeService implements IsccCodeGenerator {

    private IsccCodeGeneratorApi isccCodeGeneratorApi;

    /**
     * Constructor
     * @param isccCodeGeneratorApi the ISCC code generator API url
     */
    public IsccCodeService(IsccCodeGeneratorApi isccCodeGeneratorApi) {
        this.isccCodeGeneratorApi = isccCodeGeneratorApi;
    }

    public IsccCodeResponse getDocumentIsccCode(MultipartFile file, String originalDocumentName) throws IOException {
        return this.getDocumentUniqueIdentifier(file.getBytes(), originalDocumentName);
    }

    /**
     *
     * @param fileBytes
     * @param originalDocumentName
     * @return
     */
    public IsccCodeResponse getDocumentUniqueIdentifier(byte[] fileBytes, String originalDocumentName) {
        return isccCodeGeneratorApi.getDocumentUniqueIdentifier(fileBytes, originalDocumentName);
    }

    public IsccCodeResponse toUniqueIdentifierCodeResponse(IsccCodeResponse isccCodeResponse) {
        return IsccCodeResponse.builder()
                .context(isccCodeResponse.getContext())
                .type(isccCodeResponse.getType())
                .schema(isccCodeResponse.getSchema())
                .iscc(isccCodeResponse.getIscc())
                .name(isccCodeResponse.getName())
                .mediaId(isccCodeResponse.getMediaId())
                .content(isccCodeResponse.getContent())
                .mode(isccCodeResponse.getMode())
                .filename(isccCodeResponse.getFilename())
                .filesize(isccCodeResponse.getFilesize())
                .mediatype(isccCodeResponse.getMediatype())
                .characters(isccCodeResponse.getCharacters())
                .metahash(isccCodeResponse.getMetahash())
                .datahash(isccCodeResponse.getDatahash())
                .build();
    }
}

