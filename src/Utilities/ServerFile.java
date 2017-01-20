package Utilities;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFile extends Thread {

    private String fileName;
    private String sender;
    private String savePath;
    private ServerSocket serverSocket;
    private int port;

    private String address;

    public ServerFile(String sender, String fileName, String savePath) {
        this.fileName = fileName;
        this.sender = sender;
        this.savePath = savePath;
        this.port = 9000;
        try {
            serverSocket = new ServerSocket(port);
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Listen for a connection request
            Socket socket = serverSocket.accept();

            // Receive file
            InputStream inputStream = socket.getInputStream();
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream outputStream = new BufferedOutputStream(fos);

            byte[] buffer = new byte[16 * 1024];
            int count;

            // Write the file
            while ((count = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, count);
            }
            outputStream.flush();

            // Close I/O
            inputStream.close();
            fos.close();
            outputStream.close();

            transferFile();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Send file away
    public void transferFile() {
        try {
            Socket receive = serverSocket.accept();

            if (receive != null) {
                File file = new File(fileName);
                int count;
                byte[] bytes = new byte[(int) file.length()];

                FileInputStream inputFile = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(inputFile);
                OutputStream outStream = receive.getOutputStream();

                while ((count = bis.read(bytes)) >= 0) {
                    outStream.write(bytes, 0, count);
                }
                outStream.flush();

                inputFile.close();
                outStream.close();
                receive.close();
            }

        } catch (IOException e) {
            System.out.println("Cannot transfer file to client");
        }
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }
}
