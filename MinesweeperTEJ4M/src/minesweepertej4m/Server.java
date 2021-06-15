/*
 * Lukas Krampitz
 * Mar 27, 2021
 * 
 */
package minesweepertej4m;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Tacitor
 */
public class Server {

    //The reciving socket
    private ServerSocket serverSocket;
    //the number of clients what have connected
    private int numClients;
    private int maxClients; //the number of clients that will connect

    //The array of clients
    private ServerSideConnection[] clients;

    //A starting String
    private String chat = "Hello from server\n";
    
    //refernce to the main menu
    MainMenuFrame mainMenu;

    /**
     * The constructor
     *
     * @param portNum
     * @param mainMenu
     */
    public Server(int portNum, MainMenuFrame mainMenu) {
        //no clients have connected yet
        numClients = 0;
        //save the number of clients that will connect
        maxClients = 2;
        
        this.mainMenu = mainMenu;

        //initialize the array
        clients = new ServerSideConnection[maxClients];

        //create the socket to listen 
        try {
            serverSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            System.out.println("[Server] " + "IOException from server contructor");
        }
    }
    
    /**
     * Count one of the clients disconnecting
     */
    public void decrementClientCount() {
        numClients--;
        
        //check if we have hit 0
        if (numClients == 0) {
            //exit out
            mainMenu.setVisible(true);
        } 
    }

    public void acceptConnections() {
        try {
            System.out.println("[Server] " + "Waiting for connections...");
            //wait until all the clients have connected
            while (numClients < maxClients) {
                //create a reciving socket on the server side
                Socket s = serverSocket.accept();
                //count it as a client
                numClients++;
                System.out.println("[Server] " + "Client #" + numClients + " has connected");
                //create a new SSC for to keep track of that incoming socket
                ServerSideConnection ssc = new ServerSideConnection(s, numClients, this);

                //save that new ssc to the list of clients
                clients[numClients - 1] = ssc;

                //start a new thread just for that one client
                Thread t = new Thread(ssc);
                t.setDaemon(true);
                t.start();
            }
            System.out.println("[Server] " + "We now have " + maxClients + " players. No more connections will be accepted.");
        } catch (IOException e) {
            System.out.println("[Server] " + "IOException from acceptConnections");
        }
    }

    private void updateChat(String newMsg) {
        chat += newMsg;
    }

    private void clearChat() {
        chat = "";
    }

    private class ServerSideConnection implements Runnable {

        private Socket socket; //the socket that this client connected with
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        private int clientID;

        private Server serverRef; //reference to the server this SSC belongs to

        /**
         * Constructor
         *
         * @param socket
         * @param id
         */
        public ServerSideConnection(Socket socket, int id, Server serverRef) {
            this.socket = socket;
            clientID = id;
            this.serverRef = serverRef;
            //setup the data streams
            try {
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("[Server] " + "IOException from SSC constuctor for client#" + id);
            }
        }

        public int getID() {
            return clientID;
        }

        @Override
        public void run() {
            try {
                //when a client first connects send it's ID and the chat in it's current state
                dataOut.writeInt(clientID);
                dataOut.writeInt(maxClients); //the the client how many clients there will be
                dataOut.writeUTF(chat);
                dataOut.flush(); //send it

                //check if this is the last client to connect
                if (clientID == maxClients) {
                    //then tell the first client to begin
                    clients[0].sendBoolean(true);

                    System.out.println("[Server] " + "Send begin command to Client 1");
                }

                //loop state after all startup business is complete
                while (true) {
                    //accept a message
                    int type = dataIn.readInt(); //get the type of transmision

                    //if the client sent a chat message
                    if (type == 1) {

                        //read the chat message
                        String newMsg = dataIn.readUTF();

                        //check if a user wants to clear the chat
                        if (newMsg.equals("/clear")) {
                            clearChat();
                            updateChat("[Server] Client #" + clientID + " cleared chat\n");

                        } else { //else add the new string

                            //if a message come from client 1
                            updateChat("Client #" + clientID + ": " + newMsg + "\n");
                        }
                        //send the new chat out to all the clients
                        for (ServerSideConnection client : clients) {
                            client.sendNewString(chat);
                        }

                        //debug the chat
                        //System.out.println("[Server] " +"Chat is now: \"\n" + chat + "\" chat end.");
                    } else if (type == 2) { //if the client sent a file

                        //tell all the clients about the file
                        updateChat("[Server] Client #" + clientID + " has sent a file to everyone's SettlerDevs folder\n");

                        //and read in the length from the socket
                        int fileLength = dataIn.readInt();

                        //read in the file name and extension
                        String fileName = dataIn.readUTF();

                        //create byte array to store the file
                        byte[] fileAsStream = new byte[fileLength];

                        //read in the actual content of the file
                        int count = 0;
                        while (count < fileLength) { //do it in steps as the packets of data cannot reach the buffer instantly with large files
                            //read in the portion that has not been read yet
                            int bytesRead = dataIn.read(fileAsStream, count, fileAsStream.length - count);
                            System.out.println("[Server] " + "bytesRead: " + bytesRead);
                            if (bytesRead == -1) {
                                System.out.println("[Server] " + "didn't get a complete file");
                            }
                            count += bytesRead;
                        }

                        //debug the file that was sent
                        //System.out.println("[Server] " + Arrays.toString(fileAsStream));
                        //send the new chat out to all the clients
                        for (ServerSideConnection client : clients) {
                            //debug how many times it was sent
                            //System.out.println("[Server] " +"Sent it");
                            //System.out.println("[Server] " +"\nCurrent chat is :\n" + chat);
                            client.sendFile(chat, fileAsStream, fileName);

                        }

                    }
                }
            } catch (IOException e) {
                System.out.println("[Server] " + "IOException from SSC run() for ID#" + clientID);
                
                //since there was a connection error this client it as good as gone and can be removed
                decrementClientCount();
            }
        }

        /**
         * Send a string to the client
         *
         * @param msg
         */
        public void sendNewString(String msg) {
            try {
                dataOut.writeInt(1); //tell the client they are reciving a chat message
                dataOut.writeUTF(msg);
                dataOut.flush();
            } catch (IOException e) {
                System.out.println("[Server] " + "IOException from SSC sendNewString()");
            }
        }

        /**
         * Send a file to the client. Also update their chat so they know to
         * check for a new file
         *
         * @param msg
         * @param fileData
         */
        public void sendFile(String msg, byte[] fileData, String fileName) {
            try {
                dataOut.writeInt(2); //tell the client they are reciving a file and chat message
                dataOut.writeUTF(msg);
                dataOut.writeInt(fileData.length); //send the length of the file
                dataOut.writeUTF(fileName); //send the file name
                dataOut.write(fileData, 0, fileData.length); //send the file
                dataOut.flush();
            } catch (IOException e) {
                System.out.println("[Server] " + "IOException from SSC sendNewString()");
            }
        }

        /**
         * Send a boolean to the client
         *
         * @param msg
         */
        public void sendBoolean(boolean msg) {
            try {
                dataOut.writeBoolean(msg);
                dataOut.flush();
            } catch (IOException e) {
                System.out.println("[Server] " + "IOException from SSC sendBoolean()");
            }
        }

    }

}
