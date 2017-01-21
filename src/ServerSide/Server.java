package ServerSide;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends JFrame {

    private ServerGUI serverGUI;
    protected static ArrayList<Socket> socketArray = new ArrayList<Socket>();
    public static ArrayList<String> userList = new ArrayList<String>();

    public Server(ServerGUI gui) {
        this.serverGUI = gui;

        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(8000);
            ServerGUI.systemLog.append("Server started at " + new Date() + "\n");
            ServerGUI.systemLog.append("Host Address: " + InetAddress.getLocalHost().getHostAddress() + "\t Port:" + serverSocket.getLocalPort() + "\n");

            while (true) {
                // Listen for a connection request
                Socket socket = serverSocket.accept();
                socketArray.add(socket);
                System.out.println("Client connected from: " + socket.getLocalAddress().getHostName() + "/" + socket.getPort());

                // Create thread for new client
                ServerHandler serverHandler = new ServerHandler(socket, serverGUI);
                serverHandler.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new Server(new ServerGUI());
    }
}
