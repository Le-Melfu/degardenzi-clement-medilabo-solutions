# Bonnes pratiques Java / Spring — MédiLabo

Référence condensée pour les microservices du projet. À respecter sur les itérations suivantes.

## Architecture (MCP + Repository)

| Couche | Package | Rôle |
|--------|---------|------|
| **Model** | `model` | Entités JPA / domaine — pas de logique métier |
| **Controller** | `controller` | HTTP uniquement : mapping, validation entrée, délégation |
| **Service** | `service` | Règles métier, orchestration |
| **Repository** | `repository` | Accès données (Spring Data) — aucune règle métier |

Flux : `Controller → Service → Repository → Model`

La gateway peut n’avoir **pas de Controller** si l’auth est gérée uniquement par Spring Security (cas actuel).

## Injection de dépendances

- **Toujours** l’injection par **constructeur** (final fields)
- **Jamais** `@Autowired` sur un champ
- Un seul constructeur public → pas besoin de `@Autowired`

```java
@Service
public class ExampleService {
    private final ExampleRepository repository;

    public ExampleService(ExampleRepository repository) {
        this.repository = repository;
    }
}
```

## Entités JPA

- Constructeur `protected` sans args (exigence JPA)
- Constructeur métier pour les champs obligatoires
- Getters ; setters seulement si mutation réelle
- `open-in-view: false` (pas de lazy loading caché hors transaction)
- Table / colonnes explicites (`@Table`, `nullable`, `unique`, `length`)

## Sécurité

- HTTP Basic, session **STATELESS**
- Mots de passe **BCrypt** uniquement (`PasswordEncoder` en bean)
- Pas de sign-up, pas de gestion de rôles métier
- CSRF désactivé pour une API stateless
- Endpoints sensibles → `authenticated()`

## API / Controllers (quand présents)

- Pas de logique métier dans le controller
- Validation via Bean Validation (`@Valid`, annotations sur DTO)
- DTO / records pour les payloads — ne pas exposer l’entité JPA brute si elle évolue
- Codes HTTP corrects (200, 201, 400, 404, 401)

## Configuration

- Secrets / URLs en `application.yml` (ou variables d’env en prod)
- Un `application.yml` de test dédié (H2 en mémoire)
- Beans de config dans des classes `@Configuration` ciblées

## Code style

- Packages par responsabilité, pas par type technique global
- Noms explicites (`DatabaseUserDetailsService`, pas `ServiceImpl`)
- Pas de try/catch “fourre-tout” sauf besoin explicite
- Pas de code mort, pas de commentaires redondants
- Java 21 + Spring Boot 3.x : API Security lambda (`SecurityFilterChain`)

## Gateway (spécifique)

- **Gateway MVC** (`spring-cloud-starter-gateway-mvc`) si JPA + Security servlet
- Routes sous `spring.cloud.gateway.mvc.routes`
- Auth au point d’entrée ; routage vers les services back

## Ce qu’on ne fait pas

- Field injection
- Logique métier dans Repository ou Controller
- Users en clair / encodeur `{noop}`
- Sur-ingénierie (roles, JWT, sign-up) hors user stories
