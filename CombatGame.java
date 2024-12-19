import java.awt.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*; 



/** The CombatGame class is the main control panel for the game. Here the game initialization,
 * rendering, input handling, and the game states, are managed. The class exdens JPanel, which enables
 * the game to have custom graphics rendering and implements the ActionListener to process
 * game updates (in regular intervals).
 * 
 * @author Adam Abdulmajid
 */

public class CombatGame extends JPanel implements ActionListener {
  
    // Fields
    private Timer timer;
    private GamePainter painter;
    private GameLogic gameLogic;
    private GenerateRooms generateRooms;
    private Room rooms;
    private BufferedImage backgroundImage;
    private BufferedImage[][] playerSprites;
    private int currentGameState = STATE_MENU;

    // Constants for dimensions and states
    private final int characterWidth = 200;
    private final int characterHeight = 200;
    public static final int[] FRAMES_PER_ACTION = {8, 8, 6, 2}; 
    static final int STATE_MENU = 0;
    static final int STATE_PLAYING = 1;

    /**
     * Constructor for CombatGame, initializing game components, loading images, 
     * and setting up controls and the game timer.
     */

    public CombatGame() {
        gameLogic = new GameLogic(this);
        painter = new GamePainter(this, gameLogic);
        generateRooms = new GenerateRooms();
     
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        loadImages();
        setupControls();
        
        timer = new Timer(1000 / 30, this);
        timer.start();
    }
    
    /**
     * Updates the game state at each game tick and repaints the screen.
     * 
     * @param e the ActionEvent triggered by the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        gameLogic.onGameTick();
        repaint();
    }

    /** Loads background and sprite images for every logical player/game state.
     * Tries reading the image files, and handles IOExceptions if file/files cannot be found.
    */
    private void loadImages() {
        try {
            backgroundImage = ImageIO.read(new File("ryustage (1).jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        playerSprites = new BufferedImage[4][];
        loadFrames("Idle.png", GameLogic.STATE_IDLE, FRAMES_PER_ACTION[GameLogic.STATE_IDLE]);
        loadFrames("Run.png", GameLogic.STATE_RUN, FRAMES_PER_ACTION[GameLogic.STATE_RUN]);
        loadFrames("Attack1.png", GameLogic.STATE_ATTACK, FRAMES_PER_ACTION[GameLogic.STATE_ATTACK]);
        loadFrames("Jump.png", GameLogic.STATE_JUMP, FRAMES_PER_ACTION[GameLogic.STATE_JUMP]);
    }

    /** Loads individual frames from a sprite sheet for specific player action.
     * 
     * @param fileName the name of the file containing the sprite sheet for specfic action.
     * @param stateIndex the index representing the player's state/action. 
     * @param frameCount the number of frames in the sprite sheet for the action.
     * @throws IOException if the file cannot be found.
     */
    private void loadFrames(String fileName, int stateIndex, int frameCount) {
        try {
            BufferedImage sheet = ImageIO.read(new File(fileName));
            playerSprites[stateIndex] = new BufferedImage[frameCount];
            for (int i = 0; i < frameCount; i++) {
                playerSprites[stateIndex][i] = sheet.getSubimage(i * characterWidth, 0, characterWidth, characterHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Sets up keyboard controls for menu and gameplay.
     */
    private void setupControls() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (currentGameState == STATE_MENU) {
                    handleMenuInput(e.getKeyCode());
                } else if (currentGameState == STATE_PLAYING) {
                    gameLogic.handlePlayerInput(e.getKeyCode(), true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (currentGameState == STATE_PLAYING) {
                    gameLogic.handlePlayerInput(e.getKeyCode(), false);
                }
            }
        });
    }

    /**  Handles menu inputs to start or exit the game.
     * @param key the key value of the pressed key
     */
    private void handleMenuInput(int key) {
        if (key == KeyEvent.VK_ENTER) {
            currentGameState = STATE_PLAYING;
        } else if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

/** Renders graphical game components to the game.
 * @param g the graphics object used for drawin components
 */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        painter.paintComponent(g);
    }

    /**Retrieves the currect game state (menu, or playing)
     * 
     * @return current game state (as an integer).
     */
    public int getCurrentGameState() {
        return currentGameState;
    }
    /** Gets the background image used in the game
     * 
     * @return the background image (as BufferedImage).
     */
    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

     /** Gets the player sprites.
      * 
      * @return a 2d array of bufferedimage objects representing player sprites (the rows = which state, the colomns = which frame)
      */
    public BufferedImage[][] getPlayerSprites() {
        return playerSprites;
    }
    /** 
     * @return the width of the character (total number of pixels represents the integer).
     */
    public int getCharacterWidth() {
        return characterWidth;
    }
    /**
     * @return the height of the character.
     */
    public int getCharacterHeight() {
        return characterHeight;
    }


/**
 *  The main method to start the game. It loads up the room data, initializes the explorepreface section
 * of the game, and if successful, starts the main game window.
 * @param args command-line arguments (not used.)
 */
    public static void main(String[] args) {
        Map<String, Room> rooms = GenerateRooms.generateRoomsFromFile("Rooms.txt");

        if (GenerateRooms.explorePreface(rooms)) {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Arena");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                CombatGame game = new CombatGame();
                frame.add(game);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
        }
    }
}


