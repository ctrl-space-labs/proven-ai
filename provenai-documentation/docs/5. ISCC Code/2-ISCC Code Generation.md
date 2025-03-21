# ISCC Code Generation

## ISCC Generation API

The `IsccCodeGeneratorApi` class is responsible for interacting with the ISCC code generation API. It provides methods to build request headers, encode file names, and obtain ISCC codes for documents.

### Endpoints

#### `POST /iscc/code`

Generates an ISCC code for a document.

#### Request Headers

- `X-Upload-Filename`: The base64-encoded file name.
- **Parameters**:
  - `base64EncodedFileName`: The base64-encoded original file name of the document.

#### Request Body

- file in binary format.

#### Response

- `200 OK`: Successfully generated the ISCC code.
- `IsccCodeResponse`: Contains the ISCC code and related information.

The `IsccCodeResponse` class represents the response structure returned from the ISCC code generation API.

#### Fields

- `context` (String): The context of the ISCC code schema.
- `type` (String): The type of the digital document, specified by `@type` in the ISCC JSON-LD context.
- `schema` (String): The schema version used for the ISCC generation, specified by `$schema` in the ISCC JSON.
- `iscc` (String): The generated ISCC code.
- `name` (String): The name or title associated with the document.
- `mediaId` (String): The unique identifier of the media associated with the ISCC.
- `content` (String): The URL pointing to the content associated with the ISCC.
- `mode` (String): The mode or type of the content.
- `filename` (String): The original filename of the document.
- `filesize` (int): The size of the file in bytes.
- `mediatype` (String): The media type of the content (e.g., text/plain).
- `characters` (int): The number of characters in the content.
- `metahash` (String): The hash value representing metadata of content.
- `datahash` (String): The hash value representing the actual data content.

#### Example IsccCodeResponse

```json
{
    "@context": "http://purl.org/iscc/context/0.4.0.jsonld",
    "@type": "TextDigitalDocument",
    "$schema": "http://purl.org/iscc/schema/0.4.0.json",
    "iscc": "ISCC:KAC764GBOKHLKVPGXQHFQGL3B2LJVQ4FAATH5CDKEV4PCZXTK2PCTQI",
    "name": "a random txt",
    "media_id": "068a2u76e5su6",
    "content": "http://localhost:8970/api/v1/media/068a2u76e5su6",
    "mode": "text",
    "filename": "a_random_txt",
    "filesize": 87,
    "mediatype": "text/plain",
    "characters": 61,
    "metahash": "1e2012faa0addaebca2d7e5aab55fb790de263151eb2682cd5072ff05823c7342089",
    "datahash": "1e2078f166f3569e29c1af1573842be4d2fa7e9d0a7e280dbd1e64f9c66837d5225f"
}
```

#### Example Request

```bash
curl -X POST "https://iscc.io/api/v1/iscc" \
-H "X-Upload-Filename: ZG9jdW1lbnQudHh0" \
--data-binary "@/path/to/document.txt"
```

## IsccCodeServiceClass
The `IsccCodeService` class provides functionality to interact with the ISCC code generation API for digital assets.

### Dependencies

- `IsccCodeGeneratorApi`: An API client used to communicate with the ISCC code generation API.

### Methods

#### `getDocumentIsccCode`

Generates an ISCC code for a document represented by a `MultipartFile`.

#### Parameters

- `file` (MultipartFile): The file to generate the ISCC code for.
- `originalDocumentName` (String): The original name of the document.

#### Returns

- `IsccCodeResponse`: The response containing the generated ISCC code and related information.
