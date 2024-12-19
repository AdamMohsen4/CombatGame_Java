import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
 

/** GameLogic manages the user inputs, their corresponding animation states, game physics,
 *interactions between the players, health tracking and win conditions (everytime GameTick is called in the CombatGame class)
 * It is also here that each win is updateted/logged on a txt file (Wins.txt)
 * 
 *  @author Adam Abdulmajid
 */

public class GameLogic {
    private CombatGame game;

    // fields
    private int player1Health = 100;
    private int player2Health = 100;
    private double player1VelocityY = 0;
    private double player2VelocityY = 0;
    private int player1X = 50, player2X = 400;
    private int player1Y = 340, player2Y = 340;
    private boolean player1MovingLeft, player1MovingRight;
    private boolean player2MovingLeft, player2MovingRight;
    private boolean player1Jumping, player2Jumping;
    private boolean player1Attacking, player2Attacking;
    private boolean player1FacingRight = true;
    private boolean player2FacingRight = true;
    private int player1State = STATE_IDLE;
    private int player2State = STATE_IDLE;

    private int player1Frame = 0;
    private int player2Frame = 0;

    private final double gravity = 0.4;
    private final double jumpSpeed = 8;
    private final int baseDamage = 10;
    private final int groundLevel = 340;
    private boolean gameEnded = false;
    private String resultMessage = "";

    
    static final int STATE_IDLE = 0;
    static final int STATE_RUN = 1;
    static final int STATE_ATTACK = 2;
    static final int STATE_JUMP = 3;

    /** Constructs a GameLogic instance alongside the CombatGame instance for managing the graphical game interactions
     * 
     * @param game the CombantGame instance this logic belongs to
     */
    public GameLogic(CombatGame game) {
        this.game = game;
    }
    /** Process the player inputs to control different movements (single keys or combinations)
     * 
     * @param key the key code representing the action
     * @param pressed true if the key is pressed
     */
    public void handlePlayerInput(int key, boolean pressed) {
        if (!gameEnded) {
            switch (key) {
                case KeyEvent.VK_A: // Player 1 move left
                    player1MovingLeft = pressed;
                    if (pressed) {
                        player1FacingRight = false; // Player 1 Face left
                    }
                    if (!player1Jumping) {
                        updatePlayerState(1);
                    }
                    break;
                case KeyEvent.VK_D: // Player 1 move right
                    player1MovingRight = pressed;
                    if (pressed) {
                        player1FacingRight = true; // Player 1 Face right
                    }
                    if (!player1Jumping) {
                        updatePlayerState(1);
                    }
                    break;
                case KeyEvent.VK_W: // Player 1 jump
                    if (pressed) {
                        startJump(1);
                    }
                    break;
                case KeyEvent.VK_S: // Player 1 attack
                    if (pressed) {
                        player1Attacking = true;
                        player1State = STATE_ATTACK;
                    }
                    else{
                        player1Attacking = false;
                        player1State = STATE_IDLE;
                    }
                    break;
                case KeyEvent.VK_LEFT: // Player 2 move left
                    player2MovingLeft = pressed;
                    if (pressed) {
                        player2FacingRight = false; // Player 2 Face left
                    }
                    if (!player2Jumping) {
                        updatePlayerState(2);
                    }
                    break;
                case KeyEvent.VK_RIGHT: // Player 2 move right
                    player2MovingRight = pressed;
                    if (pressed) {
                        player2FacingRight = true; // Player 2 Face right
                    }
                    if (!player2Jumping) {
                        updatePlayerState(2);
                    }
                    break;
                case KeyEvent.VK_UP: // Player 2 jump
                    if (pressed) {
                        startJump(2);
                    }
                   
                    break;
                case KeyEvent.VK_DOWN: // Player 2 attack
                    if (pressed) {
                        player2Attacking = true;
                        player2State = STATE_ATTACK;
                    }
                  else{
                        player2Attacking = false;
                        player2State = STATE_IDLE;
                    }
                    break;
            }
        }
    }
        
    
    /**
     * Integrates physics calculations for each player (gravity while jumping)
     */
    public void applyPhysics() {
        applyJumpingPhysics(1);
        applyJumpingPhysics(2);
    }
    
