- Testing demo application using non-opaque tokens (JWTs)
  curl --location 'localhost:9090/demo' \
  --header 'X-TenantID: tenant1' \
  --header 'Authorization: Bearer eyJraWQiOiIyN2Q0YWNlYi04OTZiLTQ5OTktYWE5NC1kMTZiNDY4N2RhZTYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJjbGllbnQiLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE3NTc5NTIxMjEsInNjb3BlIjpbIkNVU1RPTSJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjcwNzAiLCJleHAiOjE3NTc5NTI0MjEsImlhdCI6MTc1Nzk1MjEyMSwianRpIjoiZmMxYzI2Y2UtMzIyNy00OTlmLTgyOTMtZjliMzBlZTkyZDQyIn0.Q0SchRIUVCIpJHcfzp72V_4uScC8sFr0p0vuagF97_oshjNfX3P2zbzYD0V_TFLeEBIqZBHY6w9jA5mIyo8M3IqU7dr8TNMQlsSyyDlxvSG9UdBwVLRn6fPS9P8gFAXQufv2bREDymf3j7iUVpoQIfDbsvOduS2degMx67mKaK-5k3bxfbIeaNN01rS5DDsgMGFD0VAIklEtxRwEbRKh0t3u4UG1Fo0TfIvzNqcd2ALpdsJDVbT4j3ry2RHRujz2DKNLyuW8xkuEEb-0YyS6B7ZzMOT4j-cQ4v7pKziMAWbpDKJxQJv0xjlcrncc90bWPkHX1D_vqnZgZ2wyYpfKmQ' \
  --header 'Cookie: JSESSIONID=BF3A8F31968462C4EC9D7094C10DF9F7'

- Testing demo application using Opaque tokens
  curl --location 'localhost:9090/demo' \
  --header 'X-TenantID: tenant2' \
  --header 'Authorization: Bearer 9WqXrcLrST30M-5jyXWvHgQgl29fSIx5owHCldQbQCBI8hY0aPdSTRwmlapFg74trmLkveOE3Eyh8MCeuciUIFc5dBBvqpEi8or2XVWP8x4yxo7udx08e07jzKGqlpM4' \
  --header 'Cookie: JSESSIONID=BF3A8F31968462C4EC9D7094C10DF9F7'