## Chapter 1: Security Today

### 1.1 Discovering Spring Security
Spring Security is a powerful and highly customizable framework for authentication and access control in Spring applications. It simplifies implementing security but requires deliberate configuration—it's not automatic. As an open-source project under Apache 2.0, it's suitable for web, reactive, and non-web apps, with source code on GitHub. Alternatives like Apache Shiro are lighter but lack Spring Security's depth. The framework uses annotations, beans, and configurations to secure methods, integrating seamlessly with Spring's philosophy. It's the de facto choice for Spring apps, offering choices for authentication, authorization, and attack protection.

```java
// Example: Basic Spring Security configuration (not from book, illustrative)
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
```

### 1.2 What is Software Security?
Software security protects applications from unauthorized access, modification, or interception of sensitive data, ensuring compliance (e.g., GDPR). It's layered: infrastructure (networks, VMs), system, and application (Spring Security's focus). At the application level, it involves authentication (identifying users), authorization (access control), and data protection (encryption/hashing at rest/in transit). Vulnerabilities like weak auth in microservices can lead to breaches such as impersonation or data leaks. Security is a cross-cutting concern, not relying on upper layers for protection.

### 1.3 Why is Security Important?
Security prevents breaches that cause financial loss, identity theft, reputational damage, legal penalties, and user churn for organizations. For users, it safeguards critical data (e.g., banking, emails). Examples: leaked internal data erodes trust; unauthorized transactions bankrupt companies; exposed details invite lawsuits. Small flaws (e.g., CSRF, broken auth) have massive impacts. Early investment in security is cheaper than fixing exploits, as nonfunctional aspects like performance and security are essential from the start.

### 1.4 What Will You Learn in This Book?
The book teaches practical Spring Security implementation with Java 17/21, Spring 6, and Spring Boot 3. Key topics: architecture/components; authentication/authorization (including OAuth 2/OpenID Connect); layered configurations; reactive apps; testing. Emphasizes best practices, flexibility, and hands-on examples for real-world adaptation. Includes references to books like JUnit in Action, Unit Testing Principles, and Testing Java Microservices for deeper testing knowledge.

**Summary Note**: Security is crucial from an app's design phase, addressing hidden nonfunctional requirements to prevent vulnerabilities. Spring Security simplifies this for Spring apps, but proper configuration is key. The chapter sets the foundation for understanding security's role and introduces the book's practical approach.