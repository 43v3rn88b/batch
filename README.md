Automated Legacy Data Importer (Spring Batch)
---------------------------------------------

This project demonstrates the ability to handle heavy-duty, background data processing—a core requirement for enterprise-level Java applications.

### 🚀 Overview

A high-performance batch processing application that automates the migration of legacy project data from CSV files into a relational SQL database. It features a robust **Reader-Processor-Writer** architecture and automated scheduling.

### 🛠️ Key Features

*   **ETL Pipeline:** Implemented using Spring Batch to read CSV data, transform/clean it in-memory, and write it to an H2 database in chunks for efficiency.
    
*   **Spring Data JPA/JDBC:** Managed database interactions and schema generation.
    
*   **Job Scheduling:** Configured with @Scheduled to trigger data synchronization every 10 seconds (configurable for production).
    
*   **State Management:** Utilizes Spring Batch Metadata tables to track job execution status (COMPLETED/FAILED).
    

### 🏗️ Architecture

1.  **ItemReader:** Reads raw strings from projects-data.csv.
    
2.  **ItemProcessor:** Applies business logic (e.g., data normalization, uppercase conversion).
    
3.  **ItemWriter:** Efficiently batches records for SQL insertion using JdbcBatchItemWriter.
    

### 🚦 How to Run

1.  Clone the repository.
    
2.  Run ```./mvnw spring-boot:run.```
    
3.  Monitor the console to see the job execute every 10 seconds.
    
4.  Access the H2 Console at http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:testdb) to see the migrated data.
    

🛣️ Production Roadmap
----------------------

While this project serves as a functional Proof of Concept, the following enhancements would prepare it for a high-availability production environment:

*   **Persistent Storage:** Transition from an in-memory H2 database to a production-grade **PostgreSQL** or **MySQL** instance to ensure metadata and project history are preserved across restarts.
    
*   **External File Integration:** Replace the local ClassPathResource reader with an **AWS S3** or **Azure Blob Storage** integration to handle files uploaded from external legacy systems.
    
*   **Enhanced Fault Tolerance:** Implement advanced SkipPolicies to log corrupted CSV rows to a "dead-letter" table rather than failing the job, and add **Retry logic** for temporary database connection issues.
    
*   **Observability:** Integrate **Spring Boot Actuator** and **Prometheus/Grafana** to monitor Job health, chunk execution times, and record success/failure rates in real-time.
    
*   **Parallel Processing:** Configure a TaskExecutor to utilize multi-threading for the Step execution, allowing the system to scale horizontally for multi-million row datasets.
