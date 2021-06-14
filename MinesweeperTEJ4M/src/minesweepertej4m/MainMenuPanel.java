/*
 * Lukas Krampitz
 * Jun 14, 2021
 * 
 */
package minesweepertej4m;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

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
        
        g2d.drawString("Java 2D", 50, 50);
        g2d.drawOval(0, 0, 100, 100);
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g); //prep the panel for drawing
        drawMenu(g); //draw the main menu
    }
    
}
