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
    //background
    public final static Image MAIN_MENU_BACKGROUND = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/background.png")).getImage();
    //regular buttons
    private final static Image MAIN_MENU_CREATE = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/createGame.png")).getImage();
    private final static Image MAIN_MENU_JOIN = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/joinGame.png")).getImage();
    //hovered buttons
    private final static Image MAIN_MENU_CREATE_HOVER = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/hoverCreate.png")).getImage();
    private final static Image MAIN_MENU_JOIN_HOVER = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/hoverJoin.png")).getImage();
    //bordered buttons
    private final static Image MAIN_MENU_CREATE_BORDER = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/clickCreate.png")).getImage();
    private final static Image MAIN_MENU_JOIN_BORDER = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/clickJoin.png")).getImage();
    //arrays containing the images for the main menu
    public final static Image[] MAIN_MENU_REG = new Image[]{MAIN_MENU_CREATE, MAIN_MENU_JOIN};
    public final static Image[] MAIN_MENU_HOVER = new Image[]{MAIN_MENU_CREATE_HOVER, MAIN_MENU_JOIN_HOVER};
    public final static Image[] MAIN_MENU_CLICK = new Image[]{MAIN_MENU_CREATE_BORDER, MAIN_MENU_JOIN_BORDER};
    
    
    //Game screen Resources
    public final static Image NAV_RESTART = new ImageIcon(ResourcesRef.class.getResource("images/navigation/restart.png")).getImage();
    public final static Image NAV_SCOUT = new ImageIcon(ResourcesRef.class.getResource("images/navigation/scout_bomb.png")).getImage();
    public final static Image NAV_FLAG = new ImageIcon(ResourcesRef.class.getResource("images/navigation/flag.png")).getImage();
    public final static Image NAV_PLACE = new ImageIcon(ResourcesRef.class.getResource("images/navigation/place_bomb.png")).getImage();
    public final static Image NAV_QUIT = new ImageIcon(ResourcesRef.class.getResource("images/navigation/quit.png")).getImage();
    
    //Icons for elemnts on the dynamic board
    public final static Image GAME_FLAG = new ImageIcon(ResourcesRef.class.getResource("images/gamePanel/flag2.png")).getImage();
    public final static Image GAME_BOMB = new ImageIcon(ResourcesRef.class.getResource("images/gamePanel/bomb.png")).getImage();

}
