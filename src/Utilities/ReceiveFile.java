package Utilities;

import ClientSide.ClientGUI;

import java.io.*;
import java.net.Socket;

public class ReceiveFile implements Runnable {

    private String address;
    private int port;
    private String savePath;

    public ReceiveFile(int port, String address, String savePath) {
        this.address = address;
        this.port = port;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        try {
            Socket sock = new Socket(address, port);

            int count;
            byte[] buffer = new byte[16 * 1024];

            InputStream inputStream = sock.getInputStream();
            FileOutputStream fos = new FileOutputStream(savePath);
            BufferedOutputStream outputStream = new BufferedOutputStream(fos);

            // Write the file
            while ((count = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, count);
            }
            outputStream.flush();

            // Close I/O
            inputStream.close();
            fos.close();
            outputStream.close();

//            ClientGUI.appendChat("File received \n");
        } catch (IOException ex) {
            System.out.println("Cannot receive file");
        }
    }
}
