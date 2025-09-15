Testing spring-authorization-server with JWT access tokens
=======================================================
- Getting Access token using Client_Credentials grant type
  curl --location 'http://localhost:7070/oauth2/token' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --header 'Authorization: Basic Y2xpZW50OnNlY3JldA==' \
  --header 'Cookie: JSESSIONID=BF3A8F31968462C4EC9D7094C10DF9F7' \
  --data-urlencode 'grant_type=client_credentials' \
  --data-urlencode 'scope=CUSTOM' 