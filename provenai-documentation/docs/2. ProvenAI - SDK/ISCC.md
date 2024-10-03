## ISCC Package
### Main Components

#### 1. **IsccCodeGenerator Interface**
- **Purpose**: Defines a method to generate an ISCC code for a document.
- **Method**:
  - `getDocumentUniqueIdentifier(byte[] fileBytes, String originalDocumentName)`: Generates an ISCC code for the provided document.
    - **Parameters**:
      - `fileBytes`: The byte array of the document.
      - `originalDocumentName`: The name of the document.
    - **Returns**: An instance of `IsccCodeResponse`.

#### 2. IsccCodeGeneratorApi Class Documentation

The `IsccCodeGeneratorApi` class is responsible for interacting with the ISCC code generation API. It manages API calls for generating ISCC codes based on uploaded documents.

#### Attributes
- **restTemplate**: A static final instance of `RestTemplate` used for making HTTP requests.
- **isccCodeGenerationApi**: A string representing the base URL of the ISCC code generation API.

#### Methods

**buildHeader**
- **Signature**: `public HttpHeaders buildHeader(String base64EncodedFileName)`
- **Purpose**: Constructs HTTP headers for the API request.
- **Parameters**:
  - `base64EncodedFileName`: The base64-encoded original file name of the document.
- **Returns**: An instance of `HttpHeaders` containing the necessary headers, including:
  - `X-Upload-Filename`: The encoded file name for the document.

**encodeFileName**
- **Signature**: `private String encodeFileName(String originalFileName)`
- **Purpose**: Encodes the original file name into a base64 format for secure transmission.
- **Parameters**:
  - `originalFileName`: The original name of the document to be encoded.
- **Returns**: A base64-encoded string representation of the original file name.


#### 3. **IsccCodeResponse Class**
 Represents the response received from the ISCC code generation service.
- **Attributes**:
  - `context`, `type`, `schema`, `iscc`, `name`, `mediaId`, `content`, `mode`, `filename`, `filesize`, `mediatype`, `characters`, `metahash`, `datahash`, `uuid`.

#### 4. **IsccCodeService Class**
Implements the `IsccCodeGenerator` interface to generate ISCC codes.
- **Methods**:
  - `getDocumentIsccCode(MultipartFile file, String originalDocumentName)`: Retrieves the ISCC code for a MultipartFile.
  - `getDocumentUniqueIdentifier(byte[] fileBytes, String originalDocumentName)`: Calls the API to generate the ISCC code.

### References
- [ISCC Documentation](https://iscc.codes/)
- [ISCC ISO Page](https://www.iso.org/standard/77899.html)