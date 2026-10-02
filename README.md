# URL Shortener

A RESTful URL Shortener application built using Java and Spring Boot. The application converts long URLs into short, easy-to-share links and provides features such as custom aliases, URL expiration, click tracking, validation, and exception handling.

## Features

* Create short URLs from long URLs
* Generate unique short codes automatically
* Create custom short aliases
* Redirect short URLs to the original URL
* Automatic URL expiration after 24 hours
* Track the number of clicks on each short URL
* Validate original URLs
* Handle invalid or expired URLs using global exception handling
* Delete existing short URLs
* View URL details and click statistics
* Swagger/OpenAPI documentation
* Unit testing with JUnit and Mockito

## Architecture

<img width="1312" height="1199" alt="URL Shortener Architecture Diagram" src="https://github.com/user-attachments/assets/677658a6-022b-4da4-b797-0dd87e711357" />



## Technologies Used

* **Java**
* **Spring Boot**
* **Spring Data JPA**
* **REST APIs**
* **MySQL**
* **Maven**
* **JUnit**
* **Mockito**
* **Swagger / OpenAPI**
* **Git & GitHub**

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.hemanshu
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── repository
│   │       └── service
│   │
│   └── resources
│       └── application.properties
│
└── test
    └── java
        └── com.hemanshu
            └── service
```

## How It Works

The application follows a simple layered architecture:

```text
Client
   ↓
REST Controller
   ↓
Service Layer
   ↓
JPA Repository
   ↓
MySQL Database
```

### URL Creation Flow

1. Client sends a long URL to the `/api/shorten` endpoint.
2. The controller validates the request.
3. The service generates a unique short code or uses the requested custom alias.
4. The URL information is stored in MySQL using Spring Data JPA.
5. The API returns the generated short code and URL details.

### Redirect Flow

1. User opens the short URL.
2. The application searches for the corresponding short code.
3. The application checks whether the URL has expired.
4. The click count is incremented.
5. The user is redirected to the original URL.

## API Endpoints

| Method | Endpoint                | Description                     |
| ------ | ----------------------- | ------------------------------- |
| POST   | `/api/shorten`          | Create a short URL              |
| GET    | `/{shortCode}`          | Redirect to original URL        |
| GET    | `/api/urls/{shortCode}` | Get URL details and click count |
| DELETE | `/api/urls/{shortCode}` | Delete a short URL              |

## Create Short URL

### Request

```http
POST /api/shorten
Content-Type: application/json
```

```json
{
  "originalUrl": "https://iitbhu.ac.in/dept/cse/faculty"
}
```

### Custom Alias

A custom short code can also be provided:

```json
{
  "originalUrl": "https://iitbhu.ac.in/dept/cse/faculty",
  "customCode": "iit"
}
```

### Response

```json
{
  "originalUrl": "https://iitbhu.ac.in/dept/cse/faculty",
  "shortCode": "iit",
  "createdAt": "2026-10-02T10:10:46",
  "expiresAt": "2026-10-03T10:10:46",
  "clickCount": 0
}
```

## Redirect

After creating a short URL such as:

```text
http://localhost:8080/iit
```

opening the URL redirects the user to the original URL.

Each successful redirect also increments the click count.

## URL Details

```http
GET /api/urls/iit
```

Example response:

```json
{
  "originalUrl": "https://iitbhu.ac.in/dept/cse/faculty",
  "shortCode": "iit",
  "createdAt": "2026-10-02T10:10:46",
  "expiresAt": "2026-10-03T10:10:46",
  "clickCount": 3
}
```

## Delete URL

```http
DELETE /api/urls/iit
```

A successful deletion returns:

```text
204 No Content
```

## URL Expiration

Each generated short URL is assigned an expiration time of **24 hours** from creation.

When an expired short URL is accessed, the application returns an appropriate error response instead of redirecting the user.

## Validation

The application validates the original URL before creating a short URL.

The URL must:

* Not be empty
* Start with `http://` or `https://`

Invalid requests return a `400 Bad Request` response.

## Exception Handling

The application uses a global exception handler with `@RestControllerAdvice`.

It handles cases such as:

* Short URL not found
* Short URL expired
* Duplicate custom short code
* Invalid request data

## Swagger API Documentation

Swagger/OpenAPI is integrated into the application for interactive API documentation and testing.

After starting the application, open:

```text
http://localhost:8080/swagger-ui.html
```

Swagger provides an interactive interface for testing the available REST APIs.

## Running the Project

### Prerequisites

Make sure the following are installed:

* Java
* Maven
* MySQL

### Database Setup

Create a MySQL database:

```sql
CREATE DATABASE url_shortener;
```

Configure the database connection in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Replace `YOUR_PASSWORD` with your local MySQL password.

**Do not commit your real database password to GitHub.**

### Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

The application will start at:

```text
http://localhost:8080
```

## Testing

The project includes unit tests covering important service-layer functionality, including:

* Short code generation
* Custom short codes
* Duplicate custom codes
* URL expiration
* Click-count increment
* URL not found
* Valid URL retrieval

Run the tests using:

```bash
mvn test
```

## Future Improvements

Possible future enhancements include:

* User authentication and authorization
* Custom expiration durations
* Advanced analytics
* Rate limiting
* Caching
* Production deployment
* Dockerization

## Author

**Hemanshu Kadam**

M.Tech Computer Science and Engineering
IIT (BHU), Varanasi
