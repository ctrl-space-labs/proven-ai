# Search

This endpoint allows an authenticated agent to search for results based on a given question. The search is executed using the `searchService` and is customized based on the agent's profile.

## **HTTP Method:**
`POST`

## **URL:**
`/search`

## **Authorization:**
- **Authentication Required**:  
  The endpoint requires the user to be authenticated. The `Authentication` object provides the agent's profile information.

## **Request Body:**

- **`question` (String, Required)**:  
  The search query string provided by the user. It contains the question or phrase for which the search results are requested.

You can see the search API reference [here](https://dev.proven-ai.ctrlspace.dev/proven-ai/api/v1/swagger-ui/index.html#/search-controller).
