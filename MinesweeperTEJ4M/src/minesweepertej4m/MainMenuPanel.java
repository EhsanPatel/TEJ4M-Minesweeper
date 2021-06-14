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
        g2d.drawImage(MAIN_MENU_CREATE, 
                super.getWidth() / 2 - (MAIN_MENU_CREATE.getWidth(this) / 2), 
                super.getHeight()/ 2, null);
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g); //prep the panel for drawing
        drawMenu(g); //draw the main menu
    }
    
}
