/*
 * Ehsan Patel
 * 14-Jun-2021
 * and open the template in the editor.
 */
package minesweepertej4m;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class gameScreenPanel extends JPanel{
    private gameScreenFrame gameScreenFrameRef;

    public gameScreenPanel(gameScreenFrame m) {
        gameScreenFrameRef = m;
    }
    
    
    
    private void drawMenu(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(88,165,100));
        g2d.fillRect(100,100,100,100);
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g); //prep the panel for drawing
        drawMenu(g); //draw the main menu
    }
}
