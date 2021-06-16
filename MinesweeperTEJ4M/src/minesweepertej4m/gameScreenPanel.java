/*
 * Ehsan Patel & Lukas Krampitz
 * 14-Jun-2021
 * The JPanel that will be placed on the main game screen JFrame that will hold the logic for the gameplay
 */
package minesweepertej4m;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
import java.util.Random;

import static resources.ResourcesRef.*;

public class gameScreenPanel extends JPanel implements ActionListener, MouseMotionListener {
    //the parent frame
    private gameScreenFrame gameScreenFrameRef;
    
    //the client from the parent Frame and also for this Panel
    private Client sweeperClient;
    
    //panel variables
    private static int[][] buttons;
//    private static boolean firstFrame = true;
    private static boolean player1FirstTurn = true;
    private static boolean player2FirstTurn = true;
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
                //checks if any of the buttons have been pressed to run an action
                checkButtonHits(e.getX(),e.getY());
                
                
                //receive whos turn it is from the server here
                //change the if statement below to make sure that it is your turn and not other client
                
                
                //checks if it is the current players turn to make a move
                if(turn == 0){
                    //checks if the mouse was clicked on the first grid space
                    if(firstGridHit(e.getX(),e.getY())){
                        //get the mapped coordinates
                        int boardX = (e.getX() - ((getWidth()/2)-550))/50;
                        int boardY = (e.getY() - ((getHeight()/2)-250))/50;
                        //update the board
                        boards = getLocalMove(boardX,boardY, 0);
                        if(player1FirstTurn){
                            generateBoard(0,boardX,boardY);
                            player1FirstTurn = false;
                        }
                        //send the updated board to the game server
                        sweeperClient.sendBoardToServer(boards); 
                        

                    //checks if the mouse was clicked on the second grid space
                    }else if(secondGridHit(e.getX(),e.getY())){
                        //get the mapped coordinates
                        int boardX = (e.getX() - ((getWidth()/2)+50))/50;
                        int boardY = (e.getY() - ((getHeight()/2)-250))/50;
                        //update the board
                        boards = getLocalMove(boardX,boardY, 1);
                        if(player2FirstTurn){
                            generateBoard(1,boardX,boardY);
                            player2FirstTurn = false;
                        }
                        //send the updated board to the game server
                        sweeperClient.sendBoardToServer(boards); 
                    }
                }
                
