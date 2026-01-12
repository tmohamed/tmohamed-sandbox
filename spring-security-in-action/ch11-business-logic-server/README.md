# ch11-business-logic-server

Simple business-logic microservice that delegates authentication to a separate authentication server. Implements request filters and JWT validation to protect endpoints and uses `AuthenticationServerProxy` to call the authentication server.

Technologies
- Java, Spring Boot
- Maven
- Spring Security (custom filters & authentication providers)
- REST (RestTemplate)

Quickstart
1. Build:
bash
mvn clean package

2. Run:
bash
mvn spring-boot:run
# or
java -jar target/ch11-business-logic-server-*.jar

3. Configuration:
Edit `src/main/resources/application.properties` to set ports and the authentication server base URL used by `AuthenticationServerProxy`.

High level behavior
- Authentication requests are handled by a separate authentication server. This service calls that server via `AuthenticationServerProxy`.
- `InitialAuthenticationFilter` handles initial authentication flows (username/password, OTP) when proxied.
- `JwtAuthenticationFilter` validates JWTs for protected endpoints.
- See `controller/TestController` for an example protected endpoint.

APIs (examples)

1) Example: login (on the business logic server)
- URL: `POST http://localhost:9090/login`
- HTTP Header parameters:
  - `username`: `alice`
  - `password`: `P@ssw0rd`

- Response: `200 OK`

cURL:
bash
curl -s -X POST http://localhost:9090/login \
  -H "username: alice" \
  -H "password: P@ssw0rd"

2) Example: acquiring access token (on the business logic server)
- URL: `POST http://localhost:9090/login`
- HTTP Header parameters:
    - `username`: `alice`
    - `code`: `1234`

- Response: `200 OK`

cURL:
bash
curl -s -X POST http://localhost:9090/login \
-H "username: alice" \
-H "code: 1234"

3) Call a protected business endpoint on this service
- Example protected endpoint (see `controller/TestController`): `GET /api/test` (hosted by this service)
- Requires header: `Authorization: Bearer <token>`

cURL (using token from login):
bash
TOKEN="eyJhbGciOi..." # token from auth server login response
curl -X GET http://localhost:9090/test \
  -H "Authorization: Bearer $TOKEN"


Configuration & source pointers
- Edit `src/main/resources/application.properties` for server port and auth server URL.
- Proxy to the auth server is implemented in `authentication_server_proxy/AuthenticationServerProxy`.
- Filters are in `filters/InitialAuthenticationFilter` and `filters/JwtAuthenticationFilter`.
- Custom authentication providers are in `authentication_providers/`.