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
    private final static Image MAIN_MENU_SETTINGS = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/settings.png")).getImage();
    private final static Image MAIN_MENU_CREATE = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/createGame.png")).getImage();
    private final static Image MAIN_MENU_JOIN = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/joinGame.png")).getImage();
    //hovered buttons
    private final static Image MAIN_MENU_SETTINGS_HOVER = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/hoverSettings.png")).getImage();
    private final static Image MAIN_MENU_CREATE_HOVER = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/hoverCreate.png")).getImage();
    private final static Image MAIN_MENU_JOIN_HOVER = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/hoverJoin.png")).getImage();
    //bordered buttons
    private final static Image MAIN_MENU_SETTINGS_BORDER = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/clickSettings.png")).getImage();
    private final static Image MAIN_MENU_CREATE_BORDER = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/clickCreate.png")).getImage();
    private final static Image MAIN_MENU_JOIN_BORDER = new ImageIcon(ResourcesRef.class.getResource("images/MainMenu/clickJoin.png")).getImage();
    
    public final static Image[] MAIN_MENU_REG = new Image[]{MAIN_MENU_CREATE, MAIN_MENU_JOIN, MAIN_MENU_SETTINGS};
    public final static Image[] MAIN_MENU_HOVER = new Image[]{MAIN_MENU_CREATE_HOVER, MAIN_MENU_JOIN_HOVER, MAIN_MENU_SETTINGS_HOVER};
    public final static Image[] MAIN_MENU_CLICK = new Image[]{MAIN_MENU_CREATE_BORDER, MAIN_MENU_JOIN_BORDER, MAIN_MENU_SETTINGS_BORDER};
    
    
    //Game screen Resources
    public final static Image NAV_SETTINGS = new ImageIcon(ResourcesRef.class.getResource("images/navigation/settings.png")).getImage();
    public final static Image NAV_SCOUT = new ImageIcon(ResourcesRef.class.getResource("images/navigation/scout_bomb.png")).getImage().getScaledInstance(80, 80, Image.SCALE_AREA_AVERAGING);
    public final static Image NAV_FLAG = new ImageIcon(ResourcesRef.class.getResource("images/navigation/flag.png")).getImage().getScaledInstance(57, 80, Image.SCALE_AREA_AVERAGING);
    public final static Image NAV_PLACE = new ImageIcon(ResourcesRef.class.getResource("images/navigation/place_bomb.png")).getImage().getScaledInstance(80, 80, Image.SCALE_AREA_AVERAGING);
    public final static Image NAV_QUIT = new ImageIcon(ResourcesRef.class.getResource("images/navigation/quit.png")).getImage().getScaledInstance(296, 80, Image.SCALE_AREA_AVERAGING);
    
    public final static Image GAME_FLAG = new ImageIcon(ResourcesRef.class.getResource("images/gamePanel/flag2.png")).getImage().getScaledInstance(31, 40, Image.SCALE_AREA_AVERAGING);
    public final static Image GAME_BOMB = new ImageIcon(ResourcesRef.class.getResource("images/gamePanel/bomb.png")).getImage().getScaledInstance(46, 46, Image.SCALE_AREA_AVERAGING);

}
