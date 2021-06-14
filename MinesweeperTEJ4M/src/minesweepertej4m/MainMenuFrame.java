/*
 * Ehsan Patel & Lukas Krampitz
 * 14-Jun-2021
 * The main menu of the MineSweeper Multiplayer Game.
 */
package minesweepertej4m;

import javax.swing.JFrame;

/**
 *
 * @author ehsan
 */
public class MainMenuFrame extends JFrame {
    
    private MainMenuPanel theMainMenuPanel;
    
    /**
     * Constructor
     */
    public MainMenuFrame(){
        initFrame();
    }
    
    /**
     * Set up the JFrame
     */
    private void initFrame() {
        setTitle("Multiplayer MineSweeper");
        setSize(1920, 1080);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        theMainMenuPanel = new MainMenuPanel(this); //creates a new blank game
        add(theMainMenuPanel); //adds it to the JFrame
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Open the main menu
        MainMenuFrame mainMenuFrame = new MainMenuFrame();
        
    }

    

}
