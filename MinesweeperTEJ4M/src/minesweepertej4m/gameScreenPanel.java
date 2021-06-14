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
import static resources.ResourcesRef.*;

public class gameScreenPanel extends JPanel{
    private gameScreenFrame gameScreenFrameRef;
    
    public gameScreenPanel(gameScreenFrame m) {
        gameScreenFrameRef = m;
    }
    
    
    
    private void drawStaticComponents(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(88,165,100));
        g2d.fillRect(0,0,getWidth(),getHeight());
        g2d.setColor(new Color(255,255,255));
        g2d.fillRect(0,getHeight()-100,getWidth(),100);
        g2d.drawImage(NAV_SETTINGS, 0, getHeight()-90, this);
        g2d.drawImage(NAV_SCOUT, (getWidth()/2)-(NAV_SCOUT.getWidth(this)/2)-100, getHeight()-90, this);
        g2d.drawImage(NAV_FLAG, (getWidth()/2)-(NAV_FLAG.getWidth(this)/2), getHeight()-90, this);
        g2d.drawImage(NAV_PLACE, (getWidth()/2)-(NAV_PLACE.getWidth(this)/2)+100, getHeight()-90, this);
        g2d.drawImage(NAV_QUIT, getWidth()-320, getHeight()-90, this);
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g); //prep the panel for drawing
        drawStaticComponents(g); //draw the main menu
    }
    
}
