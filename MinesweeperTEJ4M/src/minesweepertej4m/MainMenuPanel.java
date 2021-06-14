/*
 * Lukas Krampitz
 * Jun 14, 2021
 * 
 */
package minesweepertej4m;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import static resources.ResourcesRef.*;

/**
 *
 * @author Tacitor
 */
public class MainMenuPanel extends JPanel{
    
    private MainMenuFrame mainMenuFrameRef;

    public MainMenuPanel(MainMenuFrame m) {
        mainMenuFrameRef = m;
        
        
    }
    
    
    
    private void drawMenu(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        //add the background image
        g2d.drawImage(MAIN_MENU_BACKGROUND, 0, 0, null);
        
        //add the button images
        //add the create button
        g2d.drawImage(MAIN_MENU_CREATE, 
                super.getWidth() / 2 - (MAIN_MENU_CREATE.getWidth(null) / 2), 
                super.getHeight()/ 2, null);
        
        //add the join button
        g2d.drawImage(MAIN_MENU_JOIN, 
                super.getWidth() / 2 - (MAIN_MENU_CREATE.getWidth(null) / 2), 
                super.getHeight()/ 2 + (MAIN_MENU_CREATE.getHeight(null)) + 10, null);
        
        //add the settings button
        g2d.drawImage(MAIN_MENU_JOIN, 
                super.getWidth() / 2 - (MAIN_MENU_CREATE.getWidth(null) / 2), 
                super.getHeight()/ 2 + (MAIN_MENU_CREATE.getHeight(null) * 2) + (10 * 2), null);
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g); //prep the panel for drawing
        drawMenu(g); //draw the main menu
    }
    
}
