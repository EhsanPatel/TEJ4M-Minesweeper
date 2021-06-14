/*
 * Lukas Krampitz
 * Jun 14, 2021
 * 
 */
package minesweepertej4m;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
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

    /**
     * Constructor
     *
     * @param m
     */
    public MainMenuPanel(MainMenuFrame m) {
        mainMenuFrameRef = m;

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
                buttonMouseEvt(evt, 0); //tpye 0 for a click
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
        if (eventType == 1 || eventType == 0) {

            //check if any of the buttons were clicked
            //button x and y vars
            int btnX = super.getWidth() / 2 - MAIN_MENU_CREATE.getWidth(null) / 2;
            int btnY;

            //loop through the 3 buttons
            for (int i = 0; i < 3; i++) {

                btnY = super.getHeight() / 2 + (MAIN_MENU_CREATE.getHeight(null) * i) + (10 * i);

                //check if the click was within any of the button hitboxes
                if (event.getX() > btnX
                        && event.getY() > btnY
                        && event.getX() < (btnX + MAIN_MENU_CREATE.getWidth(null))
                        && event.getY() < (btnY + MAIN_MENU_CREATE.getHeight(null))) {

                    //if a click output that
                    if (eventType == 1) {
                        drawButtonSelection = i;
                       
                    //check the mouse position
                    } if (eventType == 0) { 
                        System.out.println("Click! On button: " + i);
                        
                        //also select that button
                        drawButtonSelection = i;
                    }

                }

            }
        }
        
        repaint();
    }

    private void drawMenu(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        //add the background image
        g2d.drawImage(MAIN_MENU_BACKGROUND, 0, 0, null);

        //add the button images
        //add the create button
        g2d.drawImage(MAIN_MENU_CREATE,
                super.getWidth() / 2 - (MAIN_MENU_CREATE.getWidth(null) / 2),
                super.getHeight() / 2, null);

        //add the join button
        g2d.drawImage(MAIN_MENU_JOIN,
                super.getWidth() / 2 - (MAIN_MENU_CREATE.getWidth(null) / 2),
                super.getHeight() / 2 + (MAIN_MENU_CREATE.getHeight(null)) + 10, null);

        //add the settings button
        g2d.drawImage(MAIN_MENU_SETTINGS,
                super.getWidth() / 2 - (MAIN_MENU_CREATE.getWidth(null) / 2),
                super.getHeight() / 2 + (MAIN_MENU_CREATE.getHeight(null) * 2) + (10 * 2), null);
        
        //draw the selection overlay on the button
        if (drawButtonSelection != -1) {
            g2d.setColor(new Color(0, 0, 0, 128));
            g2d.fillRect(super.getWidth() / 2 - (MAIN_MENU_CREATE.getWidth(null) / 2),
                super.getHeight() / 2 + (MAIN_MENU_CREATE.getHeight(null) * drawButtonSelection) + (10 * drawButtonSelection),
                MAIN_MENU_CREATE.getWidth(null),
                MAIN_MENU_CREATE.getHeight(null));
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
