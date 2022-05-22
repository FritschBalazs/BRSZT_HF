package CurveFever.gui;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.*;

public class MenuScreen extends JPanel {
    private static final int width = 1280;
    private static final int height = 720;
    private static final Color BACKROUNDCOLOR = new Color(26, 72, 98);
    private static final Color BUTTONCOLOR1 = new Color(228, 113, 250);
    private static final Color BUTTONCOLOR2 = new Color(3, 252, 217);
    private static final Color BUTTONCOLOR3 = new Color(252, 186, 83, 255);
    private  int numOfPlayers;

    JPanel centerPanel; //holds the buttons
    JPanel settingsPanel; //holds the settings
    JLabel footer;
    JLabel menuLogo;
    JButton createGameButton;
    JButton joinGameButton;
    JButton exitButton;
    JRadioButton arrowsButton;
    JRadioButton ADbutton;
    JTextField playerNameTextField;
    JTextField IPTextField;
    JComboBox numOfPlayersComboBox;

    public MenuScreen() {
        //Basic settings for screen
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(width, height));
        numOfPlayers = 0;

        //Creating and setting up center panel
        centerPanel = new JPanel();
        centerPanel.setBackground(BACKROUNDCOLOR);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //Creating and setting up settings panel
        settingsPanel = new JPanel();
        settingsPanel.setBackground(BUTTONCOLOR2);
        settingsPanel.setLayout(new GridBagLayout());

        //Creating and setting components for center panel
        menuLogo = new JLabel();
        menuLogo.setIcon(new ImageIcon("src/menulogo.png"));

        createGameButton = new JButton();
        createGameButton.setPreferredSize(new Dimension(300, 70));
        createGameButton.setFont(new Font("Lato", Font.BOLD, 15));
        createGameButton.setForeground(BACKROUNDCOLOR);
        createGameButton.setBackground(BUTTONCOLOR2);
        createGameButton.setFocusable(false);
        createGameButton.setText("Create Game");

        joinGameButton = new JButton();
        joinGameButton.setPreferredSize(new Dimension(300, 70));
        joinGameButton.setFont(new Font("Lato", Font.BOLD, 15));
        joinGameButton.setForeground(BACKROUNDCOLOR);
        joinGameButton.setBackground(BUTTONCOLOR1);
        joinGameButton.setFocusable(false);
        joinGameButton.setText("Join Game");

        exitButton = new JButton();
        exitButton.setPreferredSize(new Dimension(300, 70));
        exitButton.setFont(new Font("Lato", Font.BOLD, 15));
        exitButton.setForeground(BACKROUNDCOLOR);
        exitButton.setBackground(BUTTONCOLOR3);
        exitButton.setFocusable(false);
        exitButton.setText("Exit");

        //Creating and setting components for settings panel
        playerNameTextField = new JTextField();
        playerNameTextField.setPreferredSize(new Dimension(250, 40));
        playerNameTextField.setFont(new Font("Lato", Font.PLAIN, 20));
        playerNameTextField.setText("Player Name");
        playerNameTextField.setToolTipText("Player Name");
        playerNameTextField.setHorizontalAlignment(JTextField.CENTER);
        playerNameTextField.setSelectedTextColor(Color.MAGENTA);

        IPTextField = new JTextField();
        IPTextField.setPreferredSize(new Dimension(250, 40));
        IPTextField.setFont(new Font("Lato", Font.PLAIN, 20));
        IPTextField.setText("Server IP");
        IPTextField.setToolTipText("Server IP to Join Game");
        IPTextField.setHorizontalAlignment(JTextField.CENTER);
        IPTextField.setSelectedTextColor(Color.MAGENTA);

        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new GridBagLayout());
        radioButtonPanel.setPreferredSize(new Dimension(250,40));
        JLabel radioButtonTextLabel = new JLabel();
        radioButtonTextLabel.setFont(new Font("Lato", Font.PLAIN, 20));
        radioButtonTextLabel.setText("Control: ");
        arrowsButton = new JRadioButton();
        arrowsButton.setFont(new Font("Lato", Font.PLAIN, 15));
        arrowsButton.setText("Arrows");
        ADbutton = new JRadioButton();
        ADbutton.setFont(new Font("Lato", Font.PLAIN, 15));
        ADbutton.setText("A/D");
        ButtonGroup buttonGroup = new ButtonGroup(); //maybe kivinni tagváltozónak
        buttonGroup.add(arrowsButton);
        buttonGroup.add(ADbutton);
        radioButtonPanel.add(radioButtonTextLabel);
        radioButtonPanel.add(arrowsButton);
        radioButtonPanel.add(ADbutton);

        Integer numOfPlayersForComboBox[] = {2, 3, 4};
        numOfPlayersComboBox = new JComboBox(numOfPlayersForComboBox);
        JPanel numOfPlayersPanel = new JPanel();
        numOfPlayersPanel.setLayout(new GridBagLayout());
        JLabel numOfPlayersTextLabel = new JLabel(); //szépíteni
        numOfPlayersTextLabel.setFont(new Font("Lato", Font.PLAIN, 20));
        numOfPlayersTextLabel.setText("Number of players: ");
        numOfPlayersPanel.add(numOfPlayersTextLabel);
        numOfPlayersPanel.add(numOfPlayersComboBox);


        //Adding logo and buttons to center panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(25, 0, 100, 0);
        centerPanel.add(menuLogo, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(25, 0, 25, 0);
        centerPanel.add(createGameButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        centerPanel.add(joinGameButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        centerPanel.add(exitButton, constraints);

        //Adding components to settings panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(25, 20, 25, 20);
        settingsPanel.add(playerNameTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        settingsPanel.add(IPTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        settingsPanel.add(radioButtonPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        settingsPanel.add(numOfPlayersPanel,constraints);



        //Adding center panel and settings panel to screen
        add(centerPanel, BorderLayout.CENTER);
        add(settingsPanel, BorderLayout.EAST);

        //Creating, setting and adding footer for screen
        footer = new JLabel();
        footer.setPreferredSize(new Dimension(width, 15));
        footer.setFont(new Font("Lato", Font.PLAIN, 10));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setHorizontalAlignment(CENTER);
        footer.setForeground(Color.black);
        footer.setBackground(Color.WHITE);
        footer.setOpaque(true);
        footer.setText("Kurve Fívör Inc. Copyright, All rights reserved");
        add(footer, BorderLayout.SOUTH);
    }
}