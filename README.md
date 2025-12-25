# RAG-Based Document Chat System

A Spring Boot–based **Retrieval-Augmented Generation (RAG)** application that allows users to upload documents and ask natural-language questions.  
The system performs semantic search using vector embeddings and generates accurate, context-aware answers using an LLM.


## 🚀 Features

- Upload documents (PDF / TXT / DOCX)
- Automatic text extraction and chunking
- Vector embedding generation
- Semantic similarity search using Qdrant
- Context-aware answer generation
- Clean layered architecture (Controller → Service → Repository)
- Spring Security–ready setup


## 🛠 Tech Stack

- **Backend**: Spring Boot 3, Java 17
- **ORM**: JPA / Hibernate
- **Database**: MySQL
- **Vector Database**: Qdrant
- **Embeddings**: Google Embeddings (768 dimensions)
- **LLM**: Gemini
- **Build Tool**: Maven


## 📋 Prerequisites

Make sure you have the following installed:

- Java 17+
- Maven 3.8+
- MySQL 8+
- Docker (for Qdrant)
- Git


## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/ramkrishna500/rag-doc-chat.git

cd rag-doc-chat

2️⃣ Start Qdrant (Vector Database) using: 
docker run -p 6333:6333 qdrant/qdrant

Verify Qdrant is running:
http://localhost:6333

3️⃣ Create Qdrant Collection (IMPORTANT)👇:

This project uses 768-dimension embeddings.
PUT http://localhost:6333/collections/doc_chunks  (On postman)

Json:
{
  "vectors": {
    "size": 768,
    "distance": "Cosine"
  }
}
You must do this before uploading documents.

4️⃣ Setup MySQL Database

Login to MySQL: 
mysql -u root -p

Create database: 
CREATE DATABASE ragdocchat;

5️⃣ Configure application.properties

Update database credentials: 
spring.datasource.url=jdbc:mysql://localhost:3306/ragdocchat
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

Add your API keys (recommended via environment variables):
google.api.key=YOUR_GOOGLE_API_KEY
gemini.api.key=YOUR_GEMINI_API_KEY

6️⃣ Run the Application
mvn spring-boot:run

Application will start at:
http://localhost:8080

📤 API Usage
Upload Document
POST /api/documents/upload

Postman setup
Body → form-data
Key: file
Type: File
Value: select document

Search / Ask Question
POST /api/search
{
  "query": "What is the interest calculation method?"
}

⚠️ Important Notes
Always use the same embedding model for both document ingestion and search.
If you change embedding provider or dimensions:
Delete the Qdrant collection
Recreate it with the new size
Re-upload documents
Qdrant does not auto-create collections.

🧠 Troubleshooting

❌ 401 Unauthorized
Spring Security blocks requests by default

Ensure the upload/search endpoints are permitted in SecurityConfig

❌ 400 Bad Request from Qdrant
Embedding dimension mismatch

Collection size must match embedding vector size exactly

❌ 404 Collection not found
Create the Qdrant collection manually (see step 3)

📌 Project Structure
controller/    → REST endpoints
service/       → Business logic
repository/    → Database access
model/         → JPA entities
config/        → Security & configuration

👤 Author
Ramkrushna Madole
GitHub: https://github.com/ramkrishna500

⭐ If you find this useful
Give the repo a ⭐ and feel free to fork or contribute!


Made with ❤️ and⚡
