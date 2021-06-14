/*
 * Ehsan Patel
 * 14-Jun-2021
 * and open the template in the editor.
 */
package minesweepertej4m;

import javax.swing.JFrame;

public class gameScreenFrame extends JFrame {
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;
    
    private gameScreenPanel theGameScreenPanel;
    
    /**
     * Constructor
     */
    public gameScreenFrame(){
        initFrame();
    }
    
    /**
     * Set up the JFrame
     */
    private void initFrame() {
        theGameScreenPanel = new gameScreenPanel(this); //creates a new blank game
        setTitle("Multiplayer MineSweeper");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(theGameScreenPanel); //adds it to the JFrame
        setVisible(true);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Open the main menu
        gameScreenFrame g = new gameScreenFrame();
        
    }
}
