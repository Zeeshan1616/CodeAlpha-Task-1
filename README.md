# CodeAlpha-Task-1
This is the Task no.1 of the Java Programming Internship by @CodeAlpha

# 📊 Student Grade Tracker

A desktop application built in Java (Swing) that allows faculty to manage students and track their grades — complete with a login screen, live statistics, and report generation.
---
## ✨ Features
- 🔐 **Faculty Login** — Simple authentication before accessing the dashboard
- 👤 **Student Management** — Add or remove students with auto-assigned IDs
- 📝 **Grade Entry** — Record multiple scores (0–100) per student
- 📈 **Live Statistics** — Class-wide average, highest, and lowest scores update in real time
- 🅰️ **Letter Grades** — Automatically calculated (A/B/C/D/F) per student
- 📋 **Report Generation** — View a full formatted summary of all students
- 🖥️ **Clean GUI** — Split-panel layout with a styled table, color-coded grades, and a status bar
---
## 🛠️ Tech Stack
| Layer        | Technology                                          |
|--------------|-----------------------------------------------------|
| Language     | Java                                                |
| GUI          | Java Swing                                          |
| OOP Concepts | Encapsulation, Inheritance, Interfaces, Abstraction |
---
## 🧱 Project Structure
```
Task1.java
├── Gradable          (interface)   — contract for grade operations
├── Person            (abstract)    — base class for any person in the system
├── Student           (class)       — extends Person, implements Gradable
├── LoginDialog       (class)       — modal authentication dialog
└── Task1 / JFrame    (main class)  — full application window & UI logic
---
## 🚀 Getting Started

### Prerequisites

- Java JDK 11 or higher
- Any IDE (IntelliJ IDEA, Eclipse, VS Code) or command line

### Run via Command Line

```bash
# Compile
javac Task1.java

# Run
java InternshipTasks.Task1
```

### Run via IDE

1. Open the project in your IDE
2. Navigate to `Task1.java`
3. Run the `main` method

---

## 🔑 Default Login Credentials

| Field    | Value |
|----------|-------|
| Username | `faculty` |
| Password | `1234` |

> These are hardcoded for demo purposes. For production use, replace with a secure authentication system.

---

## 📐 Grade Scale

| Average Score | Letter Grade |
|---------------|-------------|
| 90 – 100      | A |
| 80 – 89       | B |
| 70 – 79       | C |
| 60 – 69       | D |
| Below 60      | F |

---

## 📸 UI Overview

| Section | Description |
|---------|-------------|
| **Left Panel** | Add students, add grades, view class stats |
| **Right Panel** | Full student table with per-student metrics |
| **Header** | App title + logout button |
| **Status Bar** | Feedback messages for recent actions |

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you'd like to change.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
