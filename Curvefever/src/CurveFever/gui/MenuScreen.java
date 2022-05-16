package CurveFever.gui;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;

public class MenuScreen extends JPanel {
    private static final int width = 1280;
    private static final int height = 720;

    JPanel buttonHolder; //holds the buttons
    JLabel footer;
    JButton createGameButton;
    JButton joinGameButton;
    JButton exitButton;

    public MenuScreen(){
        setLayout(new BorderLayout(0,0));
        setPreferredSize(new Dimension(width,height));
        setBackground(Color.GRAY); //elvileg nem látszik

        buttonHolder = new JPanel();
        buttonHolder.setBackground(Color.magenta);
        buttonHolder.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();



        createGameButton = new JButton();
        createGameButton.setPreferredSize(new Dimension(300,70));
        createGameButton.setText("Create Game");

        joinGameButton = new JButton();
        joinGameButton.setPreferredSize(new Dimension(300,70));
        joinGameButton.setText("Join Game");

        exitButton = new JButton();
        exitButton.setPreferredSize(new Dimension(300,70));
        exitButton.setText("Exit");

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(25,0,25,0);
        buttonHolder.add(createGameButton,c);

        c.gridx = 1;
        c.gridy = 1;
        buttonHolder.add(joinGameButton,c);

        c.gridx = 1;
        c.gridy = 2;
        buttonHolder.add(exitButton,c);

        add(buttonHolder,BorderLayout.CENTER);

        footer = new JLabel();
        footer.setPreferredSize(new Dimension(width,15));
        footer.setFont(new Font("Lato", Font.PLAIN, 10));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setHorizontalAlignment(CENTER);
        footer.setForeground(Color.black);
        footer.setBackground(Color.WHITE);
        footer.setOpaque(true);
        footer.setText("Kurve Fíver Inc. Copyright, All rights reserved");
        add(footer,BorderLayout.SOUTH);
    }
}
