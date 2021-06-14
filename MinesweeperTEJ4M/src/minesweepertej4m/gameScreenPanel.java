/*
 * Ehsan Patel
 * 14-Jun-2021
 * and open the template in the editor.
 */
package minesweepertej4m;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class gameScreenPanel extends JPanel{
    private gameScreenFrame gameScreenFrameRef;
    private Image settings;
    
    public gameScreenPanel(gameScreenFrame m) {
        gameScreenFrameRef = m;
    }
    
    
    
    private void drawStaticComponents(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(88,165,100));
        g2d.fillRect(0,0,getWidth(),getHeight());
        g2d.setColor(new Color(255,255,255));
        g2d.fillRect(0,getHeight()-100,getWidth(),100);
//        g2d.drawImage(NAV_SETTINGS, 0, 0, this);
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g); //prep the panel for drawing
        drawStaticComponents(g); //draw the main menu
    }
    
}
