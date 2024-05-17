package dev.ctrlspace.provenai.iscc;



public interface IsccCodeGenerator {


    /**
     * This method is used to generate a ISCC code for a document
     * ISCC code an identification system for digital assets
     * (including encodings of text, images, audio, video or other content across all media-sectors)
     *
     * @see <a href="https://iscc.codes/">ISCC Website</a>
     * @see <a href="https://www.iso.org/standard/77899.html">ISCC ISO Page</a>
     * @param fileBytes The file bytes of the document to get the ISCC code
     * @param originalDocumentName The original name of the document
     * @return The ISCC code response
     */
    IsccCodeResponse getDocumentUniqueIdentifier(byte[] fileBytes, String originalDocumentName);



}