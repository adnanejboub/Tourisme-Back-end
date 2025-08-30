# Keycloak Integration Guide

## How Keycloak Users Work with Backend Users

### 1. **Two-System Architecture**

Your application uses a **dual-user system**:

- **Keycloak Users**: Handle authentication and authorization (who you are)
- **Backend Users**: Store business-specific data (what you can do in the app)

### 2. **User Flow**

```
Angular Frontend → Keycloak Authentication → JWT Token → Spring Boot Backend → Database User
```

1. **User logs in via Angular** → Keycloak authenticates and returns JWT token
2. **Angular sends requests** → Includes JWT token in Authorization header
3. **Spring Security validates token** → Automatically on every request
4. **Backend maps Keycloak user** → To corresponding database user
5. **Business logic executes** → Using database user for data operations

### 3. **Automatic Token Verification**

**Spring Security handles token verification automatically** for every endpoint:

```java
// In SecurityConfig.java
.oauth2ResourceServer(oauth2 -> oauth2
    .jwt(jwt -> jwt.jwkSetUri("http://localhost:8090/realms/tourisme/protocol/openid-connect/certs"))
)
```

**What happens on each request:**

1. **Token Extraction**: Spring Security extracts JWT from `Authorization: Bearer <token>` header
2. **Signature Verification**: Validates token signature using Keycloak's public keys
3. **Claims Validation**: Checks expiration, issuer, audience
4. **Role Extraction**: Extracts roles from token claims
5. **Access Control**: Applies `@PreAuthorize` annotations and URL-based security

### 4. **User Mapping Service**

The `UserMappingService` bridges Keycloak and backend users:

```java
@Service
public class UserMappingService {
    
    // Extract current user from JWT token
    public Optional<Utilisateur> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaimAsString("email");
            return utilisateurRepository.findByEmail(email);
        }
        return Optional.empty();
    }
    
    // Sync Keycloak user to backend
    public Utilisateur syncKeycloakUser(String keycloakId, String email, String firstName, String lastName, String roleName) {
        // Creates or updates backend user based on Keycloak data
    }
}
```

### 5. **Role Mapping**

| Keycloak Role | Backend Role | Access Level |
|---------------|--------------|--------------|
| `ADMIN` | `ADMIN` | Full administrative access |
| `TOURISTE` | `TOURISTE` | Tourist-specific features |
| `FOURNISSEUR` | `FOURNISSEUR` | Service provider features |

### 6. **Endpoint Security Examples**

#### Admin Endpoints (Require ADMIN role)
```java
@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")  // ← Token verified automatically
    public ResponseEntity<?> getAllUsers() {
        // Only accessible with valid JWT containing ADMIN role
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
```

#### Tourist Endpoints (Require TOURISTE role)
```java
@RestController
@RequestMapping("/touriste")
public class TouristController {
    
    @GetMapping("/villes")
    @PreAuthorize("hasRole('TOURISTE')")  // ← Token verified automatically
    public ResponseEntity<?> getVilles() {
        // Only accessible with valid JWT containing TOURISTE role
        return ResponseEntity.ok(villeService.getAllVilles());
    }
}
```

### 7. **Token Verification Process**

**Every request automatically goes through:**

1. **Header Check**: `Authorization: Bearer <jwt_token>`
2. **Token Validation**: 
   - Signature verification using Keycloak public keys
   - Expiration check
   - Issuer validation
3. **Role Extraction**: From JWT claims
4. **Access Control**: Based on `@PreAuthorize` annotations
5. **User Mapping**: Keycloak user → Backend user

### 8. **Testing Token Verification**

Use the `/auth/me` endpoint to test:

```bash
# Get current user info (requires valid token)
curl -H "Authorization: Bearer <your_jwt_token>" \
     http://localhost:8080/auth/me
```

**Response:**
```json
{
  "keycloakId": "12345678-1234-1234-1234-123456789012",
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "realmAccess": "{\"roles\":[\"TOURISTE\"]}",
  "backendUser": {
    "idUtilisateur": 1,
    "nom": "Doe",
    "prenom": "John",
    "email": "user@example.com",
    "role": {
      "nomRole": "TOURISTE"
    }
  },
  "tokenVerified": true
}
```

### 9. **Error Handling**

**Invalid Token (401 Unauthorized):**
```json
{
  "error": "No valid token found"
}
```

**Insufficient Permissions (403 Forbidden):**
```json
{
  "error": "Access Denied",
  "message": "Access is denied"
}
```

### 10. **Security Best Practices**

1. **Always use HTTPS** in production
2. **Set appropriate token expiration** in Keycloak
3. **Use role-based access control** with `@PreAuthorize`
4. **Validate user existence** in backend before operations
5. **Log authentication events** for security monitoring

### 11. **Configuration Files**

**application.properties:**
```properties
# Keycloak JWT configuration
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8090/realms/tourisme
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8090/realms/tourisme/protocol/openid-connect/certs
```

**SecurityConfig.java:**
```java
.authorizeHttpRequests(authz -> authz
    .requestMatchers("/auth/register").permitAll()
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/touriste/**").hasRole("TOURISTE")
    .anyRequest().authenticated()
)
```

### 12. **Summary**

- **Token verification is automatic** - no manual code needed in controllers
- **Keycloak handles authentication** - username/password, social login, etc.
- **Backend handles authorization** - role-based access control
- **User mapping is seamless** - JWT email links to backend user
- **Security is enforced** - every endpoint requires valid token with proper roles

The system provides a secure, scalable authentication and authorization solution where Keycloak manages user identity and your backend manages business logic and data access. 