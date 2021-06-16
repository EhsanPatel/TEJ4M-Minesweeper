/*
 * Ehsan Patel & Lukas Krampitz
 * 14-Jun-2021
 * The JFrame to hold the main gameplay panel that will be accessed once a game is formed with two players
 */
package minesweepertej4m;

import javax.swing.JFrame;

public class gameScreenFrame extends JFrame {
    
    //constants for the window size
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;
    
    //stores the panel child component
    private gameScreenPanel theGameScreenPanel;
    
    //stores the Client Object that will transmit for this class
    private Client sweeperClient;
    
    /**
     * Constructor for this JFrame
     * @param c
     */
    public gameScreenFrame(Client c){
        sweeperClient = c;
        initFrame();
    }
    
    /**
     * Set up the JFrame
     */
    private void initFrame() {
        theGameScreenPanel = new gameScreenPanel(this, sweeperClient); //creates a new blank game
        setTitle("Multiplayer MineSweeper");
        setExtendedState(JFrame.MAXIMIZED_BOTH); //Fullscreen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(theGameScreenPanel); //adds it to the JFrame
        setVisible(true);
    }
}
