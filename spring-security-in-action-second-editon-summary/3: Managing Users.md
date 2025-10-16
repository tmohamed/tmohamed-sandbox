## Chapter 3: Managing Users

### 3.1 Implementing Authentication in Spring Security
Authentication relies on contracts like UserDetails (user info) and UserDetailsService (loads users) for decoupling. These enable flexible user management. The architecture involves AuthenticationProvider using UserDetailsService and PasswordEncoder, storing authenticated details in SecurityContextHolder.

### 3.2 Describing the User
UserDetails defines username, password, authorities (GrantedAuthority for actions), and account flags (enabled, expired, etc.). Implement or use User builder. Custom classes can extend for additional attributes like full names, while adhering to the contract.

```java
// Listing 3.6: Constructing a user with the User builder class
UserDetails u = User.withUsername("bill")
  .password("12345")
  .authorities("read")
  .build();

// Listing 3.2: A custom implementation of the UserDetails contract
public class CustomUserDetails implements UserDetails {
  private final String username;
  private final String password;
  private final String authority;

  public CustomUserDetails(String username, String password, String authority) {
    this.username = username;
    this.password = password;
    this.authority = authority;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> authority);
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  // Flags default to true
  @Override
  public boolean isAccountNonExpired() { return true; }
  @Override
  public boolean isAccountNonLocked() { return true; }
  @Override
  public boolean isCredentialsNonExpired() { return true; }
  @Override
  public boolean isEnabled() { return true; }
}
```

### 3.3 Instructing Spring Security on How to Manage Users
UserDetailsService loads users by username; implement for custom sources (in-memory, JDBC, LDAP). UserDetailsManager extends for CRUD (create, update, delete users). Use InMemoryUserDetailsManager for testing, JdbcUserDetailsManager for databases, LdapUserDetailsManager for LDAP. Custom implementations integrate with any backend.

```java
// Listing 3.13: The implementation of the UserDetailsService interface
public class InMemoryUserDetailsService implements UserDetailsService {
  private final List<UserDetails> users;

  public InMemoryUserDetailsService(List<UserDetails> users) {
    this.users = users;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    return users.stream()
      .filter(u -> u.getUsername().equals(username))
      .findFirst()
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}

// Listing 3.15: Defining the JdbcUserDetailsManager bean
@Configuration
public class ProjectConfig {
  @Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }
}
```

**Summary Note**: User management uses extensible contracts for storage-agnostic authentication. UserDetails describes users, GrantedAuthority defines actions, UserDetailsService loads users, and UserDetailsManager handles CRUD. Implementations like in-memory, JDBC, or custom fit various backends, ensuring flexibility in real-world apps.