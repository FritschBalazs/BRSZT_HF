package CurveFever.gui;

import javax.swing.*;
import java.awt.*;

import static java.util.Arrays.sort;
import static java.util.Collections.reverseOrder;

public class InfoPanel extends JPanel {
    private double[] Scores;
    private double[] ScoresToDraw;
    private String[] PlayerNames;
    private String[] PlayerNamesToDraw;
    private int roundNum;
    private int currentRound;
    private Color[] Colors;
    private Color[] ColorsToDraw;
    private JLabel[] ScoreLabels;
    private JLabel roundLabel;
    private static final Color BACKGROUND = new Color(128, 164, 252);
    private static final Color BUTTONCOLOR2 = new Color(3, 252, 217);
    private static final Color BUTTONCOLOR1 = new Color(84, 88, 255, 255);

    private int numOfPlayers;
    public InfoPanel(int width, int height) { //TODO (D) pls ne legyen ket konsturktor mert csunyan megzavart -B
        this.setPreferredSize(new Dimension(width,height));
    }
    public InfoPanel() {
        this.numOfPlayers = 4;
        this.PlayerNames = new String[numOfPlayers];
        this.Colors = new Color[numOfPlayers];

        this.ColorsToDraw = new Color[numOfPlayers];
        this.ScoresToDraw = new double[numOfPlayers];
        this.PlayerNamesToDraw = new String[numOfPlayers];

        this.Scores = new double[numOfPlayers];
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        roundLabel = new JLabel();
        roundLabel.setFont(new Font("Lato", Font.BOLD, 25));
        roundLabel.setForeground(Color.white);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 20, 50, 20);
        add(roundLabel, constraints);
        //add(roundLabel);
        ScoreLabels = new JLabel[numOfPlayers];
        constraints.insets = new Insets(15, 20, 15, 20);
        for (int i = 0; i < numOfPlayers; i = i + 1){
            ScoreLabels[i] = new JLabel();
            ScoreLabels[i].setFont(new Font("Lato", Font.BOLD, 20));
            constraints.gridy = i+1;
            add(ScoreLabels[i],constraints);
            PlayerNames[i] = " ";
        }
    }
    public double[] getScores() {return Scores;}

    public void setScores(double[] scores) {
        Scores = scores;}

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public void setPlayerNames(String[] playerNames) {
        PlayerNames = playerNames.clone();
    }

    public void setColors(Color[] colors) {
        Colors = colors;
    }
    public String[] getPlayerNames() {
        return PlayerNames;
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

    public void drawScore() {
        roundLabel.setText("Round "+currentRound+"/"+roundNum);
        sortScores();
        for (int i = 0; i < numOfPlayers; i = i + 1) {
            ScoreLabels[i].setText(i+1+". "+PlayerNamesToDraw[i]+": "+ ScoresToDraw[i]);
            ScoreLabels[i].setForeground(ColorsToDraw[i]);
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(BUTTONCOLOR1);
        drawScore();
        Toolkit.getDefaultToolkit().sync(); // this smooths out animations on some systems
    }
}
