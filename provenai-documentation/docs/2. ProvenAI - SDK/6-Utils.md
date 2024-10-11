## Utility Classes
The provenAI SDK provides several utility classes to assist with various common tasks, such as handling continuations, serialization, date conversions, and JWT operations. Below is a brief summary of the main utility classes and their functionality:

### ContinuationObjectUtils
A utility class for creating Continuation objects with various configurations:
- `createSuperContinuation()`: Creates a `Continuation` with a wildcard super type.
- `createPlainContinuation()`: Creates a plain `Continuation` without a type parameter.

### JsonLiteralSerializer
A custom serializer for JsonLiteral objects, used to serialize JSON literals into proper string, boolean, or numeric values. Handles exceptions for invalid formats.

### KotlinToJavaUtils
Utility class for converting between Kotlin and Java types:
- Convert between `Instant` objects in Java and Kotlin's `kotlinx.datetime.Instant`.
- Convert between `Kotlin` and Gson JSON elements (`JsonElement` and `JsonPrimitive`).

### SSIJWTUtils
A utility for extracting Verifiable Credential (VC) tokens from Verifiable Presentation (VP) JWTs. It decodes the VP JWT payload and extracts the VC JWT.

### WaltIdServiceInitUtils 
This singleton enum class initializes WaltId services using the minimalInitBlocking() method. It uses a plain continuation created from `ContinuationObjectUtils`.