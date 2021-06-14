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
    //Menu Resources
    public final static Image MAIN_MENU_BACKGROUND = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/background.png")).getImage();
    public final static Image MAIN_MENU_SETTINGS = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/settings.png")).getImage();
    public final static Image MAIN_MENU_CREATE = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/createGame.png")).getImage();
    public final static Image MAIN_MENU_JOIN = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/joinGame.png")).getImage();
    public final static Image MAIN_MENU_SELECTED = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/buttonSelectionOverlay.png")).getImage();
    
    
    //Game screen Resources
    public final static Image NAV_SETTINGS = new ImageIcon(ResourcesRef.class.getResource("images/navigation/settings.png")).getImage();
    public final static Image NAV_SCOUT = new ImageIcon(ResourcesRef.class.getResource("images/navigation/scout_bomb.png")).getImage().getScaledInstance(80, 80, Image.SCALE_AREA_AVERAGING);
    public final static Image NAV_FLAG = new ImageIcon(ResourcesRef.class.getResource("images/navigation/flag.png")).getImage().getScaledInstance(57, 80, Image.SCALE_AREA_AVERAGING);
    public final static Image NAV_PLACE = new ImageIcon(ResourcesRef.class.getResource("images/navigation/place_bomb.png")).getImage().getScaledInstance(80, 80, Image.SCALE_AREA_AVERAGING);
    public final static Image NAV_QUIT = new ImageIcon(ResourcesRef.class.getResource("images/navigation/quit.png")).getImage().getScaledInstance(296, 80, Image.SCALE_AREA_AVERAGING);
    
}
