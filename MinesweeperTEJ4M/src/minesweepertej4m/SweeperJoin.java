/*
 * Lukas Krampitz
 * Jun 15, 2021
 * A basic GUI used to connect to an existing Multiplayer Minesweeper server
 */
package minesweepertej4m;

/**
 *
 * @author Tacitor
 */
public class SweeperJoin extends javax.swing.JFrame {

    MainMenuFrame menuRef;
    Server sweeperServer;
    Client client;
    
    gameScreenFrame theGame;

    /**
     * Creates new form SweeperCreate
     *
     * @param m
     */
    public SweeperJoin(MainMenuFrame m) {
        menuRef = m;
        MainMenuFrame.setIcon(this);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLbl = new javax.swing.JLabel();
        portLbl = new javax.swing.JLabel();
        portInfoLbl = new javax.swing.JLabel();
        portTxtFld = new javax.swing.JTextField();
        joinBtn = new javax.swing.JButton();
        ipLbl = new javax.swing.JLabel();
        ipInfoLbl = new javax.swing.JLabel();
        ipTxtFld = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Multiplayer MineSweeper");

        titleLbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        titleLbl.setText("Join a Minesweeper Game Server");

        portLbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        portLbl.setText("Listening port:");

        portInfoLbl.setText("What port should the server be searched for on? (25570 recomended)");

        portTxtFld.setText("25570");
        portTxtFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portTxtFldActionPerformed(evt);
            }
        });

        joinBtn.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        joinBtn.setText("Join Game");
        joinBtn.setName("Multiplayer Minesweeper"); // NOI18N
        joinBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinBtnActionPerformed(evt);
            }
        });

        ipLbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ipLbl.setText("IP Address:");

        ipInfoLbl.setText("What is the IP or URL of the server? (donau.ca for dev server)");

        ipTxtFld.setText("localhost");
        ipTxtFld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ipTxtFldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleLbl)
                            .addComponent(portLbl)
                            .addComponent(portInfoLbl)
                            .addComponent(portTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(joinBtn))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ipLbl)
                            .addComponent(ipInfoLbl)
                            .addComponent(ipTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(portLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portInfoLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ipLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ipInfoLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ipTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(joinBtn)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void portTxtFldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portTxtFldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_portTxtFldActionPerformed

    private void joinBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinBtnActionPerformed
        // hide this window
        this.setVisible(false);

        FindServerRunnable findServerRunnable = new FindServerRunnable();
        findServerRunnable.setDaemon(true); //allow the JVM to close this thread
        findServerRunnable.start();

    }//GEN-LAST:event_joinBtnActionPerformed

    private void ipTxtFldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ipTxtFldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ipTxtFldActionPerformed

      private void findServer() {
        if (client == null) {
            //create a new client
            client = new Client(700, 200, ipTxtFld.getText(), Integer.parseInt(portTxtFld.getText()));
        }
        //create a CatanClient and connect to the ip specified
        try {

            //try to connect
            client.connectToServer();
            client.setUpGUI();
            
            //hand the client off to the Game Screen
            theGame = new gameScreenFrame(client);
            
            //set the gameScreenFrame attribute in the Client
            client.setGameScreenFrame(theGame);
            
            client.setUpButton();
            
        } catch (Exception e) {
            System.out.println("Error connecting to server: \n" + e);
        }
    }

    private class FindServerRunnable extends Thread implements Runnable {

        private boolean stopRequested = false;

        public synchronized void requestStop() {
            stopRequested = true;
        }

        @Override
        public void run() {
            //debug the life of the thread and how long it lives for
            //System.out.println("Started connectio attempt");

            //check if this thread should stop
            while (!stopRequested) {
                //try to connect
                findServer();
                //only run once
                stopRequested = true;
            }

            //System.out.println("done connection attempt");
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ipInfoLbl;
    private javax.swing.JLabel ipLbl;
    private javax.swing.JTextField ipTxtFld;
    private javax.swing.JButton joinBtn;
    private javax.swing.JLabel portInfoLbl;
    private javax.swing.JLabel portLbl;
    private javax.swing.JTextField portTxtFld;
    private javax.swing.JLabel titleLbl;
    // End of variables declaration//GEN-END:variables
}
