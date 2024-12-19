# Combat Game: Arena

**Combat Game: Arena** is a hybrid text-based and graphical combat game that combines room exploration with dynamic player-versus-player battles. Players navigate through a series of text-described rooms, culminating in a high-action combat scene powered by intuitive controls and visual effects.

## Features

- **Text-Based Exploration**: Explore rooms, read their descriptions, and navigate through different paths.
- **Graphical Combat**: Engage in combat using animated sprites, health bars, and dynamic physics.
- **Room Generation**: Rooms are loaded dynamically from a text file and feature directional navigation.
- **Win Tracking**: Tracks and logs wins for each player across sessions.
- **Customizable Sprites**: Player animations include idle, run, jump, and attack actions.

## Gameplay

1. Navigate through rooms using commands like "north," "south," "log" (to view win stats), or "challenger" to initiate combat.
2. Once combat begins:
   - **Player 1**: Use `W` (jump), `A` (left), `D` (right), and `S` (attack).
   - **Player 2**: Use arrow keys (`↑`, `←`, `→`, `↓`) for the same actions.
3. Combat ends when one player's health reaches zero.

## File Structure

- **`CombatGame.java`**: Main game logic, rendering, and input handling.
- **`GameLogic.java`**: Manages physics, player interactions, and win conditions.
- **`GamePainter.java`**: Handles graphical rendering of the game state.
- **`GenerateRooms.java`**: Manages room generation and exploration.
- **`FileWordSplitter.java`**: Reads text files for analysis and logging.
- **Assets**: Backgrounds, sprite sheets, and log files (e.g., `Rooms.txt`, `Wins.txt`).

## Setup

### Prerequisites

- Java 8 or higher
- IDE or command-line setup to compile and run Java programs

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your_username/CombatGame.git
   cd CombatGame
   ```
2. Ensure assets (e.g., `Rooms.txt`, sprite images) are in the correct directory.

### Running the Game

1. Compile the program:
   ```bash
   javac *.java
   ```
2. Start the game:
   ```bash
   java CombatGame
   ```

## Controls

- **Text-Based Exploration**:
  - `log`: View player win counts.
  - `north`, `south`, `east`, `west`: Navigate rooms.
- **Combat**:
  - Player 1: `W` (jump), `A` (left), `D` (right), `S` (attack).
  - Player 2: Arrow keys (`↑`, `←`, `→`, `↓`) for the same actions.

## Future Enhancements

- Add AI opponents for single-player mode.
- Implement a scoring system and game modes.
- Introduce new animations and combat mechanics.


