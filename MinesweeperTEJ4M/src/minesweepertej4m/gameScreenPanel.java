/*
 * Ehsan Patel & Lukas Krampitz
 * 14-Jun-2021
 * The JPanel that will be placed on the main game screen JFrame that will hold the logic for the gameplay
 */
package minesweepertej4m;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import static resources.ResourcesRef.*;

public class gameScreenPanel extends JPanel implements ActionListener, MouseMotionListener {
    //the parent frame
    private gameScreenFrame gameScreenFrameRef;
    
    //the client from the parent Frame and also for this Panel
    private Client sweeperClient;
    
    //panel variables
    private static int[][] buttons;
    private static boolean firstFrame = true;
    private Timer timer;

    //game variables
    private int turn = 0;
    private int turnCounter = 0;
    //board format [whos board][x][y][covered, is bomb, # surrounding, is flagged]
    private int[][][][] boards = new int[2][10][10][4];
    private String currentAction = "scout";
    
    
    /**
     * Constructor for this panel to be drawn
     * @param m
     * @param sweeperClient
     */
    public gameScreenPanel(gameScreenFrame m, Client sweeperClient) {
        //stores the parent frame passed in
        gameScreenFrameRef = m;
        
        //store the Client
        this.sweeperClient = sweeperClient;
        
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
                if(turn == 0){
                    if(firstGridHit(e.getX(),e.getY())){
                        int boardX = (e.getX() - ((getWidth()/2)-550))/50;
                        int boardY = (e.getY() - ((getHeight()/2)-250))/50;

                        boards = getLocalMove(boardX,boardY, 0);
                        
                    }else if(secondGridHit(e.getX(),e.getY())){
                        int boardX = (e.getX() - ((getWidth()/2)+50))/50;
                        int boardY = (e.getY() - ((getHeight()/2)-250))/50;

                        boards = getLocalMove(boardX,boardY, 1);
                        
                    }
                }
                turn = 0;
            }

        });
        
        //creates a timer to update the window
        timer = new Timer(25, this);
        timer.start();
        
    }
    
    private void drawDynamicComponents(Graphics g) {
        //The graphics model to use
        Graphics2D g2d = (Graphics2D) g;
        displayBoardsGUI(g2d);
        
        
        g2d.setColor(new Color(0,0,0));

        //highlights the current action
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(4));
        int x = 0;
        int y = 0;

        if(currentAction.equals("scout")){
            x = buttons[1][0];
            y = buttons[1][1];
        }else if(currentAction.equals("flag")){
            x = buttons[2][0]-10;
            y = buttons[2][1];
        }else if(currentAction.equals("bomb")){
            x = buttons[3][0];
            y = buttons[3][1];
        }
        
        g2d.drawRect(x-2,y-5,85,85);
        g2d.setStroke(oldStroke);
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
        g2d.fillRect((getWidth()/2)-(550),(getHeight()/2)-250,500,500);
        g2d.fillRect((getWidth()/2)+50,(getHeight()/2)-250,500,500);
        
        //draws all the images for buttons to be used
        
        Image[] imagesToDraw = {NAV_SETTINGS, NAV_SCOUT, NAV_FLAG, NAV_PLACE, NAV_QUIT};
        for(int i = 0; i < buttons.length; ++i){
            g2d.drawImage(imagesToDraw[i], buttons[i][0], buttons[i][1], this);
        }
    }
    
    private int[][][][] getLocalMove(int boardX, int boardY, int boardNum){
        //board format [whos board][x][y][covered, is bomb, # surrounding, is flagged]
        if(turnCounter == 0){
            //generate the board as the first move always
        }
        if(currentAction.equals("scout")){
            if(boards[boardNum][boardX][boardY][3] == 0){
                boards[boardNum][boardX][boardY][0] = 1;
            }
//            if(boards[boardNum][boardX][boardY][1] == 1){
//                game over you clicked on a bomb
//            }
        }else if(currentAction.equals("flag")){
            if(boards[boardNum][boardX][boardY][0] == 0){
                boards[boardNum][boardX][boardY][3] = (boards[boardNum][boardX][boardY][3]+1)%2;
            }
        }else if(currentAction.equals("bomb")){
            boards[(boardNum+1)%2][boardX][boardY][1] = 1;
        }
        turn = (turn+1)%2;
        
        
        displayBoards(0);
        return boards;
    }

    private void displayBoards(int metric){
        String msg = "";
        //board format [whos board][x][y][covered, is bomb, # surrounding, is flagged]
        for (int i = 0; i < boards.length; i++) {
            msg += "\n\n\n\n";
            for (int j = 0; j < boards[i].length; j++) {
                msg += "\n";
                for (int k = 0; k < boards[i][j].length; k++) {
                    msg += boards[i][k][j][metric] + " ";
                }
            }
        }
        System.out.println(msg);
    }
    
    private void displayBoardsGUI(Graphics2D g2d){
        //board format [whos board][x][y][covered, is bomb, # surrounding, is flagged]
        for (int i = 0; i < boards.length; i++) {
            for (int j = 0; j < boards[i].length; j++) {
                for (int k = 0; k < boards[i][j].length; k++) {
                    g2d.setColor(new Color(0,0,0));
                    g2d.fillRect(((getWidth()/2)-550) + (k*50) + (i*600),(getHeight()/2)-250 + (j*50),50,50);
                    
                    //draws a different color grid tile to mark covered and uncovered tiles
                    if(boards[i][k][j][0] == 0){
                        g2d.setColor(new Color(55, 57, 63));
                    }else{
                        g2d.setColor(new Color(255, 255, 255));
                    }
                    g2d.fillRect(((getWidth()/2)-550) + (k*50) + (i*600)+2,(getHeight()/2)-250 + (j*50)+2,46,46);
                    
                    
                    if(boards[i][k][j][3] == 1){
                        g2d.drawImage(GAME_FLAG,((getWidth()/2)-550) + (k*50) + (i*600)+10,(getHeight()/2)-250 + (j*50)+5,this);
                    }
                    
                    
                }
            }
        }
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
    
    
    private boolean firstGridHit(int x, int y){
        return (x > (getWidth()/2)-550 && x < (getWidth()/2)-50) && (y > (getHeight()/2)-250 && y < (getHeight()/2)+250);
    }
    private boolean secondGridHit(int x, int y){
        return (x > (getWidth()/2)+50 && x < (getWidth()/2)+550) && (y > (getHeight()/2)-250 && y < (getHeight()/2)+250);
    }
    
    
    @Override
    public void paintComponent(Graphics g){
        buttons = new int[][]{
            {0, getHeight()-90, 467, 85}, //Settings
            {(getWidth()/2)-(NAV_SCOUT.getWidth(this)/2)-140, getHeight()-90, 80, 80}, //Scout
            {(getWidth()/2)-(NAV_FLAG.getWidth(this)/2), getHeight()-90, 57, 80}, //Flag
            {(getWidth()/2)-(NAV_PLACE.getWidth(this)/2)+140, getHeight()-90, 80, 80}, //Place
            {getWidth()-320, getHeight()-90, 296, 80} //Quit
        };
        super.paintComponent(g);  //prep the panel for drawing
        drawStaticComponents(g);  //draw the main menu
        drawDynamicComponents(g); //draw the changing components
        //synchronizes the graphics
        Toolkit.getDefaultToolkit().sync();
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
