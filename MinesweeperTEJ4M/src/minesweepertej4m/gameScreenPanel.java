/*
 * Ehsan Patel & Lukas Krampitz
 * 14-Jun-2021
 * The JPanel that will be placed on the main game screen JFrame that will hold the logic for the gameplay
 */
package minesweepertej4m;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import static resources.ResourcesRef.*;

public class gameScreenPanel extends JPanel{
    //the parent frame
    private gameScreenFrame gameScreenFrameRef;
    
    /**
     * constructor for this panel to be drawn
     */
    public gameScreenPanel(gameScreenFrame m) {
        //stores the parent frame passed in
        gameScreenFrameRef = m;
    }
    
    
    
    private void drawStaticComponents(Graphics g) {
        //The graphics model to use
        Graphics2D g2d = (Graphics2D) g;
        //changes the drawing color to green
        g2d.setColor(new Color(88,165,100));
        //draws the background on the panel
        g2d.fillRect(0,0,getWidth(),getHeight());
        //changes the color to white
        g2d.setColor(new Color(255,255,255));
        //draws the navigation bar
        g2d.fillRect(0,getHeight()-100,getWidth(),100);
        
        //draws all the images for buttons to be used
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
