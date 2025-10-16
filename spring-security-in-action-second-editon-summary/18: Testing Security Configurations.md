## Chapter 18: Testing Security Configurations

### 18.1 Using Mock Users for Tests
@WithMockUser simulates users with predefined roles and credentials, allowing developers to test endpoints as if an authenticated user is present without requiring a full user management setup. This annotation is particularly useful for quick tests using MockMvc to verify access control, such as ensuring unauthorized requests return a 401 status. The process involves setting up a Spring Boot test with @AutoConfigureMockMvc to enable MockMvc, which then performs HTTP requests and validates responses.

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
```

### 18.2 Testing with Users from a UserDetailsService
@WithUserDetails leverages an existing UserDetailsService to load real user data for testing, providing a more realistic authentication scenario compared to mock users. This approach is ideal for validating security configurations against actual user records stored in memory, a database, or another backend. Using MockMvc, tests can confirm that authenticated users with specific credentials can access protected endpoints, returning a 200 OK status when successful.

```java
// Listing 18.5: Using @WithUserDetails for testing with a real user
@WithUserDetails("john")
@Test
public void testAuthenticatedUser() throws Exception {
    mvc.perform(get("/secure"))
        .andExpect(status().isOk());
}
```

### 18.3 Using Custom Authentication Objects for Testing
@WithSecurityContext, paired with a custom annotation @WithCustomUser, offers advanced testing capabilities by allowing developers to define custom authentication objects with specific usernames, passwords, and authorities. This method uses a factory class (CustomSecurityContextFactory) to create a SecurityContext with a tailored Authentication object, enabling precise control over the security context for testing complex scenarios. It’s particularly useful for testing custom authorization logic or multi-role access, with MockMvc verifying the expected outcomes.

```java
// Listing 18.7: Custom security context setup
@WithSecurityContext(factory = CustomSecurityContextFactory.class)
@Test
public void testCustomAuth() throws Exception {
    mvc.perform(get("/secure"))
        .andExpect(status().isOk());
}

static class CustomSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth = new UsernamePasswordAuthenticationToken(customUser.username(), customUser.password(), AuthorityUtils.createAuthorityList(customUser.authorities()));
        context.setAuthentication(auth);
        return context;
    }
}

// Listing 18.8: Definition of the @WithCustomUser annotation
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomSecurityContextFactory.class)
public @interface WithCustomUser {
    String username() default "customUser";
    String password() default "password";
    String[] authorities() default {"ROLE_USER"};
}
```

### 18.4 Testing Method Security
This section focuses on testing method-level security configurations, which are enabled using @EnableMethodSecurity and enforced through annotations like @PreAuthorize, @PostAuthorize, @PreFilter, and @PostFilter. Tests verify that methods annotated with these security constraints correctly enforce access control based on user roles, authorities, or custom conditions. Using Spring’s testing framework with @WithMockUser or @WithSecurityContext, developers can simulate authenticated users and test scenarios such as role-based access denial or filtered collections. MockMvc or direct method invocation tests can assert expected exceptions (e.g., AccessDeniedException) or filtered results, ensuring method security aligns with business logic and security policies.

```java
// Example: Testing a method with @PreAuthorize
@WithMockUser(roles = "ADMIN")
@Test
public void testAdminOnlyMethod() {
    NameService service = new NameService();
    assertEquals("Fantastico", service.getName());
}

@Service
public class NameService {
    @PreAuthorize("hasRole('ADMIN')")
    public String getName() {
        return "Fantastico";
    }
}
```

### 18.5 Testing CSRF, CORS, and Reactive Security
This section covers testing Cross-Site Request Forgery (CSRF) protection, Cross-Origin Resource Sharing (CORS) policies, and security in reactive applications. For CSRF, tests mock the CSRF token inclusion in requests to ensure protected endpoints reject unauthorized submissions, using MockMvc with the csrf() method. CORS testing verifies cross-origin headers, while reactive security testing uses WebTestClient to handle asynchronous flows, ensuring endpoints behave correctly under reactive constraints like Mono and Flux publishers.

```java
// Listing 18.10: Testing CSRF protection
@Test
public void testCsrfProtectedEndpoint() throws Exception {
    mvc.perform(post("/submit").with(csrf()))
        .andExpect(status().isOk());
}
```

### 18.6 Testing Authentication
This section focuses on testing various authentication mechanisms, including form-based login and HTTP Basic authentication. Tests simulate login attempts with MockMvc’s formLogin() method, checking for successful redirects (e.g., to /home) or failures based on credentials. This ensures the authentication flow, including login pages, success handlers, and error handling, works as intended across different configurations.

```java
// Listing 18.12: Testing form login success
@Test
public void testLoginSuccess() throws Exception {
    mvc.perform(formLogin().user("john").password("12345"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/home"));
}
```

**Summary Note**: Comprehensive testing ensures security configurations are robust and functional across various scenarios. The chapter emphasizes using annotations like @WithMockUser, @WithUserDetails, and @WithSecurityContext to simulate authentication states, alongside MockMvc and WebTestClient for HTTP and reactive testing. It covers critical aspects such as access control, CSRF protection, CORS compliance, method-level security, and authentication flows, providing a thorough framework to validate Spring Security implementations against real-world threats and requirements.
```