    /** The jumping physics to the players; updating their position based on gravity and checking if they have landed
     * 
     * @param player the player number (1 or 2).
     */
    private void applyJumpingPhysics(int player) {
        double velocityY = (player == 1) ? player1VelocityY : player2VelocityY;
        int playerY = (player == 1) ? player1Y : player2Y;
        boolean jumping = (player == 1) ? player1Jumping : player2Jumping;
    
        if (jumping) {
            // Update position and velocity
            playerY += velocityY;
            velocityY += gravity;
    
            // Check if the player has landed
            if (playerY >= groundLevel) {
                playerY = groundLevel;
                velocityY = 0; // Reset velocity upon landing
                jumping = false; // End jumping state

                // Update state based on movement
                if ((player == 1 && (player1MovingLeft || player1MovingRight)) || 
                    (player == 2 && (player2MovingLeft || player2MovingRight))) {
                    updatePlayerState(player);
                } 
                else {
                    if (player == 1) {
                        player1State = STATE_IDLE;
                    } else {
                        player2State = STATE_IDLE;
                    }
                }
            }

            // Update player-specific variables
            if (player == 1) {
                player1Y = playerY;
                player1VelocityY = velocityY;
                player1Jumping = jumping;
            } else {
                player2Y = playerY;
                player2VelocityY = velocityY;
                player2Jumping = jumping;
            }
        }
    }
        /**
         * Initiate the jumping action for a player (if not already jumping).
         * @param player the player number 
         */
    public void startJump(int player) {
        if (player == 1 && !player1Jumping) {
            player1Jumping = true;
            player1VelocityY = -jumpSpeed;
            player1State = STATE_JUMP;
        } else if (player == 2 && !player2Jumping) {
            player2Jumping = true;
            player2VelocityY = -jumpSpeed;
            player2State = STATE_JUMP;
        }
    }
    
          /**
           * Updates the position on the y-axis based on player movement
           */
    public void updatePositions() {
        if (player1MovingLeft) player1X -= 5;
        if (player1MovingRight) player1X += 5;
        if (player2MovingLeft) player2X -= 5;
        if (player2MovingRight) player2X += 5;
    }

    /**
     * Checks if two players are colliding based on their cordinates
     * 
     * @param x1 the x-coordinate for player 1
     * @param y1 the y-coordinate for player 1
     * @param x2 the x-coordinate for player 2
     * @param y2 the y coordinate for player 2
     * @return true if the players are colliding
     */
    private boolean isColliding(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) < game.getCharacterWidth() && Math.abs(y1 - y2) < game.getCharacterHeight();
    }
    /**
     * Applying damage if players are attacing and within attacing range. Uses isColliding method.
     */
    public void handleAttacks() {
        if (player1Attacking && isColliding(player1X, player1Y, player2X, player2Y)) {
            player2Health -= baseDamage;
            player1Attacking = false;
        }
        if (player2Attacking && isColliding(player2X, player2Y, player1X, player1Y)) {
            player1Health -= baseDamage;
            player2Attacking = false;
        }
    }


    /**
     * Checks the "gameOver" condition based on player health. Logs the game result if either player's health reacher 0
     */
    public void checkGameOver() {
       if (gameEnded) return;


        if (player1Health <= 0) {
            gameEnded = true;
            resultMessage = "Player 2 Wins!";
            appendWinToFile(resultMessage);

        } else if (player2Health <= 0) {
            gameEnded = true;
            resultMessage = "Player 1 Wins!";
            appendWinToFile(resultMessage);
            
        }
    
       
    }

    /**
     * Appends the game results to the Wins.txt file
     * 
     * @param message the game result message to log
     */
    private void appendWinToFile(String message){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("Wins.txt",true))){
            writer.write(message);
            writer.newLine();

        }catch(IOException e){
            e.printStackTrace();
          }
    }

    /**
     * Updates the state of a specific player based on their current action
     * @param player the player number
     */
    private void updatePlayerState(int player) {
        boolean movingLeft = (player == 1) ? player1MovingLeft : player2MovingLeft;
        boolean movingRight = (player == 1) ? player1MovingRight : player2MovingRight;
        boolean jumping = (player == 1) ? player1Jumping : player2Jumping;
        int state = STATE_IDLE;

        if (jumping) {
            state = STATE_JUMP;
        } else if (movingLeft || movingRight) {
            state = STATE_RUN;
        }

        if (player == 1) {
            player1State = state;
        } else {
            player2State = state;
        }
    }

    /**
     * Updates the animation frame (sprite) based on the action of each player.
     */
    public void updateFrames() {
        player1Frame = (player1Frame + 1) % game.FRAMES_PER_ACTION[player1State];
        player2Frame = (player2Frame + 1) % game.FRAMES_PER_ACTION[player2State];
    }

    /**
     * Called on each game tick to update game logics
     */
    public void onGameTick() {
        applyPhysics();
        updatePositions();
        handleAttacks();
        updateFrames();
        checkGameOver();
    }

    // Getters for player properties and game state
    public int getPlayer1X() { return player1X; }
    public int getPlayer1Y() { return player1Y; }
    public int getPlayer1State() { return player1State; }
    public int getPlayer1Frame() { return player1Frame; }
    public boolean isPlayer1FacingRight() { return player1FacingRight; }
    public int getPlayer2X() { return player2X; }
    public int getPlayer2Y() { return player2Y; }
    public int getPlayer2State() { return player2State; }
    public int getPlayer2Frame() { return player2Frame; }
    public boolean isPlayer2FacingRight() { return player2FacingRight; }
    public int getPlayer1Health() { return player1Health; }
    public int getPlayer2Health() { return player2Health; }
    public boolean isGameEnded() { return gameEnded; }
    public String getResultMessage() { return resultMessage; }
}
