# Hog & Hive Crafts

Your home for patterns, projects, and creativity.

## 📌 Overview

**Hog & Hive Crafts** is a full-stack web application that helps crafters organise their pattern libraries and (in future iterations) track their materials and works-in-progress, along with much more. Whether you're a knitter, crocheter, sewist, or any other type of crafter, this platform aims to make it easy to:

- Store all your patterns in one place
- Filter by craft type, skill level, materials and more *(future)*
- Track supplies and project progress *(future)*
- Share ideas and get inspiration *(future)*
- Receive suggestions based on your stash and preferences *(future)*

This project is being built primarily as a learning-focused software engineering project.

## 🧰 Tech Stack

| **Area** | **Stack/Tools** |
| ------------ | ------------- |
| Backend | Java 21, Spring Boot, Maven, Spring Security, Spring Data JPA |
| Database | PosgreSQL, Flyway |
| Backend Testing | JUnit, Mockito, Testcontainers |
| Frontend | React, TypeScript, Vite, Tailwind CSS |
| Frontend Testing | React Testing Library, Vitest, Playwright |
| DevOps & Tooling | Git, GitHub, Docker, Render |

The application follows a modular monolith architecture with a layered design:

- Controller Layer
- Service Layer
- Repository Layer
- Security Configuration

The backend exposes a RESTful API and uses local filesystem-based file storage.

## 📁 Project Structure

```txt
hog-and-hive-crafts/
├── backend/ # Spring Boot application
├── backend/ # React application
├── .github/ # GitHub Actions workflows
├── docker-compose.yml
└── README.md

```

## 📋 Project Management

Development is being tracked using a Kanban workflow.

- [GitHub Project Board](https://github.com/users/GemzH11/projects/3)
- [GitHub Issue Tracker](https://github.com/GemzH11/hog-and-hive-crafts/issues)

## ✅ Project Status

This project is in active development.

Current Phase: Initial Setup (v0.1)

v0.1 Scope:
- User registration and login
- Private pattern CRUD
- PDF upload and download
- Basic UI for managing patterns

Future features will be developed iteratively.

## ⚙️ Setup Instructions

Details will be added once the initial implementation is complete.

## 🧪 Running Tests

Details will be added once the initial implementation is complete.

## 🐳 Running with Docker Compose

Details will be added once the initial implementation is complete.

## 🤝 Contributing

This is a personal project in active development. Contributions are not currently being accepted, but you're welcome to open issues or share ideas via the [GitHub issue tracker](https://github.com/GemzH11/hog-and-hive-crafts/issues).

