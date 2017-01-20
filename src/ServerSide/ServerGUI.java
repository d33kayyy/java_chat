package ServerSide;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ServerGUI extends JFrame {

    protected static JList usersJlist = new JList();
    private JTextField input = new JTextField(30);
    private JButton send = new JButton("Send");
    private JButton sendFileButton = new JButton("Send File");
    private JButton selectFileButton = new JButton("Browse File");
    private JTextField filePath = new JTextField();
    private JPanel enterChatPn, showChatPn, mainPn, leftPn;
    public static DefaultStyledDocument showChat = new DefaultStyledDocument();
    public static JTextArea systemLog = new JTextArea();
    private JButton smile, blink, broken, cool, cry, heart, kiss, laugh, lmao, sad, shock, shy, teeth;
    private String path;

    public ServerGUI() {
        setLayout(new BorderLayout());

        mainPn = new JPanel(new BorderLayout());

        JLabel chat = new JLabel("Chat Box", SwingConstants.CENTER);
        chat.setBorder(new EmptyBorder(7, 0, 0, 0));
        mainPn.add(chat, BorderLayout.NORTH);

        /* Chat Box : display the messages */
        Style style = showChat.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontSize(style, 14);
        StyleConstants.setLeftIndent(style, 4);

        JTextPane textPane = new JTextPane(showChat);
        textPane.setEditable(false);
        textPane.setFont(new Font("Tahoma", Font.PLAIN, 14));

        DefaultCaret caret = (DefaultCaret) textPane.getCaret(); // The scroll move automatically to the last line
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        showChatPn = new JPanel(new BorderLayout());
        showChatPn.setBorder(new EmptyBorder(5, 10, 5, 10));
        showChatPn.add(scroll, BorderLayout.CENTER);

        mainPn.add(showChatPn, BorderLayout.CENTER);

        /* Message input and Send */
        enterChatPn = new JPanel(new BorderLayout());
        enterChatPn.setBorder(new EmptyBorder(3, 10, 6, 10));

//         Set up icons
        Icon smileIcon = new ImageIcon("src/res/img/smile.gif");
        smile = new JButton(smileIcon);
        smile.setActionCommand("smile");
        setButtonLook(smile);
        smile.addActionListener(new EmoticonListener());

        Icon blinkIcon = new ImageIcon("src/res/img/blink.gif");
        blink = new JButton(blinkIcon);
        blink.setActionCommand("blink");
        setButtonLook(blink);
        blink.addActionListener(new EmoticonListener());

        Icon brokenIcon = new ImageIcon("src/res/img/broken.gif");
        broken = new JButton(brokenIcon);
        broken.setActionCommand("broken");
        setButtonLook(broken);
        broken.addActionListener(new EmoticonListener());

        Icon coolIcon = new ImageIcon("src/res/img/cool.gif");
        cool = new JButton(coolIcon);
        cool.setActionCommand("cool");
        setButtonLook(cool);
        cool.addActionListener(new EmoticonListener());

        Icon cryIcon = new ImageIcon("src/res/img/cry.gif");
        cry = new JButton(cryIcon);
        cry.setActionCommand("cry");
        setButtonLook(cry);
        cry.addActionListener(new EmoticonListener());

        Icon heartIcon = new ImageIcon("src/res/img/heart.gif");
        heart = new JButton(heartIcon);
        heart.setActionCommand("heart");
        setButtonLook(heart);
        heart.addActionListener(new EmoticonListener());

        Icon kissIcon = new ImageIcon("src/res/img/kiss.gif");
        kiss = new JButton(kissIcon);
        kiss.setActionCommand("kiss");
        setButtonLook(kiss);
        kiss.addActionListener(new EmoticonListener());

        Icon laughIcon = new ImageIcon("src/res/img/laugh.gif");
        laugh = new JButton(laughIcon);
        laugh.setActionCommand("laugh");
        setButtonLook(laugh);
        laugh.addActionListener(new EmoticonListener());

        Icon lmaoIcon = new ImageIcon("src/res/img/lmao.gif");
        lmao = new JButton(lmaoIcon);
        lmao.setActionCommand("lmao");
        setButtonLook(lmao);
        lmao.addActionListener(new EmoticonListener());

        Icon sadIcon = new ImageIcon("src/res/img/sad.gif");
        sad = new JButton(sadIcon);
        sad.setActionCommand("sad");
        setButtonLook(sad);
        sad.addActionListener(new EmoticonListener());

        Icon shockIcon = new ImageIcon("src/res/img/shock.gif");
        shock = new JButton(shockIcon);
        shock.setActionCommand("shock");
        setButtonLook(shock);
        shock.addActionListener(new EmoticonListener());

        Icon shyIcon = new ImageIcon("src/res/img/shy.gif");
        shy = new JButton(shyIcon);
        shy.setActionCommand("shy");
        setButtonLook(shy);
        shy.addActionListener(new EmoticonListener());

        Icon teethIcon = new ImageIcon("src/res/img/teeth.gif");
        teeth = new JButton(teethIcon);
        teeth.setActionCommand("teeth");
        teeth.addActionListener(new EmoticonListener());
        setButtonLook(teeth);

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(10);

        JPanel emoticonPn = new JPanel(flowLayout);
        emoticonPn.add(blink);
        emoticonPn.add(broken);
        emoticonPn.add(cool);
        emoticonPn.add(cry);
        emoticonPn.add(heart);
        emoticonPn.add(kiss);
        emoticonPn.add(laugh);
        emoticonPn.add(lmao);
        emoticonPn.add(sad);
        emoticonPn.add(teeth);
        emoticonPn.add(shock);
        emoticonPn.add(shy);
        emoticonPn.add(smile);

        enterChatPn.add(emoticonPn, BorderLayout.NORTH);
        enterChatPn.add(input, BorderLayout.CENTER);
        enterChatPn.add(send, BorderLayout.EAST);

        mainPn.add(enterChatPn, BorderLayout.SOUTH);

        /* Left Panel - System Log */
        JLabel log = new JLabel("System Log", SwingConstants.CENTER);
        log.setBorder(new EmptyBorder(0, 0, 5, 0));

        JScrollPane scrollLog = new JScrollPane(systemLog);
        scrollLog.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        systemLog.setEditable(false);
        systemLog.setPreferredSize(new Dimension(300, 200));
        systemLog.setLineWrap(true);
        systemLog.setWrapStyleWord(true);
        systemLog.setFont(new Font("Times New Roman", Font.PLAIN, 14));

        JPanel rightPn = new JPanel(new BorderLayout());
        rightPn.add(log, BorderLayout.NORTH);
        rightPn.add(scrollLog, BorderLayout.CENTER);
        rightPn.setBorder(new EmptyBorder(7, 5, 5, 10));

        /* Right Panel - Show users in chat */
        JLabel people = new JLabel("People in chat", SwingConstants.CENTER);
        people.setBorder(new EmptyBorder(0, 0, 5, 0));

        JScrollPane scrollPane = new JScrollPane(usersJlist);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        leftPn = new JPanel(new BorderLayout());
        leftPn.setPreferredSize(new Dimension(120, 300));
        leftPn.add(people, BorderLayout.NORTH);
        leftPn.add(scrollPane, BorderLayout.CENTER);
        leftPn.setBorder(new EmptyBorder(7, 5, 5, 5));

        /* File panel*/
        FlowLayout layout = new FlowLayout();
        layout.setVgap(2);

        JPanel filePn = new JPanel(layout);
        sendFileButton.setEnabled(false);
        selectFileButton.setEnabled(true);
        filePath.setEditable(false);
        filePath.setPreferredSize(new Dimension(330, 20));
        filePn.add(filePath);
        filePn.add(selectFileButton);
        filePn.add(sendFileButton);
        filePn.setBorder(new EmptyBorder(0, 10, 5, 20));

        /* */
        add(mainPn, BorderLayout.CENTER);
        add(leftPn, BorderLayout.WEST);
        add(filePn, BorderLayout.SOUTH);
        add(rightPn, BorderLayout.EAST);

        /* Add listeners*/
        send.addActionListener(new SendMessage());
        input.addActionListener(new SendMessage());
        sendFileButton.addActionListener(new SendFileListener());
        selectFileButton.addActionListener(new SelectFileListener());

        this.setResizable(false);
        this.setTitle("Server");
        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private class SendMessage implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Send message from server
                String textInput = input.getText();
                String message;
                if (!textInput.trim().equals("")) {
                    if (usersJlist.getSelectedValue() != null) {
                        String receiver = usersJlist.getSelectedValue().toString();
                        message = "<Private message to " + receiver + "> Server : " + textInput + "\n";
                        // Find user and send
                        send1User(receiver, message);
                    } else {
                        message = "Server : " + textInput + "\n";
                        ServerHandler.sendToAll(message);
                    }

                    try {
                        showChat.insertString(showChat.getLength(), message, null);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                    input.setText("");
                }
                usersJlist.clearSelection();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class EmoticonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // message = type!:sender!:emoticonActionCommand!:receiver
            String message;
            try {
                if (usersJlist.getSelectedValue() != null) {
                    String receiver = usersJlist.getSelectedValue().toString();
                    message = "emoticon" + "!:" + "Server" + "!:" + e.getActionCommand() + "!:" + receiver;

                    // Find user and send
                    send1User(receiver, message);
                    insertEmoticon("<Private message to " + receiver + "> Server", e.getActionCommand());

                } else {
                    message = "emoticon" + "!:" + "Server" + "!:" + e.getActionCommand();
                    ServerHandler.sendToAll(message);
                    insertEmoticon("Server", e.getActionCommand());
                }
                usersJlist.clearSelection();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class SelectFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Browse a file");
            int returnVal = fileChooser.showOpenDialog(selectFileButton);
            File file = fileChooser.getSelectedFile();

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                // Validate file type
                if (!(file.getName().contains(".txt") || file.getName().contains(".pdf")
                        || file.getName().contains(".java") || file.getName().contains(".zip"))) {
                    JOptionPane.showMessageDialog(null, "Only .pdf/.java/.txt/.zip formats are supported \n "
                            + "Please select another file", "Invalid File", JOptionPane.OK_OPTION);
                } else {
                    path = file.getAbsolutePath();
                    filePath.setText(path);
                    sendFileButton.setEnabled(true);
                }
            }
        }
    }

    private class SendFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = new File(path);
            // message = type!:sender!:fileName!:receiver!:filePath

            if (usersJlist.getSelectedValue() != null) {

                String receiver = usersJlist.getSelectedValue().toString();
                String message = "";

                try {
                    message = "fileFromServer" + "!:" + file.getName() + "!:" + InetAddress.getLocalHost().getHostAddress() + "!:" + path;
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }

                send1User(receiver, message);
                sendFile(path);
            } else {
                JOptionPane.showMessageDialog(null, "You must choose a client to send file");
            }
        }
    }

    private void sendFile(String path) {
        try {
            ServerSocket serverSocket = new ServerSocket(10000);
            Socket socket = serverSocket.accept();

            File file = new File(path);
            int count;
            byte[] bytes = new byte[(int) file.length()];

            FileInputStream inputFile = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(inputFile);
            OutputStream outStream = socket.getOutputStream();

            while ((count = bis.read(bytes)) >= 0) {
                outStream.write(bytes, 0, count);
            }
            outStream.flush();

            systemLog.append("File " + file.getName() + " is sent \n");

            // Close I/O
            inputFile.close();
            bis.close();
            outStream.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void insertEmoticon(String user, String str) {
        Style labelStyle = showChat.getStyle(StyleContext.DEFAULT_STYLE);

        String image = str + ".gif";
        Icon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        StyleConstants.setComponent(labelStyle, label);

        try {
            showChat.insertString(showChat.getLength(), user + " : ", null);
            showChat.insertString(showChat.getLength(), "Ignored", labelStyle);
            showChat.insertString(showChat.getLength(), "\n", null);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public void setButtonLook(JButton button) {
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Find specific user and send
    public void send1User(String user, String msg) {
        try {
            int position = Server.userList.indexOf(user);
            Socket tempSocket = Server.socketArray.get(position);

            DataOutputStream outputToClient = new DataOutputStream(tempSocket.getOutputStream());
            outputToClient.writeUTF(msg);
            outputToClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
