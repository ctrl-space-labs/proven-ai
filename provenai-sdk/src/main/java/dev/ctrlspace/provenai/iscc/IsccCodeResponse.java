package dev.ctrlspace.provenai.iscc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to represent the 'isccCodeResponse' part of the JSON
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class IsccCodeResponse {

    private String context;
    private String type;
    private String schema;
    private String iscc;
    private String name;
    private String mediaId;
    private String content;
    private String mode;
    private String filename;
    private int filesize;
    private String mediatype;
    private int characters;
    private String metahash;
    private String datahash;
    private String uuid;


}
