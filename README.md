# Gateway

API Gateway for the microservices application. It provides a single HTTP entry point for the frontend, validates JWT access tokens, forwards requests to downstream services, and coordinates registration across User Service and Authentication Service.

Repository: https://github.com/hacker2023beginer/Gateway

## Tech Stack

- Java 21
- Spring Boot 3.2.3
- Spring Cloud Gateway
- Spring WebFlux
- Springdoc OpenAPI WebFlux UI
- JJWT
- Docker
- Kubernetes manifests

## Responsibilities

- Exposes the public API on port `8080`.
- Routes requests to:
  - Authentication Service: `8081`
  - User Service: `8082`
  - Order Service: `8083`
  - Payment Service: `8084`
- Handles JWT validation for protected requests.
- Allows frontend CORS from `http://localhost:5173`.
- Provides registration orchestration:
  - creates a user in User Service;
  - creates credentials in Authentication Service;
  - rolls back the user if credential creation fails.

## Service URLs

Default local configuration:

| Service | URL |
|---|---|
| Gateway | `http://localhost:8080` |
| Authentication Service | `http://localhost:8081` |
| User Service | `http://localhost:8082` |
| Order Service | `http://localhost:8083` |
| Payment Service | `http://localhost:8084` |
| Frontend | `http://localhost:5173` |

## Configuration

Main properties:

```properties
server.port=8080
services.payment-service.url=http://localhost:8084
services.order-service.url=http://localhost:8083
services.user-service.url=http://localhost:8082
services.auth-service.url=http://localhost:8081
jwt.secret="write yours"

For Kubernetes profile, service URLs are switched to cluster service names such as http://user-service:8082.

## API Overview

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| POST | `/register` | Creates user and credentials |
| POST | `/login` | Authenticates user and returns tokens |
| POST | `/auth/refresh` | Refreshes access token |
| POST | `/credentials` | Creates or updates credentials |

### Users and Cards
| Method | Endpoint | Description |
|---|---|---|
| GET | `/users/email?email={email}` | Find user by email |
| POST | `/users/create` | Create user |
| GET | `/users/{id}` | Get user by id |
| GET | `/users` | Get paginated users with optional `name`, `surname` filters |
| PUT | `/users/update/{id}` | Update user |
| DELETE | `/users/delete/{id}` | Delete user |
| PATCH | `/users/activate/{id}` | Activate user |
| PATCH | `/users/deactivate/{id}` | Deactivate user |
| POST | `/users/cards` | Create payment card |
| GET | `/users/cards/{id}` | Get card by id |
| PUT | `/users/cards/{id}` | Update card |
| DELETE | `/users/cards/{id}` | Delete card |
| PATCH | `/users/cards/{id}/activate` | Activate card |
| PATCH | `/users/cards/{id}/deactivate` | Deactivate card |
| GET | `/users/{id}/cards` | Get user cards |

### Orders and Items
| Method | Endpoint | Description |
|---|---|---|
| POST | `/orders/createorder` | Create order |
| GET | `/orders/{id}` | Get order by id |
| GET | `/orders` | Search orders by `from`, `to`, `status`, `page`, `size` |
| GET | `/orders/byuserid/{id}` | Get orders by user id |
| PUT | `/orders/{id}` | Update order |
| DELETE | `/orders/{id}` | Delete order |
| POST | `/orders/items/create` | Create item |
| GET | `/orders/items` | Get all items |
| GET | `/orders/items/{id}` | Get item by id |
| PUT | `/orders/items/{id}` | Update item |
| DELETE | `/orders/items/{id}` | Delete item |
| POST | `/orders/orderitems` | Add item to order |
| GET | `/orders/orderitems/{id}` | Get order item by id |
| PUT | `/orders/orderitems/{id}` | Update order item |
| DELETE | `/orders/orderitems/{id}` | Delete order item |

### Payments
| Method | Endpoint | Description |
|---|---|---|
| POST | `/payments` | Create payment |
| GET | `/payments` | Filter payments by `userId`, `orderId`, or `status` |
| GET | `/payments/sum/user` | Get payment sum for a user and date range |
| GET | `/payments/sum` | Get total payment sum for a date range |

## Run Locally

Bash:       ./mvnw spring-boot:run
Swagger.ui: http://localhost:8080/webjars/swagger-ui/index.html
Docker:     docker build -t gateway . && docker run -p 8080:8080 gateway
Tests:      ./mvnw test
