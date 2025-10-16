## Chapter 18: Testing Security Configurations
### 18.1 Using Mock Users for Tests
Mock users skip authentication to focus on authorization, using @WithMockUser to simulate users with roles/authorities. Tests use MockMvc to verify endpoint access (e.g., 401 Unauthorized without auth, 200 OK with). This isolates authorization testing from authentication, ideal for regression checks during framework upgrades.

```java
// Listing 18.3: Testing that you can’t call the endpoint without an authenticated user
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {
  @Autowired
  private MockMvc mvc;

  @Test
  public void testfindById() throws Exception {
    mvc.perform(get("/"))
      .andExpect(status().isUnauthorized());
  }
}

// Listing 18.4: Using @WithMockUser to define a mock authenticated user
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {
  @Autowired
  private MockMvc mvc;

  @Test
  @WithMockUser
  public void testHelloAuth() throws Exception {
    mvc.perform(get("/hello"))
      .andExpect(status().isOk());
  }
}
```

### 18.2 Testing with Users from a UserDetailsService
Uses @WithUserDetails to load real users from UserDetailsService for more realistic authorization tests, integrating with data sources like databases. Verifies access based on actual user details, ensuring storage-agnostic auth works.

```java
// Listing 18.7: Defining the authenticated user with the @WithUserDetails annotation
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {
  @Autowired
  private MockMvc mvc;

  @Test
  @WithUserDetails("john")
  public void testHelloAuthenticated() throws Exception {
    mvc.perform(get("/hello"))
      .andExpect(status().isOk());
  }
}
```

### 18.3 Using Custom Authentication Objects for Testing
@WithSecurityContext allows full control over the security context with custom Authentication objects via a factory. Useful for testing specific auth implementations or complex scenarios.

```java
// Listing 18.8: Defining the @WithCustomUser annotation
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomSecurityContextFactory.class)
public @interface WithCustomUser {
  String username() default "john";
  String password() default "12345";
  String[] authorities() default {"read"};
}

// Listing 18.9: The implementation of a factory for the SecurityContext
public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {
  @Override
  public SecurityContext createSecurityContext(WithCustomUser annotation) {
    var auth = new UsernamePasswordAuthenticationToken(
      annotation.username(), annotation.password(),
      AuthorityUtils.createAuthorityList(annotation.authorities()));
    var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);
    return context;
  }
}

// Listing 18.11: Writing a test that uses the custom SecurityContext
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {
  @Autowired
  private MockMvc mvc;

  @Test
  @WithCustomUser
  public void testHelloAuthenticated() throws Exception {
    mvc.perform(get("/hello"))
      .andExpect(status().isOk());
  }
}
```

### 18.4 Testing Method Security
Tests method-level security (@PreAuthorize, etc.) using mock users or contexts to verify access control. Asserts AccessDeniedException for unauthorized calls and success for authorized ones.

```java
// Listing 18.12: Implementation of the three test scenarios for the getName() method
@SpringBootTest
class MainTests {
  @Autowired
  private NameService nameService;

  @Test
  void testNameServiceNoAuth() {
    assertThrows(AccessDeniedException.class, nameService::getName);
  }

  @Test
  @WithMockUser
  void testNameServiceAuthNoAuthority() {
    assertThrows(AccessDeniedException.class, nameService::getName);
  }

  @Test
  @WithMockUser(authorities = "write")
  void testNameServiceAuthWithAuthority() {
    var result = nameService.getName();
    assertEquals("Fantastico", result);
  }
}
```

### 18.5 Testing Authentication
Simulates full authentication flows (HTTP Basic, form login) using MockMvc's httpBasic() or formLogin() to verify success/failure, redirects, and custom handlers.

```java
// Listing 18.13: Testing authentication with httpBasic() RequestPostProcessor
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {
  @Autowired
  private MockMvc mvc;

  @Test
  public void testHelloAuth() throws Exception {
    mvc.perform(get("/hello").with(httpBasic("john", "12345")))
      .andExpect(header().string("X-Authority", "read"))
      .andExpect(status().isOk());
  }
}

// Listing 18.14: Testing form login-failed authentication
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {
  @Autowired
  private MockMvc mvc;

  @Test
  public void testBadCredentials() throws Exception {
    mvc.perform(formLogin().user("john").password("doe"))
      .andExpect(status().isFound())
      .andExpect(redirectedUrl("/login?error"));
  }
}
```

### 18.6 Testing CSRF Configurations
Tests CSRF protection by including/excluding tokens in requests, verifying rejection of invalid POSTs and acceptance of valid ones.

```java
// Listing 18.18: Implementing the CSRF protection test scenarios
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {
  @Autowired
  private MockMvc mvc;

  @Test
  public void testHelloPostWithCSRF() throws Exception {
    mvc.perform(post("/hello").with(csrf()))
      .andExpect(status().isOk());
  }

  @Test
  public void testHelloPostNoCSRF() throws Exception {
    mvc.perform(post("/hello"))
      .andExpect(status().isForbidden());
  }
}
```

### 18.7 Testing CORS Configurations
Verifies CORS policies by sending requests with origin headers and asserting allowed methods/origins in responses.

```java
// Listing 18.19: Test implementation for CORS policies
@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {
  @Autowired
  private MockMvc mvc;

  @Test
  public void testCorsForTestPathPost() throws Exception {
    mvc.perform(post("/test").header("Access-Control-Request-Method", "POST")
      .header("Origin", "http://localhost:8080"))
      .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:8080"))
      .andExpect(header().string("Access-Control-Allow-Methods", "POST"))
      .andExpect(status().isOk());
  }
}
```

### 18.8 Testing Reactive Spring Security Implementations
Uses WebTestClient for reactive apps, applying mock users or contexts to test authorization, authentication, and protections like CSRF.

```java
// Listing 18.20: Using the @WithMockUser when testing reactive implementations
@SpringBootTest
@AutoConfigureWebTestClient
public class MainTests {
  @Autowired
  private WebTestClient webTestClient;

  @Test
  @WithMockUser
  public void testHelloAuth() {
    webTestClient.get().uri("/hello")
      .exchange()
      .expectStatus().isOk();
  }
}

// Listing 18.21: Using a WebTestClientConfigurer to define a mock user
@SpringBootTest
@AutoConfigureWebTestClient
public class MainTests {
  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void testHelloAuth() {
    webTestClient.mutateWith(mockUser().authorities(new SimpleGrantedAuthority("read")))
      .get().uri("/hello")
      .exchange()
      .expectStatus().isOk();
  }
}
```

**Summary Note**: Testing integrates app code with Spring Security for endpoints, methods, and reactive flows. Use mocks to skip auth for authorization tests, simulate full auth for login flows, and verify protections like CSRF/CORS. Ensures upgrades don't break security, focusing on isolation and realism.