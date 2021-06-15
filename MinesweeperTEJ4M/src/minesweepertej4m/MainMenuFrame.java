/*
 * Ehsan Patel & Lukas Krampitz
 * 14-Jun-2021
 * The main menu of the MineSweeper Multiplayer Game.
 */
package minesweepertej4m;

import javax.swing.JFrame;

/**
 *
 * @author ehsan
 */
public class MainMenuFrame extends JFrame {
    
    private MainMenuPanel theMainMenuPanel;
    
    /**
     * Constructor
     */
    public MainMenuFrame(){
        initFrame();
    }
    
    /**
     * Set up the JFrame
     */
    private void initFrame() {
        /* Set the Windows 10 look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("[Client] " + "Error loading Windows Look and feel");
        }
        
        
        setTitle("Multiplayer MineSweeper");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        theMainMenuPanel = new MainMenuPanel(this); //creates a new blank game
        add(theMainMenuPanel); //adds it to the JFrame
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Open the main menu
        MainMenuFrame mainMenuFrame = new MainMenuFrame();
        
    }

    

}
