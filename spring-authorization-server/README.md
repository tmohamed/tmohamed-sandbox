Testing spring-authorization-server
- Exploring authorization server exposed endpoints
    - curl -X GET http://localhost:8080/.well-known/openid-configuration
- Authorization request
  - http://localhost:8080/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=https://www.manning.com/authorized (hit this URL in the browser)
  - Then application will forward the user to login page
  - After successful login, authorization server will send the authorization code to redirect_uri specified
    - https://www.manning.com/authorized?code=KxKP1504A8GQNb0HRvN4bPMIpgc9cuaYfe-Y-0qs2rpb7gNQOo-g1Ex0HvG0q7DD6lwZtsM0DL__2Cs5HkoshIm13X3E-ThdS6wrbIP1tsGYw1hsNbbqpuza4No1cYE8
  - Requesting access token
    - curl -X POST http://localhost:8080/oauth2/token?client_id=client&redirect_uri=https://www.manning.com/authorized&grant_type=authorization_code&code=KxKP1504A8GQNb0HRvN4bPMIpgc9cuaYfe-Y-0qs2rpb7gNQOo-g1Ex0HvG0q7DD6lwZtsM0DL__2Cs5HkoshIm13X3E-ThdS6wrbIP1tsGYw1hsNbbqpuza4No1cYE8 \ --header 'Authorization: Basic Y2xpZW50OnNlY3JldA=='
