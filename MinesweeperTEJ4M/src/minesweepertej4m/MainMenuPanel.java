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
public class MainMenuPanel extends JPanel  implements ActionListener, MouseMotionListener{

    private MainMenuFrame mainMenuFrameRef;

    
    /**
     * Constructor
     * @param m 
     */
    public MainMenuPanel(MainMenuFrame m) {
        mainMenuFrameRef = m;

        //add a mouse Listener
        addMouseListener(new MouseInputAdapter() {
            /**
             * Trigger when the user clicks the Main menu Panel.
             *
             * @param evt The event representing the mouse click
            */ 
            @Override
            public final void mouseReleased(MouseEvent evt) {
                //send the mouse event to the click handeler
                mouseClick(evt);
            }
            
            @Override
            public final void mouseMoved(MouseEvent evt) {
                //send the mouse event to the click handeler
                mouseMovement(evt);
            }
        });

    }
    
    public void mouseMovement(MouseEvent event) {
        System.out.println("Moved!");
        
    }

    /**
     * Method to handle when the user clicks on the main menu
     *
     * @param event
     */
    public void mouseClick(MouseEvent event) {
        System.out.println("Clicked!");

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

                System.out.println("Click! On button: " + i);

            }

        }

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
        System.out.println("Dragged");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println("Moved");
    }

}
