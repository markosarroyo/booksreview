\# 📚 BooksReview



A microservices-based REST API for managing books and user reviews, built with Java 17, Spring Boot, and MongoDB.



\## Architecture



The project is composed of three independent microservices, each running in its own Docker container:



| Service | Responsibility |

|---|---|

| `book-service` | Manage books (CRUD) |

| `review-service` | Manage reviews linked to books |

| `user-service` | Manage users |



All services share a MongoDB instance orchestrated via Docker Compose.



\## Tech Stack



\- \*\*Java 17\*\*

\- \*\*Spring Boot 3\*\*

\- \*\*Spring Data MongoDB\*\*

\- \*\*Docker \& Docker Compose\*\*

\- \*\*REST API\*\*



\## Getting Started



\### Prerequisites



\- \[Docker](https://www.docker.com/) installed and running

\- \[Java 17+](https://adoptium.net/)

\- \[Maven](https://maven.apache.org/)



\### Run the project



Clone the repository:



```bash

git clone https://github.com/markosarroyo/booksreview.git

cd booksreview

```



Start all services with Docker Compose:



```bash

docker-compose up --build

```



This will start:

\- MongoDB on port `27017`

\- book-service on port `8081`

\- review-service on port `8082`

\- user-service on port `8083`



\### Stop the project



```bash

docker-compose down

```



\## API Endpoints



\### Book Service (`http://localhost:8081`)



| Method | Endpoint | Description |

|---|---|---|

| GET | `/api/books` | Get all books |

| GET | `/api/books/{id}` | Get book by ID |

| POST | `/api/books` | Create a new book |

| PUT | `/api/books/{id}` | Update a book |

| DELETE | `/api/books/{id}` | Delete a book |



\### Review Service (`http://localhost:8082`)



| Method | Endpoint | Description |

|---|---|---|

| GET | `/api/reviews` | Get all reviews |

| GET | `/api/reviews/{id}` | Get review by ID |

| POST | `/api/reviews` | Create a new review |

| DELETE | `/api/reviews/{id}` | Delete a review |



\### User Service (`http://localhost:8083`)



| Method | Endpoint | Description |

|---|---|---|

| GET | `/api/users` | Get all users |

| GET | `/api/users/{id}` | Get user by ID |

| POST | `/api/users` | Register a new user |

| DELETE | `/api/users/{id}` | Delete a user |



\## Project Status



🚧 Work in progress — actively developed as a learning project to practice Spring Boot microservices and MongoDB.



\## Author



\*\*Marcos Arroyo Martín\*\*  

\[LinkedIn](https://www.linkedin.com/in/marcos-arroyo-martin) · \[GitHub](https://github.com/markosarroyo)

