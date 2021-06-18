/*
 * Ehsan Patel & Lukas Krampitz
 * 14-Jun-2021
 * The JPanel that will be placed on the main game screen JFrame that will hold the logic for the gameplay
 */
package minesweepertej4m;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.InputStream;
import resources.ResourcesRef;

import static resources.ResourcesRef.*;

public class gameScreenPanel extends JPanel implements ActionListener, MouseMotionListener {
    //the parent frame
    private gameScreenFrame gameScreenFrameRef;
    
    //the client from the parent Frame and also for this Panel
    private Client sweeperClient;
    
    //panel variables
    private static int[][] buttons;
    
    private Timer timer;

    //game variables
    private int turn = 0;
    private int turnCounter = 0;
    private boolean showBombs = false; //toggle to change bomb visibility
    private static boolean player1FirstTurn = true;
    private static boolean player2FirstTurn = true;
    private boolean isValid = false;
    private boolean gameOver = false;
    private int winner = 0;
    
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
    
    double widthScalar;
    double heightScalar;
    
    private boolean waitsForBothPlayers = true; //If this is the first client does it wait for the server to send the start up command before it accepts grid hits
    private boolean hasRecievedStartup; //whether or not this client has gotten the startup command from the server
    
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
        
        //the client has not yet recieved the start up command
        hasRecievedStartup = false;
        
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
                if(turn == 0 && !gameOver) {
                    
                    //checks if the game needs to check for startup command and if it has recived it
                    //but only if this is the first player. The second will always be able to go
                    if ((id == 1 && (waitsForBothPlayers && hasRecievedStartup) || (!waitsForBothPlayers)) || id == 2)   {

                        //checks if the mouse was clicked on the first grid space
                        if(firstGridHit(e.getX(),e.getY()) && ((id == 2 && currentAction.equals("bomb") && !player2FirstTurn)||(id == 1 && !currentAction.equals("bomb")))){
                            //get the mapped coordinates
                            int boardX = (e.getX() - ((getWidth()/2)-(int)(550 / widthScalar)))/(int)(50 / widthScalar);
                            int boardY = (e.getY() - ((getHeight()/2)-(int)(250 / heightScalar)))/(int)(50 / heightScalar);
                            //update the board
                            boards = getLocalMove(boardX,boardY, 0);
                            checkForWins();

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
                            int boardX = (e.getX() - ((getWidth()/2) +(int)(50 / widthScalar))) /(int)(50 / widthScalar);
                            int boardY = (e.getY() - ((getHeight()/2)-(int)(250 / heightScalar)))/(int)(50 / heightScalar);
                            //update the board
                            boards = getLocalMove(boardX,boardY, 1);
                            checkForWins();

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
        checkForWins();
        
        //flips the turn
        turn = (turn+1)%2;
        
        //play the turn chime shound
        playTurnBeep();
        
        repaint();
        
    }
    
    /**
     * Method to call in Client.java to acknowledge the reception of the start up command
     */
    public void networkStartupCommand() {
        hasRecievedStartup = true;
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
            g2d.fillRect((getWidth() / 2) - ((int)(560 / widthScalar)) + (turn * (int)(600 / widthScalar)), 
                    (getHeight() / 2) - (int)(260 / heightScalar), 
                    (int)(516 / widthScalar),
                    (int)(512 / heightScalar));
        }else{
            g2d.fillRect((getWidth() / 2) - ((int)(560 / widthScalar)) + (((turn+1)%2) * (int)(600 / widthScalar)), 
                    (getHeight() / 2) - (int)(260 / heightScalar), 
                    (int)(516 / widthScalar), 
                    (int)(512 / heightScalar));
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
            x = buttons[2][0]-(int)(10 / widthScalar);
            y = buttons[2][1];
        }else if(currentAction.equals("bomb")){
            x = buttons[3][0];
            y = buttons[3][1];
        }
        
        //draws the rectangle
        g2d.drawRect(x-(int)(2 / widthScalar),
                y-(int)(5 / heightScalar),
                (int)(85 / widthScalar),
                (int)(85 / heightScalar));
        //resets the stroke so the other items are not outlined
        g2d.setStroke(oldStroke);
        //draw boards on screen
    }
    
    
    
    /**
     * draws the components that do not move on the screen
     * @param g 
     */
    private void drawStaticComponents(Graphics g) {
        //values for scaling elements
        widthScalar = 1920.0 / gameScreenFrameRef.getWidth();
        heightScalar = 1080.0 / gameScreenFrameRef.getHeight();
        
        //The graphics model to use
        Graphics2D g2d = (Graphics2D) g;
        //changes the drawing color to green
        g2d.setColor(new Color(88,165,100));
        //draws the background on the panel
        g2d.fillRect(0,0,getWidth(),getHeight());
        //changes the color to white
        g2d.setColor(new Color(255,255,255));
        //draws the navigation bar
        g2d.fillRect(0, (int) (getHeight()-(100.0 / heightScalar)),getWidth(), (int) (101.0 / heightScalar));
        
        //draws all the images for buttons to be used        
        Image[] imagesToDraw = {NAV_SETTINGS, NAV_SCOUT, NAV_FLAG, NAV_PLACE, NAV_QUIT};
        for(int i = 0; i < buttons.length; ++i){
            g2d.drawImage(imagesToDraw[i], 
                    buttons[i][0], 
                    buttons[i][1], 
                    buttons[i][2], 
                    buttons[i][3], 
                    this);
        }
        
        //sets font styles
        g2d.setColor(new Color(0,0,0));
        g2d.setFont(new Font("TimesRoman", Font.ITALIC, 30));
        //Smoothens the font - Credit to stackoverflow
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        //draws the strings for opponent and you to know which board you should move on
        g2d.drawString("You:", 
                (getWidth() / 2) - (int)(560 / widthScalar) + (id-1) * (int)(600 / widthScalar), 
                (getHeight() / 2) - (int)(280 / heightScalar));
        g2d.drawString("Opponent:", 
                (getWidth() / 2) - (int)(560 / widthScalar) + ((id)%2) * (int)(600 / widthScalar), 
                (getHeight() / 2) - (int)(280 / heightScalar));
        
        
        //displays if there is a winner
        if(gameOver){
            String winnerText = (id-1==winner) ? "You Lost!":"You Won!";
            // Get the FontMetrics for font width
            FontMetrics metrics = g.getFontMetrics(g2d.getFont());
            //draws the text to display the winner
            g2d.drawString(winnerText, 
                    (getWidth() - metrics.stringWidth(winnerText)) / 2, 
                    (int)(80 / heightScalar));
        }

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

    
    private void checkForWins(){
        //loops through all the tiles on the board
        for (int i = 0; i < boards.length; i++) {
            for (int j = 0; j < boards[i].length; j++) {
                for (int k = 0; k < boards[i][j].length; k++) {
                    if(boards[i][k][j][1] == 1 && boards[i][k][j][0] == 1){
                        showBombs = true;
                        gameOver = true;
                        winner = i;
                    }
                }
            }
        }
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
                    g2d.fillRect((int)(((getWidth()/2)-(550 / widthScalar)) + (k*(int)(50 / widthScalar)) + (i*(600 / widthScalar))),
                            (int)((getHeight()/2)-(250 / heightScalar) + (j*(int)(50 / heightScalar))),
                            (int)(52 / widthScalar),
                            (int)(51 / heightScalar));
                    
                    
                    //draws a different color grid tile to mark covered and uncovered tiles
                    if(boards[i][k][j][0] == 0){
                        g2d.setColor(new Color(55, 57, 63));
                    }else if(boards[i][k][j][0] == 1 && boards[i][k][j][1] == 1){
                        g2d.setColor(new Color(255, 57, 63));
                    }else{
                        g2d.setColor(new Color(255, 255, 255));
                    }
                    //draws the tile using the color
                    g2d.fillRect(((getWidth()/2)-(int)(550 / widthScalar)) + (k*(int)(50 / widthScalar)) + (i*(int)(600 / widthScalar))+(int)(2 / widthScalar),
                            (getHeight()/2)-(int)(250 / heightScalar) + (j*(int)(50 / heightScalar))+(int)(2 / heightScalar),
                            (int)(46 / widthScalar),
                            (int)(46 / heightScalar));
                    
                    //draws a flag on all the tiles that the user has selected to have a flag on
                    if(boards[i][k][j][3] == 1){
                        g2d.drawImage(GAME_FLAG,
                                ((getWidth()/2)-(int)(550 / widthScalar)) + (k*(int)(50 / widthScalar)) + (i*(int)(600 / widthScalar))+(int)(10 / widthScalar),
                                (getHeight()/2)-(int)(250 / heightScalar) + (j*(int)(50 / heightScalar))+(int)(5 / heightScalar),
                                (int)(31 / widthScalar),
                                (int)(40 / heightScalar),
                                this);
                    }
                    
                    //draws a bomb on all the tiles that the user has selected to have a bomb on
                    if(boards[i][k][j][1] == 1 && ((i == id - 1 && showBombs) || (i != id - 1))){
                        g2d.drawImage(GAME_BOMB,
                                ((getWidth()/2)-(int)(550 / widthScalar)) + (k*(int)(50 / widthScalar)) + (i*(int)(600 / widthScalar))+(int)(2 / widthScalar),
                                (getHeight()/2)-(int)(250 / heightScalar) + (j*(int)(50 / heightScalar))+(int)(2 / heightScalar),
                                (int)(46 / widthScalar),
                                (int)(46 / heightScalar),
                                this);
                    }
                    
                    //displays a number if the square has been uncovered
                    if(boards[i][k][j][0] == 1 && boards[i][k][j][2] != 0 && boards[i][k][j][1] != 1){
                        g2d.setColor(colors[boards[i][k][j][2]-1]);
                        g2d.setFont(new Font("TimesRoman", Font.PLAIN, (int)(30 / heightScalar)));
                        g2d.drawString(""+boards[i][k][j][2],
                                ((getWidth()/2)-(int)(550 / widthScalar)) + (k*(int)(50 / widthScalar)) + (i*(int)(600 / widthScalar))+(int)(17 / widthScalar),
                                (getHeight()/2)-(int)(250 / heightScalar) + (j*(int)(50 / heightScalar))+(int)(36 / heightScalar));
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
                currentAction = "scout";
                break;
            case 2:
                currentAction = "flag";
                break;
            case 3:
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
        return (x > (getWidth()/2)-(int)(550 / widthScalar) && x < (getWidth()/2)-(int)(50 / widthScalar))
                && (y > (getHeight()/2)-(int)(250 / heightScalar) && y < (getHeight()/2)+(int)(250 / heightScalar));
    }
    
    
    /**
     * checks for a collision with a tile on the second grid
     * @param x - mouse x coordinate
     * @param y - mouse y coordinate
     * @return - if the mouse clicked in a space on the second grid
     */
    private boolean secondGridHit(int x, int y){
        return (x > (getWidth()/2)+(int)(50 / widthScalar) && x < (getWidth()/2)+(int)(550 / widthScalar))
                && (y > (getHeight()/2)-(int)(250 / heightScalar) && y < (getHeight()/2)+(int)(250 / heightScalar));
    }
    
    
    /**
     * update / draw loop
     * @param g - the graphics object to use for drawing
     */
    @Override
    public void paintComponent(Graphics g){
        //stores the coordinates of all the buttons
        buttons = new int[][]{
            {0, 
                getHeight()-(int)(90 / heightScalar), 
                (int)(467 / widthScalar), 
                (int)(85 / heightScalar)}, //Settings
            {(getWidth()/2)-(  (int)(NAV_SCOUT.getWidth(this) / widthScalar)/2)- (int)(140 / widthScalar), 
                getHeight()-(int)(90 / heightScalar), 
                (int)(80 / widthScalar), 
                (int)(80 / heightScalar)}, //Scout
            {(getWidth()/2)-(int)((NAV_FLAG.getWidth(this) / widthScalar)/2),
                getHeight()-(int)(90 / heightScalar), 
                (int)(57 / widthScalar), 
                (int)(80 / heightScalar)}, //Flag
            {(int)((getWidth()/2)-((NAV_PLACE.getWidth(this) / widthScalar)/2)+(140 / widthScalar)), 
                getHeight()-(int)(90 / heightScalar), 
                (int)(80 / widthScalar), 
                (int)(80 / heightScalar)}, //Place
            {getWidth()-(int)(320 / widthScalar),
                getHeight()-(int)(90 / heightScalar), 
                (int)(296 / widthScalar), 
                (int)(80 / heightScalar)} //Quit
        };
        
        super.paintComponent(g);  //prep the panel for drawing
        drawStaticComponents(g);  //draw the main menu
        drawDynamicComponents(g); //draw the changing components
        //synchronizes the graphics
        Toolkit.getDefaultToolkit().sync();
    }
    
    /**
     * Play audio to the user from a file
     * @param TURN_BEEP 
     */
    private void playTurnBeep() {
        
        //try to load and play it
        try {
            InputStream TURN_BEEP = (ResourcesRef.class.getResourceAsStream("audio/turnBeep.wav"));

            InputStream bufferedStream; //add a buffer to the stream
            AudioInputStream stream; //the stream best used for playing audio

            //decorate it with a buffer
            bufferedStream = new BufferedInputStream(TURN_BEEP);

            //load it in
            stream = AudioSystem.getAudioInputStream(bufferedStream);

            //clip it
            Clip clip = AudioSystem.getClip();
            clip.open(stream);

            clip.start();
            
        } catch (FileNotFoundException ex) {
            System.out.println("[ERROR] Sound File not found\n" + ex);
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("[ERROR] Sound File is of wrong type\n" + ex);
        } catch (IOException | LineUnavailableException ex) {
            System.out.println("[ERROR] Sound File error\n" + ex);
        } 
        
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
