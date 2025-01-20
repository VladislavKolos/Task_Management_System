# Task Management System

## **⚛ Technology Stack:**

- **Backend**: Java, Spring Boot (Web, Security, Data JPA, Validation, AOP, Flyway)
- **Database**: PostgreSQL
- **Dependency Management**: Maven
- **Testing**: JUnit, Mockito
- **Logging**: Log4j2
- **Containerization**: Docker, Docker Compose

---

## **✅ Prerequisites**

1. **Docker** and **Docker Compose** installed.
2. **Postman** installed for API testing.
3. Basic knowledge of Docker and RESTful APIs.

---

## **🚀 Quick Start**

### **How to Open the Project in IntelliJ IDEA**

Follow these steps to open the project locally using IntelliJ IDEA:

1. **Clone the Repository:**
   - Copy the repository URL: `https://github.com/VladislavKolos/Task_Management_System`
   - Open IntelliJ IDEA.
   - Go to **File** > **New** > **Project from Version Control...**.
   - Paste the repository URL and select the directory where you want to clone the project.
   - Click **Clone**.

2. **Open the Project:**
   - Once the project is cloned, IntelliJ IDEA will automatically open it.
   - If prompted, click **Trust Project** to load it.

---

### **1. Prepare the ****`.env`**** File**

In the root of the project, there is an `.env` file containing environment variables for the database and application configuration. This file is pre-configured for demonstration purposes.

> **Important:** For security reasons, storing sensitive information in `.env` files is discouraged in production. However, these values are provided for testing convenience.

### **2. Build and Run the Application**

Use the following commands to build and run the application with Docker:

#### **To build and start the application:**

```bash
docker-compose up --build
```

#### **To stop and remove containers:**

```bash
docker-compose down
```

### **3. Access the Application**

Once the application is running:

- Access the API documentation: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Use Postman for testing (instructions below).

---

## **🔑 Default Credentials**

In the project, several administrator accounts are already created to help you test the functionality of the application. Here are their credentials:

| Role  | Email                      | Password          |
|-------|----------------------------|-------------------|
| Admin | `admin1@gmail.com`         | `adminpassword1001` |
| Admin | `admin2@gmail.com`         | `adminpassword2112` |
| Admin | `admin3@mail.ru`           | `adminpassword3221` |

> **Note:** Sharing login credentials in this manner is not secure and should be avoided in production systems. However, for the purpose of fully testing the application's functionality, these details are provided here.

---

## **📬 Postman Collection**

To test the API endpoints, follow these steps:

1. In the root of the repository, locate the file `Task_Management_System.postman_collection.json`.
2. Open Postman and click on the **Import** button.
3. Select the `Task_Management_System.postman_collection.json` file to import the pre-configured API requests.
4. Use the default admin credentials (provided above) to log in **or register your own account**.
5. Explore the API by executing the pre-configured requests in the collection to test task management features.

---

## **🔎 Troubleshooting**

- **Logs not appearing:** Ensure the `logs` directory has the correct permissions.
- **Database connection issues:** Verify the `.env` file and `docker-compose.yml` configurations.
