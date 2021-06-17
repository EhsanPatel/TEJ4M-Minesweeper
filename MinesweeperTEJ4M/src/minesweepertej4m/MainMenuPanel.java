/*
 * Lukas Krampitz
 * Jun 14, 2021
 * 
 */
package minesweepertej4m;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import static resources.ResourcesRef.*;

/**
 *
 * @author Tacitor
 */
public class MainMenuPanel extends JPanel implements ActionListener, MouseMotionListener {

    private MainMenuFrame mainMenuFrameRef;
    private int drawButtonSelection; //the button that should be drawn with a selection overlay
    private int drawButtonBorder; //the button that should be drawn with a border after a click

    double widthScalar;
    double heightScalar;

    /**
     * Constructor
     *
     * @param m
     */
    public MainMenuPanel(MainMenuFrame m) {
        mainMenuFrameRef = m;

        //setup vars
        drawButtonBorder = -1;
        drawButtonSelection = -1;

        //add a mouse motion listener for hovering over button
        addMouseMotionListener(this);

        //add a mouse Listener for clicks
        addMouseListener(new MouseInputAdapter() {
            /**
             * Trigger when the user clicks the Main menu Panel.
             *
             * @param evt The event representing the mouse click
             */
            @Override
            public final void mouseReleased(MouseEvent evt) {
                //send the mouse event to the click handeler
                buttonMouseEvt(evt, 0); //tpye 0 for a click release
            }

            @Override
            public final void mousePressed(MouseEvent evt) {
                //send the mouse event to the click handeler
                buttonMouseEvt(evt, 2); //tpye 2 for a click press
            }
        });

    }

    /**
     * Method to handle when the user clicks on the main menu
     *
     * @param event
     * @param eventType whether or not the button is hovered over or clicked
     */
    public void buttonMouseEvt(MouseEvent event, int eventType) {

        //deselect any button
        drawButtonSelection = -1;

        //check the buttons if the mouse even is click or move
        if (eventType <= 2 || eventType >= 0) {

            //if the button is released always clear a button selection
            if (eventType == 0) {
                drawButtonBorder = -1;
            }

            //check if any of the buttons were clicked
            //button x and y vars
            int btnX = super.getWidth() / 2 - ((int) (MAIN_MENU_REG[0].getWidth(null) / widthScalar) / 2);
            int btnY;

            //loop through the 2 buttons
            for (int i = 0; i < 2; i++) {

                btnY = super.getHeight() / 2 + ((int) (MAIN_MENU_REG[i].getHeight(null) / heightScalar) * i) + ((int) (10 / heightScalar) * i);

                //check if the click was within any of the button hitboxes
                if (event.getX() > btnX
                        && event.getY() > btnY
                        && event.getX() < (btnX + (int) (MAIN_MENU_REG[i].getWidth(null) / widthScalar))
                        && event.getY() < (btnY + (int) (MAIN_MENU_REG[i].getHeight(null) / heightScalar))) {

                    //if a mouse movemnt
                    if (eventType == 1) {
                        drawButtonSelection = i;
                    }

                    //chcek if the user made a down click on a button
                    if (eventType == 2) {
                        drawButtonBorder = i;
                    }

                    //check if the user released the mouse button
                    if (eventType == 0) {
                        //System.out.println("Click! On button: " + i);

                        //also select that button
                        drawButtonSelection = i;
                        drawButtonBorder = -1;

                        //preform the action that that button is reposible for
                        if (i == 0) { //create server
                            //hide this main menu
                            mainMenuFrameRef.setVisible(false);

                            //create a new game server creation Frame and show it
                            SweeperCreate creationFrame = new SweeperCreate(mainMenuFrameRef);
                            creationFrame.setLocationRelativeTo(null);
                            creationFrame.setVisible(true);
                        } else if (i == 1) { //join game
                            //hide this main menu
                            mainMenuFrameRef.setVisible(false);

                            //create a new game server creation Frame and show it
                            SweeperJoin joinFrame = new SweeperJoin(mainMenuFrameRef);
                            joinFrame.setLocationRelativeTo(null);
                            joinFrame.setVisible(true);
                        }
                    }

                }

            }
        }

        repaint();
    }

    private void drawMenu(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        //vars
        widthScalar = 1920.0 / mainMenuFrameRef.getWidth();
        heightScalar = 1080.0 / mainMenuFrameRef.getHeight();

        //add the background image
        g2d.drawImage(MAIN_MENU_BACKGROUND,
                0,
                0,
                mainMenuFrameRef.getWidth(), //scale it to the same size as the Frame
                mainMenuFrameRef.getHeight(),
                null);

        //create a new array to store the Image array for a given button
        java.awt.Image[] buttonImg;

        //loop through the 3 buttons to draw them
        for (int i = 0; i < 2; i++) {

            if (drawButtonBorder == i) {
                buttonImg = MAIN_MENU_CLICK;
            } else if (drawButtonSelection == i) {
                buttonImg = MAIN_MENU_HOVER;
            } else {
                buttonImg = MAIN_MENU_REG;
            }

            g2d.drawImage(buttonImg[i],
                    super.getWidth() / 2 - ((int) (buttonImg[i].getWidth(null) / widthScalar) / 2),
                    super.getHeight() / 2 + ((int) (buttonImg[i].getHeight(null) / heightScalar) * i) + ((int) (10 / heightScalar) * i),
                    (int) (buttonImg[i].getWidth(null) / widthScalar),
                    (int) (buttonImg[i].getHeight(null) / heightScalar),
                    null);

        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g); //prep the panel for drawing
        drawMenu(g); //draw the main menu
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //System.out.println("Dragged");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        buttonMouseEvt(e, 1); //type 1 for mouse moved
    }

}
