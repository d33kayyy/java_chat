package ClientSide;

import Utilities.ReceiveFile;
import Utilities.SendFile;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket socket;
    private DataInputStream inputFromServer;
    private ClientGUI gui;

    public ClientHandler(Socket socket, ClientGUI gui) {
        this.socket = socket;
        this.gui = gui;
    }

    public void run() {
        boolean keep = true;
        try {
            // Create data input and output streams
            inputFromServer = new DataInputStream(socket.getInputStream());

            while (true) {
                if (!socket.isConnected() || socket.isClosed()) {
                    break;
                }
                String message = inputFromServer.readUTF();
                String splitMsg[] = message.split("!:");

                if (splitMsg[0].equals("refresh")) {
                    // Refresh user list
                    String temp = splitMsg[1].replace("[", "");
                    String temp2 = temp.replace("]", "");
                    String userlist[] = temp2.split(", ");

                    gui.usersJlist.setListData(userlist);

                } else if (splitMsg[0].equals("requestSave")) {
                    // Receive file request
                    int confirm = JOptionPane.showConfirmDialog(null, "Save file [" + splitMsg[2] + "] from " + splitMsg[1] + "?");
                    if (confirm == JOptionPane.OK_OPTION) {
                        // Pop up save dialog
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Save file");
                        fileChooser.setSelectedFile(new File(splitMsg[2]));
                        int returnVal = fileChooser.showSaveDialog(null);

                        String savePath = fileChooser.getSelectedFile().getPath();
                        String msg;

                        if (returnVal == JFileChooser.APPROVE_OPTION && savePath != null) {
                            // Start receiving file
                            ReceiveFile receiveFile = new ReceiveFile(Integer.parseInt(splitMsg[3]), splitMsg[4], savePath);
                            Thread receive = new Thread(receiveFile);
                            receive.start();
                        }
                    } else {
                        appendChat("File transfer declined\n");
                    }

                } else if (splitMsg[0].equals("acceptFile")) {
                    // Receive confirmation => Start sending file
                    // message = type!:sender!:port!:hostAddress!:filePath
                    Thread threadFile = new SendFile(Integer.parseInt(splitMsg[2]), splitMsg[3], splitMsg[4]);
                    threadFile.start();

                } else if (splitMsg[0].equals("emoticon")) {
                    // Receive emoticon
                    String blocker = splitMsg[1];
                    if (splitMsg.length == 4) {

                        if (!gui.blockList.contains(blocker)) {
                            appendChat("<Private message to " + splitMsg[3] + "> ");
                            gui.insertEmoticon(splitMsg[1], splitMsg[2]);
                        }
                    } else {
                        if (!gui.blockList.contains(blocker)) {
                            gui.insertEmoticon(splitMsg[1], splitMsg[2]);
                        }
                    }

                } else if (splitMsg[0].equals("fileFromServer")) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Save file [" + splitMsg[1] + "] from " + "Server" + "?");
                    if (confirm == JOptionPane.OK_OPTION) {
                        // Pop up save dialog
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Save file");
                        fileChooser.setSelectedFile(new File(splitMsg[1]));
                        int returnVal = fileChooser.showSaveDialog(null);

                        String savePath = fileChooser.getSelectedFile().getPath();

                        if (returnVal == JFileChooser.APPROVE_OPTION && savePath != null) {
                            // Start receiving file
                            ReceiveFile receiveFile = new ReceiveFile(10000, splitMsg[2], savePath);
                            Thread receive = new Thread(receiveFile);
                            receive.start();
                        }
                    } else {
                        appendChat("File transfer declined\n");
                    }
                } else {
                    // Receive normal message
                    String blocker = "";
                    String messageHead = splitMsg[0].split(" : ")[0];
                    if (messageHead.contains("<Private message to ") && messageHead.contains("> ")) {
                        blocker = messageHead.split("> ")[1];
                    } else {
                        blocker = messageHead;
                    }
                    if (!gui.blockList.contains(blocker)) {
                        appendChat(message);
                    }
                }


            }
        } catch (IOException ex) {
            ex.printStackTrace();
            appendChat("Connection to server lost \n");
        }
    }

    public void appendChat(String str) {
        try {
            gui.document.insertString(gui.document.getLength(), str, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    protected void close() {
        try {
            // Close input stream and socket
            if (inputFromServer != null) inputFromServer.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
