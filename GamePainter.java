import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;



/** GamePainter is responsible for rendering the visual compontents of the game.
 * The classes CombatGame and GameLogic are integrated with this class to draw the various
 * game states and dynamically update the visuals based on the game actions and logics.  
 * 
 * @author Adam Abdulmajid
 */

public class GamePainter {

    private CombatGame game;
    private GameLogic gameLogic;

    /**
     * Constructs a GamePainter, referencing to CombatGame and GameLogic inform necessary rendering.
     * @param game the CombatGame instance.
     * @param gameLogic the GameLogic instance.
     */
    public GamePainter(CombatGame game, GameLogic gameLogic) {
        this.game = game;
        this.gameLogic = gameLogic;
    }

     /** Paints the game components based on current game state.
      * @param g the graphics object.
      */
    public void paintComponent(Graphics g) {
        if (game.getCurrentGameState() == CombatGame.STATE_MENU) {
            drawMenu(g);
        } else if (game.getCurrentGameState() == CombatGame.STATE_PLAYING) {
            drawBackground(g);
            drawHealthBars(g);
            drawSprites(g);
            drawHUD(g);
        }
    }

    /** Draws all the aspects of the menu
     * 
     * @param g the graphics object
     */
    private void drawMenu(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, game.getWidth(), game.getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Arena", game.getWidth() / 2 -60, 150);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("Press ENTER to Start", game.getWidth() / 2 - 130, 300);
        g.drawString("Press ESC to Exit", game.getWidth() / 2 - 110, 350);
    }

    /** Draws the background image on the window
     * 
     * @param g the graphics object
     */
    private void drawBackground(Graphics g) {
        BufferedImage backgroundImage = game.getBackgroundImage();
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, game.getWidth(), game.getHeight(), null);
        }
    }

    /** Draws the health bars for both players.
     * 
     * @param g
     */
    private void drawHealthBars(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(50, 20, gameLogic.getPlayer1Health()*2, 25);
        g.fillRect(550, 20, gameLogic.getPlayer2Health()*2,25);
    }

    /**
     * Draws the player sprites based on their current position, state and the direction they are facing.
     * @param g
     */
    private void drawSprites(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage[][] playerSprites = game.getPlayerSprites();
        double scaleFactor = 3;
        int characterWidth = game.getCharacterWidth();
        int characterHeight = game.getCharacterHeight();
        int scaledWidth = (int) (characterWidth * scaleFactor);
        int scaledHeight = (int) (characterHeight * scaleFactor);

        // Player 1
        int player1X = gameLogic.getPlayer1X();
        int player1Y = gameLogic.getPlayer1Y();
        int player1State = gameLogic.getPlayer1State();
        int player1Frame = gameLogic.getPlayer1Frame();
       
        boolean player1FacingRight = gameLogic.isPlayer1FacingRight();

        if (player1FacingRight) {
          
            g2d.drawImage(playerSprites[player1State][player1Frame], player1X, player1Y - characterHeight, scaledWidth, scaledHeight, null);

        } else {
           
            g2d.drawImage(playerSprites[player1State][player1Frame], player1X + scaledWidth, player1Y - characterHeight, -scaledWidth, scaledHeight, null);
        }

        //  Player 2
        int player2X = gameLogic.getPlayer2X();
        int player2Y = gameLogic.getPlayer2Y();
        int player2State = gameLogic.getPlayer2State();
        int player2Frame = gameLogic.getPlayer2Frame();
       
        boolean player2FacingRight = gameLogic.isPlayer2FacingRight();

        if (player2FacingRight) {
            g2d.drawImage(playerSprites[player2State][player2Frame], player2X, player2Y - characterHeight, scaledWidth, scaledHeight, null);
        } else {
          
            g2d.drawImage(playerSprites[player2State][player2Frame], player2X + scaledWidth, player2Y - characterHeight, -scaledWidth, scaledHeight, null);
        }
    }

    /**
     * Draws the HUD (HeadsUpDisplay), i.e. the player names, if the game has ended, and game results.
     * @param g
     */
    private void drawHUD(Graphics g) {
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Player 1", 50, 15);
        g.drawString("Player 2", 550, 15);
        if (gameLogic.isGameEnded()) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString(gameLogic.getResultMessage(), game.getWidth() / 2 - 100, game.getHeight() / 2);
        }
    }
}
