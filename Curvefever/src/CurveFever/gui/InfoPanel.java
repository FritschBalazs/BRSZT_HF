package CurveFever.gui;

import javax.swing.*;
import java.awt.*;

import static java.util.Arrays.sort;
import static java.util.Collections.reverseOrder;


//GamePanelen belül lesz!
public class InfoPanel extends JPanel {
    private double[] Scores;
    private String[] PlayerNames;
    private int roundNum;
    private int currentRound;
    private Color[] Colors;
    private JLabel[] ScoreLabels;
    private JLabel roundLabel;
    private static final Color BACKGROUND = new Color(128, 164, 252);
    private static final Color BUTTONCOLOR2 = new Color(3, 252, 217);

    private static final Color BUTTONCOLOR1 = new Color(72, 47, 155);

    private int numOfPlayers;
    public InfoPanel(int width, int height) { //TODO (D) pls ne legyen ket konsturktor mert csunyan megzavart -B
        this.setPreferredSize(new Dimension(width,height));
    }
    public InfoPanel() {
        this.numOfPlayers = 4;
        this.PlayerNames = new String[numOfPlayers];
        this.Colors = new Color[numOfPlayers];
        this.Scores = new double[numOfPlayers];
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        roundLabel = new JLabel();
        roundLabel.setFont(new Font("Lato", Font.BOLD, 20));
        roundLabel.setForeground(Color.white);
        roundLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roundLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        add(roundLabel);
        ScoreLabels = new JLabel[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i = i + 1){
            ScoreLabels[i] = new JLabel();
            ScoreLabels[i].setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            ScoreLabels[i].setFont(new Font("Lato", Font.PLAIN, 15));
            ScoreLabels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            add(ScoreLabels[i]);
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

    public void drawScore() {
        roundLabel.setText("Round "+currentRound+"/"+roundNum);
        sortScores();
        for (int i = 0; i < numOfPlayers; i = i + 1) {
            ScoreLabels[i].setText(PlayerNames[i]+": "+ (int)Scores[i]);
            ScoreLabels[i].setForeground(Colors[i]);
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
