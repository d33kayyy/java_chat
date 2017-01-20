package ClientSide;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.List;

public class ClientGUI extends JFrame {
    /* UI components */

    protected static JList usersJlist = new JList();
    private JTextField input = new JTextField(30);
    private JButton send = new JButton("Send");
    private JButton logInOut = new JButton("Login");
    private JButton signUp = new JButton("Sign up");
    private JButton block = new JButton("Block");
    private JButton unblock = new JButton("Unblock");
    private JButton sendFileButton = new JButton("Send File");
    private JButton selectFileButton = new JButton("Browse File");
    private JTextField filePath = new JTextField();
    private JLabel people = new JLabel("People in chat", SwingConstants.CENTER);
    private JPanel enterChatPn, showChatPn, leftPn, rightPn, centerPn, buttons;
    private JLabel address = new JLabel(" Address:");
    protected JTextField insertAdd = new JTextField(8);
    private JLabel port = new JLabel(" Port:");
    protected JTextField insertPort = new JTextField(8);
    private JLabel user = new JLabel(" Logged in as:");
    protected JTextField showUserName = new JTextField(8);
    private JComboBox status;
    private JTextField username = new JTextField();
    private JPasswordField password = new JPasswordField();
    private JLabel statusLb = new JLabel("Status:");
    private JButton smile, blink, broken, cool, cry, heart, kiss, laugh, lmao, sad, shock, shy, teeth;
    public static DefaultStyledDocument document = new DefaultStyledDocument();

    /* Connection components */
    private Socket socket = null;
    private DataOutputStream outputToServer;
    private DataInputStream inputFromServer;
    private Thread threadChat;
    private String path;
    protected static List<String> blockList = new ArrayList<String>();

