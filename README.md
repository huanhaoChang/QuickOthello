# QuickOthello

A modern Android implementation of the classic Othello (Reversi) board game built with Jetpack Compose and following clean architecture principles.

## Features

### Game Features
- **Multiple Board Sizes**: Play on 4x4, 6x6, or 8x8 boards
- **Game Modes**:
    - Human vs Human
    - Human vs Computer (AI opponent)
- **Smart AI**: Computer player uses minimax algorithm with alpha-beta pruning
- **Valid Move Indicators**: Visual hints showing where you can place your disks
- **Real-time Score Tracking**: See the current disk count for each player
- **Responsive Design**: Optimized layouts for both portrait and landscape orientations

### Technical Features
- Built with **Jetpack Compose** for modern declarative UI
- **Clean Architecture** with separation of concerns
- **Dependency Injection** using Hilt
- **MVVM Pattern** with ViewModels and StateFlow
- **Coroutines** for asynchronous operations
- **Material Design 3** for beautiful UI
- Persistent settings using SharedPreferences

## Screenshots

| Portrait Mode | Landscape Mode |
|--------------|----------------|
| 📱 Game board optimized for vertical screens | 🖥️ Game board optimized for horizontal screens |

## Architecture

The app follows clean architecture principles with the following layers:

```
┌─────────────────────────────────────┐
│         UI Layer (Compose)          │
├─────────────────────────────────────┤
│            ViewModel                │
├─────────────────────────────────────┤
│            Use Cases                │
├─────────────────────────────────────┤
│           Repository                │
├─────────────────────────────────────┤
│     Data Sources (Game Logic)      │
└─────────────────────────────────────┘
```

### Key Components

- **UI Layer**: Compose screens and components
- **ViewModels**: Manage UI state and handle user interactions
- **Use Cases**: Encapsulate business logic
- **Repositories**: Abstract data sources
- **Game Logic**: Core game rules and AI implementation

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Asynchronous Programming**: Coroutines + Flow
- **Build System**: Gradle with Kotlin DSL

## Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 21
- Android SDK 35 (Android 15)
- Minimum Android device/emulator with API 28 (Android 9.0)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/QuickOthello.git
```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Run the app on an emulator or physical device

## How to Play

### Game Rules
1. The game starts with 2 black and 2 white disks in the center
2. Black always moves first
3. Place a disk to capture opponent's disks between your new disk and existing disks
4. You can only place a disk where it captures at least one opponent disk
5. If you can't make a valid move, your turn is skipped
6. The game ends when neither player can move
7. The player with the most disks wins!

### Controls
- **Tap** on a highlighted cell to place your disk
- **Menu Button** (☰) to access game settings
- **New Game** to restart with current settings
- **Board Size** selection: 4x4, 6x6, or 8x8
- **Game Mode** selection: Human vs Human or Human vs Computer

## Project Structure

```
app/
├── src/main/java/jp/co/nissan/ae/quickothello/
│   ├── di/                    # Dependency injection modules
│   ├── model/                 # Data models and game logic
│   ├── repository/            # Data repositories
│   ├── usecase/              # Business logic use cases
│   ├── ui/                   # UI components and screens
│   │   ├── screens/          # Compose screens
│   │   └── theme/            # Material theme
│   ├── viewmodel/            # ViewModels
│   ├── MainActivity.kt       # Main activity
│   └── OthelloApplication.kt # Application class
├── src/main/res/             # Resources
└── build.gradle.kts          # Module build configuration
```

## AI Implementation

The computer player uses a minimax algorithm with:
- **Alpha-beta pruning** for performance optimization
- **Position evaluation** based on:
    - Disk count difference
    - Strategic position values (corners and edges)
    - Mobility (number of available moves)
- **Adjustable search depth** (currently set to 3)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

### Development Guidelines
1. Follow Kotlin coding conventions
2. Write meaningful commit messages
3. Add tests for new features
4. Update documentation as needed

## Future Enhancements

- [ ] Online multiplayer support
- [ ] Different AI difficulty levels
- [ ] Game history and statistics
- [ ] Undo/Redo functionality
- [ ] Custom themes and board colors
- [ ] Sound effects and animations
- [ ] Tournament mode

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Inspired by the classic Othello/Reversi game
- Built with Android Jetpack libraries
- Material Design 3 for UI components

## Contact

For questions or suggestions, please open an issue on GitHub.

---

**Enjoy playing QuickOthello!** 🎮