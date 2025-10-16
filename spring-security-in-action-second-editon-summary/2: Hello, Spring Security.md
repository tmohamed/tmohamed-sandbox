## Chapter 2: Hello, Spring Security

### 2.1 Starting Your First Project
Begin with a Spring Boot project adding web and security dependencies, auto-configuring HTTP Basic and Form Login with default user "user" and generated password. Test /hello endpoint: 401 Unauthorized without credentials, 200 OK with valid auth. Spring Security uses filters for auth logic without manual code. Note: Defaults include both HTTP Basic and Form Login; browser shows form, but focus on HTTP Basic here.

```java
// Listing 2.1: Spring Security dependencies for our first web app
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 2.2 The Big Picture of Spring Security Class Design
Spring Security employs a filter chain: AuthenticationFilter delegates to AuthenticationManager using AuthenticationProvider, validating against UserDetailsService and PasswordEncoder. Successful auth stores in SecurityContext via SecurityContextHolder. Default in-memory user management can be overridden for custom logic.

### 2.3 Overriding Default Configurations
Customize with UserDetailsService for multiple users/authorities. Configure endpoint authorization via HttpSecurity, e.g., authorizeHttpRequests() with hasAuthority() or permitAll(). Use lambdas/method references for flexibility. Implement custom AuthenticationProvider for specific logic. Modularize configs with multiple @Configuration classes.

```java
// Listing 2.7: Using the HttpSecurity parameter to alter the configuration
@Configuration
public class ProjectConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.httpBasic(Customizer.withDefaults());
    http.authorizeHttpRequests(c -> c.anyRequest().authenticated());
    return http.build();
  }
}
```

**Summary Note**: Spring Boot's convention-over-configuration enables quick setup with defaults like HTTP Basic auth, but customization via beans and configs allows tailored security. Understand core components (AuthenticationManager, UserDetailsService) for overriding defaults, ensuring secure apps with minimal boilerplate.