                //switches turn back: FOR DEBUGGING
                turn = 0;
            }

        });
        
        //creates a timer to update the window
        timer = new Timer(25, this);
        timer.start();
        
    }
    
    /**
     * Update the game boards based of the array received by the network
     * @param ungodlyArray 
     */
    public void networkUpdateBoards(int[][][][] ungodlyArray) {
        
        boards = ungodlyArray;
        
        repaint();
        
    }
    
    
    /**
     * Draws all the components that change location or color etc
     * @param g - the graphics object to use to draw
     */
    private void drawDynamicComponents(Graphics g) {
        //The graphics model to use
        Graphics2D g2d = (Graphics2D) g;
        
        //displays the grid
        displayBoardsGUI(g2d);
        
        //setts the color to black to draw the outline
        g2d.setColor(new Color(0,0,0));

        //highlights the current action
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(4));
        
        //coordinates to draw the action highlight are created
        int x = 0;
        int y = 0;

        //changes the coordinates based on the action being performed
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
        
        //draws the rectangle
        g2d.drawRect(x-2,y-5,85,85);
        //resets the stroke so the other items are not outlined
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
        
        //completes a different move for each action
        if(currentAction.equals("scout")){
            //checks to make sure there is no flag on the tile to be scouted
            if(boards[boardNum][boardX][boardY][3] == 0){
                //uncovers the tile
                boards[boardNum][boardX][boardY][0] = 1;
            }
            if(boards[boardNum][boardX][boardY][1] == 1){
                System.out.println("Game over, you clicked on a bomb");
            }
        }else if(currentAction.equals("flag")){
            //checks to make sure that the place you want to flag is not uncovered already
            if(boards[boardNum][boardX][boardY][0] == 0){
                //toggles the flag to be on or off
                boards[boardNum][boardX][boardY][3] = (boards[boardNum][boardX][boardY][3]+1)%2;
            }
        }else if(currentAction.equals("bomb")){
            //places a bomb
            if(boards[boardNum][boardX][boardY][0] == 0){
                boards[boardNum][boardX][boardY][1] = 1;
            }
        }
        
        updateNumbers();
        
        //switches turn: may need to move into specific actions to be flag at any time
        turn = (turn+1)%2;
        
        //prints the boards to the system output: FOR DEBUGGING
        //displayBoards(0);
        return boards;
    }
    
    
    private void updateNumbers(){
        for (int i = 0; i < boards.length; i++) {
            for (int j = 0; j < boards[i].length; j++) {
                for (int k = 0; k < boards[i][j].length; k++) {
                    boards[i][k][j][2] = 0;
                    for (int l = -1; l < 2; l++) {
                        for (int m = -1; m < 2; m++) {
                            if(k+l >= 0 && k+l < 10 && j+m >= 0 && j+m < 10){
                                boards[i][k][j][2] += boards[i][k+l][j+m][1];
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    private void generateBoard(int boardNum, int startX, int startY){
        int bombCount = 0;
        
        Random rand = new Random();
        while(bombCount < 27){
            int randX = rand.nextInt(10);
            int randY = rand.nextInt(10);
            boolean test = true;
            for (int l = -1; l < 2; l++) {
                for (int m = -1; m < 2; m++) {
                    if(startX+l == randX && startY+m == randY){
                        test = false;
                    }
                }
            }
            
            if(test && boards[boardNum][randX][randY][1] != 1 && !(randX == startX && randY == startY)){
                boards[boardNum][randX][randY][1] = 1;
                bombCount++;
            }
        }
        updateNumbers();
        displayBoards(1);
    }

    /**
     * Used to print out information to the console
     * @param metric - what information about the boards to print
     */
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
                    }else if(boards[i][k][j][0] == 1 && boards[i][k][j][1] == 1){
                        g2d.setColor(new Color(255, 57, 63));
                    }else{
                        g2d.setColor(new Color(255, 255, 255));
                    }
                    //draws the tile using the color
                    g2d.fillRect(((getWidth()/2)-550) + (k*50) + (i*600)+2,(getHeight()/2)-250 + (j*50)+2,46,46);
                    
                    //draws a flag on all the tiles that the user has selected to have a flag on
                    if(boards[i][k][j][3] == 1){
                        g2d.drawImage(GAME_FLAG,((getWidth()/2)-550) + (k*50) + (i*600)+10,(getHeight()/2)-250 + (j*50)+5,this);
                    }
                    //draws a bomb on all the tiles that the user has selected to have a flag on
                    if(boards[i][k][j][1] == 1){
                        g2d.drawImage(GAME_BOMB,((getWidth()/2)-550) + (k*50) + (i*600)+2,(getHeight()/2)-250 + (j*50)+2,this);
                    }
                    
                    //displays a number if the square has been uncovered
                    if(boards[i][k][j][0] == 1){
                        g2d.setColor(new Color(0, 0, 0));
                        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                        g2d.drawString(""+boards[i][k][j][2],((getWidth()/2)-550) + (k*50) + (i*600)+17,(getHeight()/2)-250 + (j*50)+36);
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
        //runs through the buttons array to get the x and y coordinates of the each button
        for(int i = 0; i < buttons.length; ++i){
            //checks for a mouse click within the confines of the button
            if(x > buttons[i][0] && x < buttons[i][0] + buttons[i][2]
                && y > buttons[i][1] && y < buttons[i][1] + buttons[i][3]){
                //stores which button in the list was hit
                buttonHit = i;
                //ends the loop when the button is found
                break;
            }
        }

        //executes a different action based on the button clicked
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
    
    /**
     * checks for a collision with a tile on the first grid
     * @param x - mouse x coordinate
     * @param y - mouse y coordinate
     * @return - if the mouse clicked in a space on the first grid
     */
    private boolean firstGridHit(int x, int y){
        return (x > (getWidth()/2)-550 && x < (getWidth()/2)-50) && (y > (getHeight()/2)-250 && y < (getHeight()/2)+250);
    }
    
    
    /**
     * checks for a collision with a tile on the second grid
     * @param x - mouse x coordinate
     * @param y - mouse y coordinate
     * @return - if the mouse clicked in a space on the second grid
     */
    private boolean secondGridHit(int x, int y){
        return (x > (getWidth()/2)+50 && x < (getWidth()/2)+550) && (y > (getHeight()/2)-250 && y < (getHeight()/2)+250);
    }
    
    
    /**
     * update / draw loop
     * @param g - the graphics object to use for drawing
     */
    @Override
    public void paintComponent(Graphics g){
        //stores the coordinates of all the buttons
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
    
    /**
     * updates the frame each loop
     * @param e 
     */
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
