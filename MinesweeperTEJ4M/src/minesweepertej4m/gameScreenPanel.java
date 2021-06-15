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
    
    //panel variables
    private static int[][] buttons;
    private static boolean firstFrame = true;
    
    //game variables
    private int turn = 0;
    private int turnCounter = 0;
    //board format [whos board][x][y][covered, is bomb, # surrounding, is flagged]
    private int[][][][] boards = new int[2][10][10][4];
    private String currentAction = "scout";
    
    
    /**
     * Constructor for this panel to be drawn
     */
    public gameScreenPanel(gameScreenFrame m) {
        //stores the parent frame passed in
        gameScreenFrameRef = m;
        
        //recieves mouse motion input
        addMouseMotionListener(this);
        
        //allows the board to recieve input from mouseclicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                checkButtonHits(e.getX(),e.getY());
                if(gridHit(e.getX(),e.getY())){
                    if(turn == 0){
                        boards = getLocalMove(e.getX(),e.getY());
                    }
                    if(turn == 1){
                        //to be switched to network move
                        boards = getLocalMove(e.getX(),e.getY());
                    }
                }
            }

        });
        
    }
    
    private void drawDynamicComponents(Graphics g) {
        //The graphics model to use
        Graphics2D g2d = (Graphics2D) g;
        
        //draw boards on screen
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
        
        
        //draws the grid background
        int margin = 50;
        g2d.fillRect((getWidth()/2)-(500+margin),(getHeight()/2)-250,500,500);
        g2d.fillRect((getWidth()/2)+margin,(getHeight()/2)-250,500,500);
        
        //draws all the images for buttons to be used
        
        Image[] imagesToDraw = {NAV_SETTINGS, NAV_SCOUT, NAV_FLAG, NAV_PLACE, NAV_QUIT};
        for(int i = 0; i < buttons.length; ++i){
            g2d.drawImage(imagesToDraw[i], buttons[i][0], buttons[i][1], this);
        }
    }
    
    private int[][][][] getLocalMove(int boardX, int boardY){
        boardX = 0;
        boardY = 0;
        //board format [whos board][x][y][covered, is bomb, # surrounding, is flagged]
        if(turnCounter == 0){
            //generate the board as the first move always
        }
        if(currentAction.equals("scout")){
            boards[turn][boardX][boardY][0] = 1;
            if(boards[turn][boardX][boardY][1] == 1){
                //game over you clicked on a bomb
            }
        }else if(currentAction.equals("flag")){
            boards[turn][boardX][boardY][3] = 1;
        }else if(currentAction.equals("bomb")){
            boards[(turn+1)%2][boardX][boardY][1] = 1;
        }
        turn = (turn+1)%2;
        
        
        displayBoards(1);
        return boards;
    }

    private void displayBoards(int metric){
        String msg = "";
        
        for (int i = 0; i < boards.length; i++) {
            msg += "\n\n\n\n";
            for (int j = 0; j < boards[turn].length; j++) {
                msg += "\n";
                for (int k = 0; k < boards[turn][j].length; k++) {
                    msg += boards[turn][j][k][metric] + " ";
                }
            }
        }
        System.out.println(msg);
    }
    
    /**
     * Checks all the components for button hits and completes the action related to the button
     * @param x - x coordinate of mouse
     * @param y - y coordinate of mouse
     */
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
                currentAction = "scout";
                break;
            case 2:
                System.out.println("Flag Bomb");
                currentAction = "flag";
                break;
            case 3:
                System.out.println("Place Bomb");
                currentAction = "bomb";
                break;
            case 4:
                System.exit(0);
        }            
    }
    
    
    private boolean gridHit(int x, int y){
        return true;
    }    
    
    
    @Override
    public void paintComponent(Graphics g){
        if(firstFrame){
            buttons = new int[][]{
                {0, getHeight()-90, 467, 85}, //Settings
                {(getWidth()/2)-(NAV_SCOUT.getWidth(this)/2)-150, getHeight()-90, 80, 80}, //Scout
                {(getWidth()/2)-(NAV_FLAG.getWidth(this)/2)-25, getHeight()-90, 57, 80}, //Flag
                {(getWidth()/2)-(NAV_PLACE.getWidth(this)/2)+65, getHeight()-90, 80, 80}, //Place
                {getWidth()-320, getHeight()-90, 296, 80} //Quit
            };
            firstFrame = false;
        }
        super.paintComponent(g);  //prep the panel for drawing
        drawStaticComponents(g);  //draw the main menu
        drawDynamicComponents(g); //draw the changing components
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
