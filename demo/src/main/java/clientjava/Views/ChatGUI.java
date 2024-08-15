package clientjava.Views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import clientjava.Domain.Entity.ChatMessage;
import clientjava.Domain.Port.ChatControllerPort;
import clientjava.Domain.Port.ChatDataPort;
import clientjava.Domain.Port.ChatRouterPort;
import clientjava.Infrastructure.Controller.ChatController;
import clientjava.Infrastructure.Model.Transaction;
import clientjava.Infrastructure.Router.ChatRouter;

public class ChatGUI extends JFrame implements ChatDataPort {

    private JTextField messageField;
    private JList<String> userList;
    private DefaultListModel<String> userModel;

    private JPanel messagePanel;
    private String username;

    ChatControllerPort controller;
    ChatRouterPort router;

    public ChatGUI() {
        InitializeGUI();
    }

    public void Init(){
        controller = new ChatController(
                this
        );

        router = new ChatRouter(controller);
        if (router.Init()) {
            this.setVisible(true);

            username = JOptionPane.showInputDialog(null,
                "Enter your username:",
                "User");
                
            router.Write(new Transaction("Connect",username));
        }
    }

    private void InitializeGUI() {
        setTitle("Java Chat Client");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        messagePanel = new JPanel(new BorderLayout());
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        messagePanel.add(new JLabel("Chat", JLabel.CENTER), BorderLayout.NORTH);

        sendButton.addActionListener((ActionEvent e) -> {
            sendMessage();
        });

        userModel = new DefaultListModel<>();
        userList = new JList<>(userModel);
        JScrollPane userScrollPane = new JScrollPane(userList);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Users", JLabel.CENTER), BorderLayout.NORTH);
        rightPanel.add(userScrollPane, BorderLayout.CENTER);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(rightPanel, BorderLayout.EAST);   
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        
        if (!message.isEmpty()) {
            ChatMessage mss = new ChatMessage(username, message);
            this.AddMessage(mss);
            router.Write(new Transaction("Message", mss));
            messageField.setText("");
        }
    }

    @Override
    public void AddUsername(String username) {
        userModel.addElement(username);
    }

    @Override
    public void AddUsernames(String[] usernames) {
        for (String u : usernames) {
            userModel.addElement(u);    
        }
    }

    @Override
    public void RemoveUsername(String username) {
        userModel.removeElement(username);
    }

    @Override
    public void AddMessage(ChatMessage message) {
        JLabel messageLabel = new JLabel(message.getUsername() + ": " + message.getBody());
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagePanel.add(messageLabel);

        messagePanel.revalidate();
        messagePanel.repaint();
    }
}
