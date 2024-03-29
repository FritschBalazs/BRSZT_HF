package CurveFever.gui;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;

public class EndGameScreen extends JPanel {
    private static final int width = 1280;
    private static final int height = 720;
    private static final Color DARKBLUE = new Color(26, 72, 98);
    private static final Color PINK = new Color(228, 113, 250);
    private static final Color BLUE = new Color(84, 88, 255, 255);
    private static final Color CYAN = new Color(3, 252, 217);
    private String[] PlayerNames;
    private String[] PlayerNamesToDraw;
    private Color[] Colors;
    private Color[] ColorsToDraw;
    private double[] Scores;
    private double[] ScoresToDraw;
    private int numOfPlayers;
    JButton backToMenuButton;
    JButton playAgainButton;
    JLabel waitingTextLabel;
    private final JLabel[] ScoreLabels;

    public EndGameScreen() {
        this.numOfPlayers = 4;
        this.PlayerNames = new String[numOfPlayers];
        this.Colors = new Color[numOfPlayers];
        this.Scores = new double[numOfPlayers];
        this.ScoreLabels = new JLabel[numOfPlayers];
        setPreferredSize(new Dimension(width,height));
        setLayout(new BorderLayout());

        //Creating and setting up center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(DARKBLUE);
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
        sidePanel.setBackground(BLUE);
        sidePanel.setLayout(new GridBagLayout());

        backToMenuButton = new JButton();
        backToMenuButton.setPreferredSize(new Dimension(300, 70));
        backToMenuButton.setFont(new Font("Lato", Font.BOLD, 15));
        backToMenuButton.setForeground(DARKBLUE);
        backToMenuButton.setBackground(CYAN);
        backToMenuButton.setFocusable(false);
        backToMenuButton.setText("Back to Menu");

        playAgainButton = new JButton();
        playAgainButton.setPreferredSize(new Dimension(300, 70));
        playAgainButton.setFont(new Font("Lato", Font.BOLD, 15));
        playAgainButton.setForeground(DARKBLUE);
        playAgainButton.setBackground(PINK);
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

    public JLabel getWaitingTextLabel() {
        return waitingTextLabel;
    }

    public void sortScores () {
        double tempScore;
        String tempName;
        Color tempColor;
        ScoresToDraw = Scores.clone();
        PlayerNamesToDraw = PlayerNames.clone();
        ColorsToDraw = Colors.clone();
        for (int i = 0; i < numOfPlayers; i++) {
            for (int j = i + 1; j < numOfPlayers; j++) {
                if (ScoresToDraw[i] < ScoresToDraw[j]) {
                    tempScore = ScoresToDraw[i];
                    tempName = PlayerNamesToDraw[i];
                    tempColor = ColorsToDraw[i];
                    ScoresToDraw[i] = ScoresToDraw[j];
                    ScoresToDraw[j] = tempScore;
                    PlayerNamesToDraw[i] = PlayerNamesToDraw[j];
                    PlayerNamesToDraw[j] = tempName;
                    ColorsToDraw[i] = ColorsToDraw[j];
                    ColorsToDraw[j] = tempColor;
                }
            }
        }
    }

    public void render(){
        sortScores();
        for (int i = 0; i < numOfPlayers; i = i + 1) {
            ScoreLabels[i].setText(i+1+". "+PlayerNamesToDraw[i]+": "+ (int)Math.floor(ScoresToDraw[i]));
            ScoreLabels[i].setForeground(ColorsToDraw[i]);
        }
    }
}