    /* Constructor */
    public ClientGUI() {
        setLayout(new BorderLayout());

        /* Top panel */
        insertPort.setText("8000");
        insertAdd.setText("localhost");
        insertAdd.setHorizontalAlignment(JTextField.CENTER);
        insertPort.setHorizontalAlignment(JTextField.CENTER);

        showUserName.setEditable(false);
        showUserName.setHorizontalAlignment(JTextField.CENTER);

        status = new JComboBox();
        status.addItem("Online");
        status.addItem("Busy");
        status.addItem("Away");
        status.setEnabled(false);

        FlowLayout flow = new FlowLayout();
        flow.setHgap(10);

        JPanel topPn = new JPanel(flow);
        topPn.add(address);
        topPn.add(insertAdd);
        topPn.add(port);
        topPn.add(insertPort);
        topPn.add(user);
        topPn.add(showUserName);
        topPn.add(statusLb);
        topPn.add(status);

        /* Text area display the messages */
        leftPn = new JPanel(new BorderLayout());

        Style style = document.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontSize(style, 14);
        StyleConstants.setLeftIndent(style, 4);

        JTextPane textPane = new JTextPane(document);
        textPane.setEditable(false);
        textPane.setFont(new Font("Tahoma", Font.PLAIN, 14));

        DefaultCaret caret = (DefaultCaret) textPane.getCaret(); // The scroll move automatically to the last line
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        showChatPn = new JPanel(new BorderLayout());
        showChatPn.setBorder(new EmptyBorder(5, 10, 5, 10));
        showChatPn.add(scroll, BorderLayout.CENTER);

        leftPn.add(showChatPn, BorderLayout.CENTER);

        /* Message input and Send */
        enterChatPn = new JPanel(new BorderLayout());
        enterChatPn.setBorder(new EmptyBorder(3, 10, 6, 10));

        // Set up icons
        Icon smileIcon = new ImageIcon("src/res/img/smile.gif");
        smile = new JButton(smileIcon);
        setButtonLook(smile);
        smile.setActionCommand("smile");
        smile.addActionListener(new EmoticonListener());

        Icon blinkIcon = new ImageIcon("src/res/img/blink.gif");
        blink = new JButton(blinkIcon);
        setButtonLook(blink);
        blink.setActionCommand("blink");
        blink.addActionListener(new EmoticonListener());

        Icon brokenIcon = new ImageIcon("src/res/img/broken.gif");
        broken = new JButton(brokenIcon);
        setButtonLook(broken);
        broken.setActionCommand("broken");
        broken.addActionListener(new EmoticonListener());

        Icon coolIcon = new ImageIcon("src/res/img/cool.gif");
        cool = new JButton(coolIcon);
        setButtonLook(cool);
        cool.setActionCommand("cool");
        cool.addActionListener(new EmoticonListener());

        Icon cryIcon = new ImageIcon("src/res/img/cry.gif");
        cry = new JButton(cryIcon);
        setButtonLook(cry);
        cry.setActionCommand("cry");
        cry.addActionListener(new EmoticonListener());

        Icon heartIcon = new ImageIcon("src/res/img/heart.gif");
        heart = new JButton(heartIcon);
        setButtonLook(heart);
        heart.setActionCommand("heart");
        heart.addActionListener(new EmoticonListener());

        Icon kissIcon = new ImageIcon("src/res/img/kiss.gif");
        kiss = new JButton(kissIcon);
        setButtonLook(kiss);
        kiss.setActionCommand("kiss");
        kiss.addActionListener(new EmoticonListener());

        Icon laughIcon = new ImageIcon("src/res/img/laugh.gif");
        laugh = new JButton(laughIcon);
        setButtonLook(laugh);
        laugh.setActionCommand("laugh");
        laugh.addActionListener(new EmoticonListener());

        Icon lmaoIcon = new ImageIcon("src/res/img/lmao.gif");
        lmao = new JButton(lmaoIcon);
        setButtonLook(lmao);
        lmao.setActionCommand("lmao");
        lmao.addActionListener(new EmoticonListener());

        Icon sadIcon = new ImageIcon("src/res/img/sad.gif");
        sad = new JButton(sadIcon);
        setButtonLook(sad);
        sad.setActionCommand("sad");
        sad.addActionListener(new EmoticonListener());

        Icon shockIcon = new ImageIcon("src/res/img/shock.gif");
        shock = new JButton(shockIcon);
        setButtonLook(shock);
        shock.setActionCommand("shock");
        shock.addActionListener(new EmoticonListener());

        Icon shyIcon = new ImageIcon("src/res/img/shy.gif");
        shy = new JButton(shyIcon);
        setButtonLook(shy);
        shy.setActionCommand("shy");
        shy.addActionListener(new EmoticonListener());

        Icon teethIcon = new ImageIcon("src/res/img/teeth.gif");
        teeth = new JButton(teethIcon);
        setButtonLook(teeth);
        teeth.setActionCommand("teeth");
        teeth.addActionListener(new EmoticonListener());

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

        send.setEnabled(false);
        input.setEnabled(false);
        enterChatPn.add(emoticonPn, BorderLayout.NORTH);
        enterChatPn.add(input, BorderLayout.CENTER);
        enterChatPn.add(send, BorderLayout.EAST);

        leftPn.add(enterChatPn, BorderLayout.SOUTH);

        /* Buttons */
        logInOut.setActionCommand("Login");

        buttons = new JPanel(new GridLayout(0, 1, 10, 10));
        buttons.add(logInOut);
        buttons.add(signUp);
        buttons.add(block);
        buttons.add(unblock);
        block.setEnabled(false);
        unblock.setEnabled(false);
        buttons.setBorder(new EmptyBorder(5, 5, 5, 10));

        rightPn = new JPanel(new BorderLayout());
        rightPn.add(buttons, BorderLayout.NORTH);

        /* Show users in chat*/
        people.setBorder(new EmptyBorder(0, 0, 5, 0));

        JScrollPane scrollPane = new JScrollPane(usersJlist);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        centerPn = new JPanel(new BorderLayout());
        centerPn.setBorder(new EmptyBorder(10, 5, 5, 10));
        centerPn.add(people, BorderLayout.NORTH);
        centerPn.add(scrollPane, BorderLayout.CENTER);

        rightPn.setPreferredSize(new Dimension(120, 300));
        rightPn.add(centerPn, BorderLayout.CENTER);

        /* File panel*/
        FlowLayout layout = new FlowLayout();
        layout.setVgap(2);

        JPanel filePn = new JPanel(layout);
        sendFileButton.setEnabled(false);
        selectFileButton.setEnabled(false);
        filePath.setEditable(false);
        filePath.setPreferredSize(new Dimension(330, 20));
        filePn.add(filePath);
        filePn.add(selectFileButton);
        filePn.add(sendFileButton);
        filePn.setBorder(new EmptyBorder(0, 10, 5, 20));

        /* */
        add(topPn, BorderLayout.NORTH);
        add(leftPn, BorderLayout.CENTER);
        add(rightPn, BorderLayout.EAST);
        add(filePn, BorderLayout.SOUTH);

        /* Add listeners*/
        send.addActionListener(new SendMessage());
        input.addActionListener(new SendMessage());
        logInOut.addActionListener(new LogListener());
        signUp.addActionListener(new SignUpListener());
        sendFileButton.addActionListener(new SendFileListener());
        selectFileButton.addActionListener(new SelectFileListener());
        status.addActionListener(new StatusListener());
        block.addActionListener(new BlockListener());
        unblock.addActionListener(new UnblockListener());
        this.addWindowListener(new MyWindowListener());

        this.setResizable(false);
        this.setTitle("Chat room");
        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    /* Button Listeners */
    private class LogListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            boolean validatePort = true;

            if (insertAdd.getText().trim().equals("") || insertPort.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter Host address and Port number", "Invalid host", JOptionPane.OK_OPTION);
                validatePort = false;
            }

            if (action.equals("Login") && validatePort) {

                Object[] message = {"Username", username, "Password", password};

                boolean valid = false;
                boolean ready = false;
                boolean pass = false;

                // Pop up window to register
                // Validate id + password
                while (!valid) {
                    int k = JOptionPane.showOptionDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (k == JOptionPane.OK_OPTION) {
                        if (username.getText().length() < 6 || password.getText().length() < 6) {
                            JOptionPane.showMessageDialog(null, "Invalid username or password. Please enter again", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                        } else {
                            Scanner readfile = null;

                            try {
                                readfile = new Scanner(new File("src/res/users.txt"));
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            while (readfile.hasNext()) {

                                String line = readfile.nextLine();
                                String[] array = line.split("\t");

                                if (array[0].equals(username.getText()) && array[1].equals(password.getText())) {
                                    pass = true;
                                    break;
                                } else if (!readfile.hasNext()) {
                                    JOptionPane.showMessageDialog(null, "Wrong username or password. Please enter again", "Login Failed", JOptionPane.WARNING_MESSAGE);
                                    pass = false;
                                }
                            }
                            if (pass) {
                                valid = true;
                                ready = true;
                            }
                        }
                    } else {
                        valid = true;
                    }
                }
                // Login Successfully

                if (valid == true && ready == true) {
                    if (connect()) {
                        String msg = "login" + "!:" + username.getText();
                        try {
                            // send message to server
                            outputToServer.writeUTF(msg);
                            outputToServer.flush();
                        } catch (Exception e1) {
                            System.out.println("");
                        }

                        showUserName.setText(username.getText());
                        setUI(true, "Logout");
                    }
                }

            } else if (action.equals("Logout")) {
                // Close the socket and delete from usersJlist

                String message = "logout" + "!:" + username.getText();
                try {
                    // send message to server
                    outputToServer.writeUTF(message);
                    outputToServer.flush();
                } catch (IOException e1) {
                    System.out.println("Log out error. Please close the program and start again");
                }
                showUserName.setText("");
                setUI(false, "Login");
                sendFileButton.setEnabled(false);

                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                appendChat("You have disconnected from server \n");
            }
        }
    }

    private class SignUpListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField username = new JTextField();
            JPasswordField password = new JPasswordField();
            Object[] message = {"Username", username, "Password", password};

            boolean valid = false;
            boolean ready = false; // ready to write to file
            boolean exist = false;

            /**
             * Pop up window to register Validate id + password Check existed
             * account
             *
             */
            while (!valid) {
                int k = JOptionPane.showOptionDialog(null, message, "Sign Up", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (k == JOptionPane.OK_OPTION) {
                    if (username.getText().length() < 6 || password.getText().length() < 6) {
                        JOptionPane.showMessageDialog(null, "Your username and password must be at least 6 characters length", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    } else {
                        Scanner readfile = null;

                        try {
                            readfile = new Scanner(new File("users.txt"));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        while (readfile.hasNext()) {

                            String line = readfile.nextLine();
                            String[] array = line.split("\t");

                            if (array[0].equals(username.getText())) {
                                JOptionPane.showMessageDialog(null, "This username is taken. Please enter another username", "Username existed", JOptionPane.WARNING_MESSAGE);
                                exist = true;
                                break;
                            } else {
                                exist = false;
                            }
                        }
                        if (!exist) {
                            valid = true;
                            ready = true;
                        }
                    }
                } else {
                    valid = true;
                }
            }

            // Store the account to the system
            if (valid == true && ready == true) {
                JOptionPane.showMessageDialog(null, "Sign up successfully!", "Success!", JOptionPane.PLAIN_MESSAGE);
                PrintWriter output = null;
                try {
                    output = new PrintWriter(new FileWriter("users.txt", true));
                    output.println(username.getText() + "\t" + password.getText());
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                } finally {
                    if (output != null) {
                        output.close();
                    }
                }
            }
        }
    }

    private class SendMessage implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String textInput = input.getText();
            String message = "";

            if (!textInput.trim().equals("")) {
                // message = type!:sender!:content
                ArrayList<String> selectionList = new ArrayList<String>();
                ArrayList<String> receiverList = new ArrayList<String>();

                if (!usersJlist.getSelectedValuesList().isEmpty()) {
                    for (int i = 0; i < usersJlist.getSelectedValuesList().size(); i++) {
                        String selection = usersJlist.getSelectedValuesList().get(i).toString();
                        String array[] = selection.split(" - ");
                        String receiver = array[0];
                        selectionList.add(selection);
                        receiverList.add(receiver);
                    }
                    if (!receiverList.contains(username.getText())) {
                        message = "message" + "!:" + username.getText() + "!:" + textInput + "!:" + selectionList;

                        appendChat("<Private message to " + receiverList + "> " + username.getText() + ": " + textInput + "\n");

                    } else {
                        JOptionPane.showMessageDialog(null, "You cannot send message to yourself");
                    }

                } else {
                    message = "message" + "!:" + username.getText() + "!:" + textInput;
                }
                try {
                    // send message to server
                    outputToServer.writeUTF(message);
                    outputToServer.flush();
                    input.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            usersJlist.clearSelection();
        }
    }

    private class SelectFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Browse a file");
            int returnVal = fileChooser.showOpenDialog(selectFileButton);
            File file = fileChooser.getSelectedFile();

            if (returnVal == JFileChooser.APPROVE_OPTION && socket.isConnected()) {

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
            String message = "";

            if (usersJlist.getSelectedValue() != null) {

                String receiver = usersJlist.getSelectedValue().toString();
                if (!receiver.equals(username.getText())) {
                    message = "requestSave" + "!:" + username.getText() + "!:" + file.getName() + "!:" + receiver + "!:" + path;
                } else {
                    JOptionPane.showMessageDialog(null, "You cannot send file to yourself");
                }

            } else {
                message = "requestSave" + "!:" + username.getText() + "!:" + file.getName() + "!:" + "server" + "!:" + path;
            }
            try {
                outputToServer.writeUTF(message);
                outputToServer.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class MyWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            // Close the socket and delete user from usersJList
            String message = "logout" + "!:" + username.getText();
            try {
                if (!socket.isClosed()) {
                    if (socket.isConnected()) {
                        // send message to server
                        outputToServer.writeUTF(message);
                        outputToServer.flush();
                        socket.close();
                    } else {
                        System.exit(0);
                    }
                }
            } catch (Exception ex) {
                System.out.println("Closed");
            } finally {
                System.exit(0);
            }

        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }

    private class StatusListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String stat = status.getSelectedItem().toString();
            String message = "changeStatus" + "!:" + username.getText() + "!:" + stat;

            try {
                // send message to server
                outputToServer.writeUTF(message);
                outputToServer.flush();
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
            if (usersJlist.getSelectedValue() != null) {
                String receiver = usersJlist.getSelectedValue().toString();
                message = "emoticon" + "!:" + username.getText() + "!:" + e.getActionCommand() + "!:" + receiver;

                appendChat("<Private message to " + receiver + "> ");
                insertEmoticon(username.getText(), e.getActionCommand());

            } else {
                message = "emoticon" + "!:" + username.getText() + "!:" + e.getActionCommand();
            }
            try {
                // send message to server
                outputToServer.writeUTF(message);
                outputToServer.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private class BlockListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (usersJlist.getSelectedValue() != null) {
                String selection = usersJlist.getSelectedValue().toString();
                String blocker = selection.split(" - ")[0];
                if (!username.getText().equals(blocker)) {
                    if (!blockList.isEmpty()) {
                        if (!blockList.contains(blocker)) {
                            blockList.add(blocker);
                        }
                    } else {
                        blockList.add(blocker);
                    }
                    JOptionPane.showMessageDialog(null, blocker + " has been BLOCKED!", "Success", JOptionPane.PLAIN_MESSAGE);
                    appendChat(blocker + " has been BLOCKED!" + "\n");
                } else {
                    JOptionPane.showMessageDialog(null, "You cannot block/unblock yourself!", "Alert", JOptionPane.PLAIN_MESSAGE);
                }
            }
            usersJlist.clearSelection();

        }
    }

    private class UnblockListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (usersJlist.getSelectedValue() != null) {
                String selection = usersJlist.getSelectedValue().toString();
                String unblocker = selection.split(" - ")[0];
                if (!username.getText().equals(unblocker)) {
                    if (!blockList.isEmpty()) {
                        if (blockList.contains(unblocker)) {
                            blockList.remove(unblocker);
                            JOptionPane.showMessageDialog(null, unblocker + " has been UNBLOCKED!", "Success", JOptionPane.PLAIN_MESSAGE);
                            appendChat(unblocker + " has been UNBLOCKED!" + "\n");
                        } else {
                            JOptionPane.showMessageDialog(null, "There is no " + unblocker + " in your Block List", "Alert", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You cannot block/unblock yourself!", "Alert", JOptionPane.PLAIN_MESSAGE);
                }
            }
            usersJlist.clearSelection();
        }
    }

    /* Methods */
    // Set UI when login/logout
    public void setUI(boolean isLogin, String logout) {
        selectFileButton.setEnabled(isLogin);
        send.setEnabled(isLogin);
        input.setEnabled(isLogin);
        signUp.setEnabled(!isLogin);
        block.setEnabled(isLogin);
        unblock.setEnabled(isLogin);
        status.setEnabled(isLogin);
        smile.setEnabled(isLogin);
        blink.setEnabled(isLogin);
        broken.setEnabled(isLogin);
        cool.setEnabled(isLogin);
        cry.setEnabled(isLogin);
        heart.setEnabled(isLogin);
        kiss.setEnabled(isLogin);
        laugh.setEnabled(isLogin);
        lmao.setEnabled(isLogin);
        sad.setEnabled(isLogin);
        shock.setEnabled(isLogin);
        shy.setEnabled(isLogin);
        smile.setEnabled(isLogin);
        teeth.setEnabled(isLogin);
        insertPort.setEditable(!isLogin);
        insertAdd.setEditable(!isLogin);
        logInOut.setText(logout);
        logInOut.setActionCommand(logout);
    }

    // Set button
    public void setButtonLook(JButton button) {
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setEnabled(false);
    }

    // Connect to server
    public boolean connect() {
        try {
            socket = new Socket(insertAdd.getText().trim(), Integer.parseInt(insertPort.getText().trim()));
            appendChat("Connected to server" + "\n");

            // Create I/O streams
            inputFromServer = new DataInputStream(socket.getInputStream());
            outputToServer = new DataOutputStream(socket.getOutputStream());

            threadChat = new ClientHandler(socket);
            threadChat.start();
            return true;
        } catch (Exception e) {
            appendChat("Cannot connect to server" + "\n");
            return false;
        }

    }

    // Insert Emoticon
    public static void insertEmoticon(String userSend, String str) {
        Style labelStyle = document.getStyle(StyleContext.DEFAULT_STYLE);

        String image = str + ".gif";
        Icon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        StyleConstants.setComponent(labelStyle, label);

        try {
            document.insertString(document.getLength(), userSend + " : ", null);
            document.insertString(document.getLength(), "Ignored", labelStyle);
            document.insertString(document.getLength(), "\n", null);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    // Add message to chat
    public static void appendChat(String str) {
        try {
            document.insertString(document.getLength(), str, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println("");
        }
        new ClientGUI();
    }

}
