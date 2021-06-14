/*
 * Ehsan Patel
 * 14-Jun-2021
 * and open the template in the editor.
 */
package minesweepertej4m;

import javax.swing.JFrame;

public class gameScreenFrame extends JFrame {
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
        setTitle("Multiplayer MineSweeper");
        setSize(1920, 1080);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        theGameScreenPanel = new gameScreenPanel(this); //creates a new blank game
        add(theGameScreenPanel); //adds it to the JFrame
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

}
