# hessMaster-Java

A comprehensive Chess Engine and GUI application built with Java 17. This project demonstrates advanced Object-Oriented Programming (OOP) principles, clean architecture, and the implementation of several key Software Design Patterns.

## Features

### Gameplay & Engine
* **Rule Enforcement:** Full validation for all chess pieces, including check/checkmate detection and threefold repetition.
* **AI Opponent:** Play against a computer engine that selects optimal legal moves.
* **Pawn Promotion:** Interactive selection for Queen, Rook, Bishop, or Knight when reaching the final rank.
* **Dual Interface:** Supports both a modern **Swing GUI** and a classic **CLI (Command Line)** mode.

### Account & Persistence
* **User Management:** Full Login/Registration system with point tracking and player statistics.
* **JSON Storage:** Game states and user accounts are persisted via JSON, allowing you to **Save and Resume** matches later.
* **Point System:** Earn points for capturing pieces and winning games using custom scoring strategies.

## esign Patterns Applied

| Pattern | Description |
| :--- | :--- |
| **Strategy** | Used for piece movements (`MoveStrategy`) and scoring logic (`ScoreStrategy`), allowing behaviors to be swapped at runtime. |
| **Factory** | Centralized piece creation via `PieceFactory` to handle complex object instantiation. |
| **Observer** | Implemented via `GameObserver` to update the UI and log game events without tight coupling. |
| **Singleton** | Ensures a single source of truth for the application state through `Main` and `Main2` controllers. |

##Tech Stack
* **Language:** Java 17+
* **GUI Framework:** Java Swing / AWT
* **Data Format:** JSON (via `json-simple`)
* **Architecture:** Strategy-based Move Validation

## Project Structure
* `cli/` & `gui/`: Application entry points and UI components.
* `game/`: Board management and core engine logic.
* `movesStrategies/`: Algorithms for piece-specific movements.
* `pieces/`: The Chess piece hierarchy and Factory class.
* `io/`: JSON reading/writing utilities for persistence.
* `model/`: Shared data structures (Positions, Enums, Pairs).

## Quick Start

### Prerequisites
* Java JDK 17 or higher.
* `json-simple-1.1.1.jar` in your library path.

### How to Run
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/ChessMaster-Java.git](https://github.com/yourusername/ChessMaster-Java.git)
