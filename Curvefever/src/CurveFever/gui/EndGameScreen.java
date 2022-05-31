package CurveFever.gui;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;

public class EndGameScreen extends JPanel {
    private static final int width = 1280;
    private static final int height = 720;
    private static final Color BACKROUNDCOLOR = new Color(26, 72, 98);
    private static final Color BUTTONCOLOR1 = new Color(228, 113, 250);
    private static final Color SIDEPANELCOLOR = new Color(84, 88, 255, 255);
    private static final Color BUTTONCOLOR2 = new Color(3, 252, 217);
    private static final Color BUTTONCOLOR3 = new Color(252, 186, 83, 255);
    private String[] PlayerNames;
    private Color[] Colors;
    private double[] Scores;
    private int numOfPlayers;
    JButton backToMenuButton;
    JButton playAgainButton;
    JLabel waitingTextLabel;
    private JLabel[] ScoreLabels;

    public EndGameScreen() {
        this.numOfPlayers = 4;
        this.PlayerNames = new String[numOfPlayers];
        this.Colors = new Color[numOfPlayers];
        this.Scores = new double[numOfPlayers];
        this.ScoreLabels = new JLabel[numOfPlayers];
        setPreferredSize(new Dimension(width,height));
        //setBackground(BACKROUNDCOLOR);
        setLayout(new BorderLayout());
        //Creating and setting up center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(BACKROUNDCOLOR);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel endGameLogo = new JLabel();
        endGameLogo.setIcon(new ImageIcon("src/endgamescreenlogo.png"));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(25, 0, 100, 0);
        centerPanel.add(endGameLogo, constraints);

        for (int i = 0; i < numOfPlayers; i = i + 1){
            ScoreLabels[i] = new JLabel();
            //ScoreLabels[i].setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            ScoreLabels[i].setFont(new Font("Lato", Font.BOLD, 30));
            add(ScoreLabels[i]);
            constraints.gridx = 0;
            constraints.gridy = i+2;
            constraints.insets = new Insets(25, 20, 25, 20);
            centerPanel.add(ScoreLabels[i],constraints);
            PlayerNames[i] = "";
        }


        //Creating and setting up sidepanel
        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(SIDEPANELCOLOR);
        sidePanel.setLayout(new GridBagLayout());


        backToMenuButton = new JButton();
        backToMenuButton.setPreferredSize(new Dimension(300, 70));
        backToMenuButton.setFont(new Font("Lato", Font.BOLD, 15));
        backToMenuButton.setForeground(BACKROUNDCOLOR);
        backToMenuButton.setBackground(BUTTONCOLOR2);
        backToMenuButton.setFocusable(false);
        backToMenuButton.setText("Back to Menu");

        playAgainButton = new JButton();
        playAgainButton.setPreferredSize(new Dimension(300, 70));
        playAgainButton.setFont(new Font("Lato", Font.BOLD, 15));
        playAgainButton.setForeground(BACKROUNDCOLOR);
        playAgainButton.setBackground(BUTTONCOLOR1);
        playAgainButton.setFocusable(false);
        playAgainButton.setText("Play again");

        waitingTextLabel = new JLabel(); //szépíteni
        waitingTextLabel.setFont(new Font("Lato", Font.BOLD, 20));
        waitingTextLabel.setText("Waiting other players...");
        waitingTextLabel.setVisible(false);

        //Adding components to side panel
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(25, 20, 25, 20);
        sidePanel.add(backToMenuButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        sidePanel.add(playAgainButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.insets = new Insets(50, 20, 25, 20);
        sidePanel.add(waitingTextLabel, constraints);

        //Adding center panel and side panel to screen
        add(centerPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        //Creating, setting and adding footer for screen
        JLabel footer = new JLabel();
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

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void setPlayerNames(String[] playerNames) {
        PlayerNames = playerNames.clone();
    }

    public void setColors(Color[] colors) {
        Colors = colors.clone();
    }

    public void setScores(double[] scores) {
        Scores = scores;
    }
    public void sortScores () {
        double tempScore;
        String tempName;
        for (int i = 0; i < Scores.length; i++) {
            for (int j = i + 1; j < Scores.length; j++) {
                if (Scores[i] < Scores[j]) {
                    tempScore = Scores[i];
                    tempName = PlayerNames[i];
                    Scores[i] = Scores[j];
                    Scores[j] = tempScore;
                    PlayerNames[i] = PlayerNames[j];
                    PlayerNames[j] = tempName;
                }
            }
        }
    }

    public void render(){
        sortScores();
        for (int i = 0; i < numOfPlayers; i = i + 1) {
            ScoreLabels[i].setText(i+1+". "+PlayerNames[i]+": "+ Scores[i]);
            ScoreLabels[i].setForeground(Colors[i]);
        }
    }
}
