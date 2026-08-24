## 🐍 Key Features

*  **Classic Snake Game** – Play the traditional Snake game through a web-based interface.
*  **Snake Movement** – Control the snake and navigate around the game area.
*  **Food Collection** – Collect food to increase the snake's length and score.
*  **Score Tracking** – Track the player's score during gameplay.
*  **Java Backend** – Game logic is implemented using Java.
*  **Maven Build** – Project dependencies and build management are handled using Maven.
*  **Dockerized Application** – The application can be packaged and executed using Docker.
*  **Jenkins CI/CD** – Jenkins is used to automate the build and deployment workflow.

---

## 🏗️ Architecture Pipeline

```text
Developer
    ↓
GitHub Repository
    ↓
Jenkins Pipeline
    ↓
Maven Build
    ↓
Test
    ↓
Docker Image Build
    ↓
Docker Container
    ↓
Application Deployment
```

---

## 🛠️ Tech Stack

| Category                 | Technologies     |
| ------------------------ | ---------------- |
| **Programming Language** | Java             |
| **Build Tool**           | Apache Maven     |
| **Backend**              | Java             |
| **Frontend**             | Web Interface    |
| **Containerization**     | Docker           |
| **CI/CD**                | Jenkins          |
| **Version Control**      | Git, GitHub      |
| **Configuration**        | XML, Jenkinsfile |
| **Project Management**   | Maven `pom.xml`  |

The repository specifically contains a `pom.xml`, `Dockerfile`, and `Jenkinsfile`, supporting the Maven, Docker, and Jenkins workflow described above.

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/dhanashree340-cmyk/devops.git
cd devops
```

### 2. Run Using Maven

Make sure Java and Maven are installed.

Build the project:

```bash
mvn clean package
```

Then run the generated application according to the project's Maven configuration.

### 3. Run Using Docker

Build the Docker image:

```bash
docker build -t snake-game .
```

Run the container:

```bash
docker run -p 8080:8080 snake-game
```

Open the application in your browser:

```text
http://localhost:8080
```

The current repository README also documents the Docker quick-start commands using port `8080`.

### 4. Jenkins CI/CD

The repository includes a `Jenkinsfile` for automating the project workflow. The general pipeline is:

```text
GitHub
   ↓
Jenkins
   ↓
Maven Build
   ↓
Testing
   ↓
Docker Build
   ↓
Deployment
```

This allows the application to be built and packaged automatically whenever the CI/CD pipeline is triggered.
