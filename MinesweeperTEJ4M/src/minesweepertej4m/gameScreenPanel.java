/*
 * Ehsan Patel & Lukas Krampitz
 * 14-Jun-2021
 * The JPanel that will be placed on the main game screen JFrame that will hold the logic for the gameplay
 */
package minesweepertej4m;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import static resources.ResourcesRef.*;

public class gameScreenPanel extends JPanel implements ActionListener, MouseMotionListener {
    //the parent frame
    private gameScreenFrame gameScreenFrameRef;
    private static int[][] buttons;
    private static boolean firstFrame = true;
    
    /**
     * constructor for this panel to be drawn
     */
    public gameScreenPanel(gameScreenFrame m) {
        //stores the parent frame passed in
        gameScreenFrameRef = m;
        
        addMouseMotionListener(this);
        
        //allows the board to recieve input from mouseclicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                checkButtonHits(e.getX(),e.getY());
            }

        });
        
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
        
        Image[] imagesToDraw = {NAV_SETTINGS, NAV_SCOUT, NAV_FLAG, NAV_PLACE, NAV_QUIT};
        for(int i = 0; i < buttons.length; ++i){
            g2d.drawImage(imagesToDraw[i], buttons[i][0], buttons[i][1], this);
        }
    }
    
    
    
    private void checkButtonHits(int x, int y){
        int buttonHit = -1;
        for(int i = 0; i < buttons.length; ++i){
            if(x > buttons[i][0] && x < buttons[i][0] + buttons[i][2]
                && y > buttons[i][1] && y < buttons[i][1] + buttons[i][3]){
                buttonHit = i;
                break;
            }
        }

        switch(buttonHit){
            case 0:
                System.out.println("Settings");
                break;
            case 1:
                System.out.println("Scout Bomb");
                break;
            case 2:
                System.out.println("Flag Bomb");
                break;
            case 3:
                System.out.println("Place Bomb");
                break;
            case 4:
                System.exit(0);

        }
                
            //switch to complete different functions
        
    }
    
    
    
    
    @Override
    public void paintComponent(Graphics g){
        if(firstFrame){
            buttons = new int[][]{
                {0, getHeight()-90, 467, 85}, //Settings
                {(getWidth()/2)-(NAV_SCOUT.getWidth(this)/2)-100, getHeight()-90, 80, 80}, //Scout
                {(getWidth()/2)-(NAV_FLAG.getWidth(this)/2), getHeight()-90, 57, 80}, //Flag
                {(getWidth()/2)-(NAV_PLACE.getWidth(this)/2)+100, getHeight()-90, 80, 80}, //Place
                {getWidth()-320, getHeight()-90, 296, 80} //Quit
            };
            firstFrame = false;
        }
        super.paintComponent(g); //prep the panel for drawing
        drawStaticComponents(g); //draw the main menu
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
//        System.out.println(e.getX() + ":" + e.getY());
    }

    public void mouseDragged(MouseEvent e) {
//        System.out.println(e.getX() + ":" + e.getY());
    }
}
