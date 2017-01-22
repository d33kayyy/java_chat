package Utilities;

import ClientSide.ClientGUI;

import javax.swing.text.BadLocationException;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class SendFile extends Thread {

    private Socket sock;
    private String filepath;

    public SendFile(int port, String hostAddress, String filepath) {
        try {
            this.sock = new Socket(InetAddress.getByName(hostAddress), port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.filepath = filepath;
    }

    @Override
    public void run() {
        try {
            File file = new File(filepath);
            int count;
            byte[] bytes = new byte[(int) file.length()];

            FileInputStream inputFile = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(inputFile);
            OutputStream outStream = sock.getOutputStream();

            while ((count = bis.read(bytes)) >= 0) {
                outStream.write(bytes, 0, count);
            }
            outStream.flush();

//            try {
////                ClientGUI.document.insertString(ClientGUI.document.getLength(), "File " + file.getName() + " is sent \n", null);
//            } catch (BadLocationException e) {
//                e.printStackTrace();
//            }

            // Close I/O
            inputFile.close();
            bis.close();
            outStream.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
