Arkanoid Game - Celestial Resonance - Object-Oriented Programming Project
---

Author
---

Group 2 - Class INT2204 11

1. Nguyễn Thùy Trang - 24021644
2. Nguyễn Minh Anh - 24021370
3. Phan Yến Nhi - 24022705
4. Trịnh Thị Vân - 24022842

Instructor: Kiều Văn Tuyên
Semester: HK1 - 2025
---

Description
---
This is a classic Arkanoid game developed in Java as a final project for Object-Oriented Programming course. The project demonstrates the implementation of OOP principles and design patterns.

Key feature:
1. The game is developed using Java 21+ with JavaFX for GUI.

2. Implements core OOP principles: Encapsulation, Inheritance, Polymorphism, and Abstraction.

3. Applies multiple design patterns: 
- Factory: BrickFactory 
- Behavior Pattern: GameLoop
- Facade: World, MainMenu, LevelSelect
- Strategy: Storage, ScoringSystem, ThemeManager
- State: MainMenu, LevelSelect, Setting, InGame, End
- Observer: EventBus, MainMenu, LevelSelect, Setting, End, Shop
- Command: InGame
- Singleton: AudioSystem, ScoringSystem, JSonStorage, SQLiteStorage, ThemeManager
- Object Pool: PowerUpPool
- Reposity Pattern: PlayerRepository, ScoreRepository
- Builder: ButtonUI
- MVC: Game, World, ui/screen/*
- Logger: Logger, ErrorHandle

4. Features multithreading for smooth gameplay and responsive UI.
- Luồng Game Loop 60 fps -> GameLoop
- Luồng Render -> Game, GameLoop
- Thread âm thanh -> AudioSystem
- I/O -> Storage, Game, MockServer


Game mechanics:
- 

- 

- 

...
---

UML Diagram

...
---

Design Patterns Implementation
1. ...
Used in :
Purpose :
---

**Multithreading Implementation**

...
---

**Installation**
1. Clone the project from the repository.
2. Open the project in the IDE.
3. Run the project.

**Usage**

**Control**
...

**How to Play**
1. 
2. 
3. 
...

**Power-ups**
...

**Scoring System**
...
---

**Demo**
...
---

**Future Improvements**
...
---

**Technologies Used**
...
---

**License**
... 
---

**Notes**
...
---

*Last update: 12/11/2025*

