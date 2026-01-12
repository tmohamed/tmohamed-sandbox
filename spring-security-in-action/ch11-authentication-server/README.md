# ch11-authentication-server

Simple authentication server built with Java and Spring Boot (example code from *Spring Security in Action* chapter 11). Provides user registration, password authentication and one-time-password (OTP) flows.

Technologies
- Java, Spring Boot
- Maven
- SQL (embedded or configured datasource)
- Minimal REST API for auth operations

Getting started
1. Build:

mvn clean package

2. Run:

mvn spring-boot:run
# or
java -jar target/ch11-authentication-server-*.jar

3. Default base URL:

http://localhost:8080


APIs

Base path used in examples: `http://localhost:8080`

1. Register user
- Endpoint: `POST /user/add`
- Request JSON:
json
{
  "username": "alice",
  "password": "P@ssw0rd"
}

- Response: `200 OK` (user info or id)

cURL:

curl -X POST http://localhost:8080/user/add \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"P@ssw0rd"'


2. Password login
- Endpoint: `POST /user/auth`
- Request JSON:
json
{
  "username": "alice",
  "password": "P@ssw0rd"
}

- Response: `200 OK` with OTP generated and saved into the DB

cURL:

curl -X POST http://localhost:8080/user/auth \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"P@ssw0rd"}'


3. Verify OTP
- Endpoint: `POST /otp/check`
- Request JSON:
json
{
  "username": "alice",
  "code": "1234"
}

- Response: `200 OK`

cURL:

curl -X POST http://localhost:8080//otp/check \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","code": "1234"}'
