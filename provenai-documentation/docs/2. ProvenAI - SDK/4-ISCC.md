## ISCC Code Gneration
### Components

#### 1. **IsccCodeGenerator Interface**
- **Purpose**: Defines a method to generate an ISCC code for a document.
- **Method**:
  - `getDocumentUniqueIdentifier(byte[] fileBytes, String originalDocumentName)`: Generates an ISCC code for the provided document.
    - **Parameters**:
      - `fileBytes`: The byte array of the document.
      - `originalDocumentName`: The name of the document.
    - **Returns**: An instance of `IsccCodeResponse`.

#### 2. IsccCodeGeneratorApi Class Documentation

The `IsccCodeGeneratorApi` class is responsible for interacting with the ISCC code generation API. It manages API calls for generating ISCC codes based on uploaded documents. For more information about the ISCC code generation API and the response returned see 

#### 4. **IsccCodeService Class**
Implements the `IsccCodeGenerator` interface to generate ISCC codes.
- **Methods**:
  - `getDocumentIsccCode(MultipartFile file, String originalDocumentName)`: Retrieves the ISCC code for a MultipartFile.
  - `getDocumentUniqueIdentifier(byte[] fileBytes, String originalDocumentName)`: Calls the API to generate the ISCC code.
