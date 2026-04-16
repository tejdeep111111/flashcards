# ⚡ DBMS Flashcards

A full-stack web application for studying **Database Management Systems (DBMS)** concepts using interactive flashcards.

---

## 5-Minute Presentation Guide

### 1 · Problem Statement *(~30 s)*

Students preparing for DBMS exams often lack an interactive, self-paced way to revisit key concepts. Traditional notes are static; this app turns those concepts into **flip-card quizzes** they can browse, filter, and study anywhere in a browser.

---

### 2 · Tech Stack *(~30 s)*

| Layer | Technology |
|---|---|
| **Backend** | Java 17 · Spring Boot 3.5 · Spring Data JPA |
| **Database** | MySQL (schema auto-managed by Hibernate) |
| **Frontend** | Vanilla HTML 5 / CSS 3 / JavaScript (single-page app) |
| **Build tool** | Apache Maven |

---

### 3 · Architecture *(~1 min)*

```
Browser (frontend/index.html)
        │  HTTP / REST JSON
        ▼
Spring Boot REST API  (port 8080)
  └─ FlashcardApiController  →  FlashcardService
                                      │
                              FlashcardRepo / TopicRepo
                                      │
                                   MySQL
                           ┌──────────────────┐
                           │  topic           │
                           │  ─────────────   │
                           │  id · name · icon│
                           └────────┬─────────┘
                                    │ 1 : N
                           ┌────────▼─────────┐
                           │  flashcard       │
                           │  ─────────────   │
                           │  id · question   │
                           │  answer          │
                           │  difficulty      │
                           │  topic_id (FK)   │
                           └──────────────────┘
```

The frontend is a **static single-page application** served independently (e.g. via Live Server / any HTTP server). It communicates with the backend exclusively through REST calls — no server-side rendering.

---

### 4 · Key Features *(~1.5 min)*

| Feature | How it works |
|---|---|
| **Home** | Displays live stats (total cards, total topics) and a clickable topic grid |
| **Browse** | Filter cards by topic and/or difficulty (Easy / Medium / Hard); flip a card to reveal the answer |
| **Shuffle** | Randomises card order at the database level (`ORDER BY RAND()`) |
| **Study Mode** | Sequential card-by-card review with prev / next navigation and progress dots |
| **Add Card** | Form to create a new flashcard (topic, difficulty, question, answer); saved via `POST /api/cards` |
| **Delete Card** | Remove a card from the back of a flipped card; calls `DELETE /api/cards/{id}` |
| **Responsive UI** | Dark-theme, mobile-friendly layout using CSS custom properties |

---

### 5 · REST API Endpoints *(~30 s)*

| Method | URL | Description |
|---|---|---|
| `GET` | `/api/topics` | List all topics |
| `GET` | `/api/cards` | List cards (optional: `?topicId=`, `?difficulty=`, `?shuffle=true`) |
| `POST` | `/api/cards` | Create a new flashcard |
| `DELETE` | `/api/cards/{id}` | Delete a flashcard by ID |

---

### 6 · Data Model *(~15 s)*

- **Topic** — `id`, `name`, `icon` (emoji)
- **Flashcard** — `id`, `question` (TEXT), `answer` (TEXT), `difficulty` (EASY / MEDIUM / HARD), `topic` (FK → Topic)

---

### 7 · How to Run *(~15 s)*

**Prerequisites:** Java 17+, Maven, MySQL running locally.

```bash
# 1. Create the database
mysql -u root -p -e "CREATE DATABASE dbms_flashcards;"

# 2. Update credentials in src/main/resources/application.properties
#    spring.datasource.username / spring.datasource.password

# 3. Start the backend (Hibernate creates tables automatically)
mvn spring-boot:run

# 4. Open the frontend
open frontend/index.html   # or use VS Code Live Server
```

Backend runs on **http://localhost:8080**. The frontend auto-connects to it.

---

### 8 · Project Structure *(reference)*

```
flashcards/
├── frontend/
│   └── index.html                  # Single-page frontend (HTML + CSS + JS)
├── src/main/java/com/dbms/flashcards/
│   ├── FlashCardsApplication.java  # Spring Boot entry point
│   ├── controller/
│   │   └── FlashcardApiController.java
│   ├── service/
│   │   └── FlashcardService.java
│   ├── repository/
│   │   ├── FlashcardRepo.java
│   │   └── TopicRepo.java
│   └── model/
│       ├── Flashcard.java          # Entity: question, answer, difficulty, topic
│       └── Topic.java              # Entity: name, icon
├── src/main/resources/
│   └── application.properties      # DB connection & JPA config
└── pom.xml                         # Maven dependencies
```

---

## Quick-Reference Card for Q&A

| Question | Answer |
|---|---|
| Why Spring Boot? | Rapid setup, embedded Tomcat, production-ready defaults |
| Why MySQL? | Relational data fits a strict topic–card hierarchy; `ORDER BY RAND()` natively supported |
| Why vanilla JS? | Zero build-step needed for the frontend; keeps the project lightweight |
| How is shuffling done? | `SELECT * FROM flashcard ORDER BY RAND()` via a native JPA query |
| How are cross-origin calls handled? | `@CrossOrigin(origins = "*")` on the controller |
| What prevents duplicate topics? | Topics are seeded / managed manually in the DB; no duplicate-check API yet (future improvement) |
