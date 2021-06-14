/*
 * Lukas Krampitz
 * Jun 14, 2021
 * A refenece class for all resources files so that they can be located even within a compiled .jar
 */
package resources;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Tacitor
 */
public class ResourcesRef {
    
    public final static Image MAIN_MENU_BACKGROUND = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/background.png")).getImage();
    public final static Image MAIN_MENU_SETTINGS = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/settings.png")).getImage();
    public final static Image MAIN_MENU_CREATE = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/createGame.png")).getImage();
    public final static Image MAIN_MENU_JOIN = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/joinGame.png")).getImage();
    
}
