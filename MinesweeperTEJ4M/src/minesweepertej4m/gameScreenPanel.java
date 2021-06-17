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
import java.awt.RenderingHints;
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
    private static boolean player1FirstTurn = true;
    private static boolean player2FirstTurn = true;
    private boolean isValid = false;
    private Timer timer;

    //game variables
    private int turn = 0;
    private int turnCounter = 0;
    private boolean showBombs = true; //toggle to change bomb visibility
    //board format [whos board][x][y][covered, is bomb, # surrounding, is flagged]
    private int[][][][] boards = new int[2][10][10][4];
    private String currentAction = "scout";
    private Color[] colors = {
        new Color(29, 0, 255),
        new Color(52, 126, 0),
        new Color(234, 26, 0),
        new Color(135, 66, 245),
        new Color(117, 9, 0),
        new Color(54, 126, 129),
        new Color(0,0,0),
        new Color(128, 128, 128)
    };
  
    private int id;
    
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
        
        //receive whos turn it is from the server here
        id = sweeperClient.getClientID(); //still need to do something with this data
        //change the if statement below to make sure that it is your turn and not other client
        System.out.println(id);
        turn = id - 1;
        
        //allows the board to recieve input from mouseclicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                //checks if any of the buttons have been pressed to run an action
                checkButtonHits(e.getX(),e.getY());
                
                //checks if it is the current players turn to make a move
                if(turn == 0){

                    //checks if the mouse was clicked on the first grid space
                    if(firstGridHit(e.getX(),e.getY()) && ((id == 2 && currentAction.equals("bomb") && !player2FirstTurn)||(id == 1 && !currentAction.equals("bomb")))){
                        //get the mapped coordinates
                        int boardX = (e.getX() - ((getWidth()/2)-550))/50;
                        int boardY = (e.getY() - ((getHeight()/2)-250))/50;
                        //update the board
                        boards = getLocalMove(boardX,boardY, 0);
                        
                        //checks if the move is valid
                        if(isValid){
                            if(player1FirstTurn){
                                generateBoard(0,boardX,boardY);
                                player1FirstTurn = false;
                            }
                            if(boards[1][boardX][boardY][3] == 0){
                                revealTiles(boardX, boardY, 0);
                            }
                            
                            //send the updated board to the game server
                            sweeperClient.sendBoardToServer(boards);
                            isValid = false;
                        }

                    //checks if the mouse was clicked on the second grid space
                    }else if(secondGridHit(e.getX(),e.getY()) && ((id == 1 && currentAction.equals("bomb") && !player1FirstTurn)||(id == 2 && !currentAction.equals("bomb")))){
                        //get the mapped coordinates
                        int boardX = (e.getX() - ((getWidth()/2)+50))/50;
                        int boardY = (e.getY() - ((getHeight()/2)-250))/50;
                        //update the board
                        boards = getLocalMove(boardX,boardY, 1);
                        
                        //checks if the move is valid
                        if(isValid){
                            if(player2FirstTurn){
                                generateBoard(1,boardX,boardY);
                                player2FirstTurn = false;
                            }
                            if(boards[1][boardX][boardY][3] == 0){
                                revealTiles(boardX, boardY, 1);
                            }
                            
                            //send the updated board to the game server
                            sweeperClient.sendBoardToServer(boards);
                            isValid = false;
                        }
                    }
                }
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
        
        //flips the turn
        turn = (turn+1)%2;
        
        repaint();
        
    }
    
    
    /**
     * Draws all the components that change location or color etc
     * @param g - the graphics object to use to draw
     */
    private void drawDynamicComponents(Graphics g) {
        //The graphics model to use
        Graphics2D g2d = (Graphics2D) g;
        
        
        //outlines the grid of who's turn it is
        g2d.setColor(new Color(0,0,0));
        
        //which grid to outline is dependant on the id and the turn
        if(id == 1){
            g2d.fillRect((getWidth() / 2) - (560) + turn * 600, (getHeight() / 2) - 260, 520, 520);
        }else{
            g2d.fillRect((getWidth() / 2) - (560) + ((turn+1)%2) * 600, (getHeight() / 2) - 260, 520, 520);
        }
        
        
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
    
    
    
    /**
     * draws the components that do not move on the screen
     * @param g 
     */
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
        
        //sets font styles
        g2d.setColor(new Color(0,0,0));
        g2d.setFont(new Font("TimesRoman", Font.ITALIC, 30));
        //Smoothens the font - Credit to stackoverflow
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        //draws the strings for opponent and you to know which board you should move on
        g2d.drawString("You:", (getWidth() / 2) - (560) + (id-1) * 600, (getHeight() / 2) - 280);
        g2d.drawString("Opponent:", (getWidth() / 2) - (560) + ((id)%2) * 600, (getHeight() / 2) - 280);

    }
    
    
    /**
     * gets the move of the current player based on the click position on the grid and the board they clicked
     * @param boardX - x position of grid tile clicked
     * @param boardY - y position of grid tile clicked
     * @param boardNum - which board was clicked
     * @return an updated version of the board
     */
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
                isValid = true;
                //switches turn: may need to move into specific actions to be flag at any time
                turn = (turn + 1) % 2;
            }
            if(boards[boardNum][boardX][boardY][1] == 1){
                showBombs = true;
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
            if(boards[boardNum][boardX][boardY][0] == 0 && boards[boardNum][boardX][boardY][1] != 1){
                boards[boardNum][boardX][boardY][1] = 1;
                isValid = true;
                //switches turn: may need to move into specific actions to be flag at any time
                turn = (turn + 1) % 2;
            }
            
        }
        //corrects the numbers displaying the number of bombs surrounding a tile
        updateNumbers();
        
        return boards;
    }
    
    /**
     * Recursive function that opens up tiles around empty tiles
     * @param boardX - the starting x coordinate
     * @param boardY - the starting y coordinate
     * @param boardNum - the board to perform the recursive function on
     */
    private void revealTiles(int boardX, int boardY, int boardNum){
        //if the action was to scout the tile
        if(currentAction.equals("scout")){
            //check around the tile
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    //only verify if the position is within the grid
                    if(boardX+i >= 0 && boardX+i < 10 && boardY+j >= 0 && boardY+j < 10){
                        //checks the starting point to make sure it is a zero as well as the tile around it and that it is currently uncovered
                        if(boards[boardNum][boardX][boardY][2] == 0 && boards[boardNum][boardX+i][boardY+j][2] == 0 && boards[boardNum][boardX+i][boardY+j][0] == 0 && !(i == 0 && j == 0)){
                            //open the tile and check around it
                            boards[boardNum][boardX+i][boardY+j][0] = 1;
                            revealTiles(boardX+i, boardY+j, boardNum);
                        }else if(boards[boardNum][boardX][boardY][2] == 0){
                            //otherwise just open the tile and remove flags
                            boards[boardNum][boardX+i][boardY+j][0] = 1;
                            boards[boardNum][boardX+i][boardY+j][3] = 0;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * updates the numbers for all tiles indicating the number of bombs surrounding the tiles
     */
    private void updateNumbers(){
        //loops through all the tiles on the board
        for (int i = 0; i < boards.length; i++) {
            for (int j = 0; j < boards[i].length; j++) {
                for (int k = 0; k < boards[i][j].length; k++) {
                    //resets the counter for the tile
                    boards[i][k][j][2] = 0;
                    //counts around the tile
                    for (int l = -1; l < 2; l++) {
                        for (int m = -1; m < 2; m++) {
                            //adds to the counter for each bomb surrounding the tile
                            if(k+l >= 0 && k+l < 10 && j+m >= 0 && j+m < 10){
                                boards[i][k][j][2] += boards[i][k+l][j+m][1];
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    /**
     * Generates the bombs on the board after the first click
     * @param boardNum - the board to generate bombs on
     * @param startX - the initial x position that should be protected
     * @param startY - the initial y position that should be protected
     */
    private void generateBoard(int boardNum, int startX, int startY){
        
        //the number of bombs that have been generated
        int bombCount = 0;
        
        //runs through the rows and columns of the board
        for (int i = 0; i < boards[boardNum].length; ++i) {
            for (int j = 0; j < boards[boardNum][i].length; ++j) {
                //exits if the board alrady has bombs generated on it
                if(boards[boardNum][i][j][1] == 1){
                    return;
                }
            }
        }
        
        //Used to generate random numbers
        Random rand = new Random();
        
        //runs until there are 27 bombs on the field
        while(bombCount < 27){
            //picks a random x and y to place a bomb
            int randX = rand.nextInt(10);
            int randY = rand.nextInt(10);
            
            //tests to make sure the bombs do not spawn around the first tile clicked
            boolean test = true;
            //checks around the tile
            for (int l = -1; l < 2; l++) {
                for (int m = -1; m < 2; m++) {
                    //bomb placement does not pass the test to make sure it isn't spawned nearby
                    if(startX+l == randX && startY+m == randY){
                        test = false;
                    }
                }
            }
            
            //if the test is passed and the bomb is not on the start position
            if(test && boards[boardNum][randX][randY][1] != 1 && !(randX == startX && randY == startY)){
                //add a bomb to the tile and record the number of bombs
                boards[boardNum][randX][randY][1] = 1;
                bombCount++;
            }
        }
        
        //displays the numbers surrounding the bombs
        updateNumbers();
    }

    
    /**
     * displays the board on the screen with all the changing components
     * @param g2d - the drawing object to use to create the shapes and images
     */
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
                    //draws a bomb on all the tiles that the user has selected to have a bomb on
                    if(boards[i][k][j][1] == 1 && (showBombs ||  i == (turn+1)%2)){
                        g2d.drawImage(GAME_BOMB,((getWidth()/2)-550) + (k*50) + (i*600)+2,(getHeight()/2)-250 + (j*50)+2,this);
                    }
                    
                    //displays a number if the square has been uncovered
                    if(boards[i][k][j][0] == 1 && boards[i][k][j][2] != 0){
                        g2d.setColor(colors[boards[i][k][j][2]-1]);
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
     * @param e - the event
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
