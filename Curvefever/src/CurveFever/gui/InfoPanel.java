package CurveFever.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static java.util.Arrays.sort;
import static java.util.Collections.reverseOrder;


//GamePanelen belül lesz!
public class InfoPanel extends JPanel {
    private double[] Points;
    private String[] PlayerNames;
    private int roundNum;
    private int currentRound;
    private Color[] Colors;
    private JLabel[] ScoreLabels;
    private JLabel roundLabel;
    private static final Color BACKGROUND = new Color(128, 164, 252);
    private int numOfPlayers;
    public InfoPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width,height));
    }
    public InfoPanel(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
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
        }
    }
    public double[] getPoints() {return Points;}

    public void setPoints(double[] points) {Points = points;}

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
        PlayerNames = playerNames;
    }

    public void setColors(Color[] colors) {
        Colors = colors;
    }

    public void drawScore(Graphics g) {//not used, instead draw2
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Lato", Font.BOLD, 20));
        g2d.drawString("Scores:", 10,  20);
        g2d.setFont(new Font("Lato", Font.PLAIN, 15));
        int y = 20;
        for (int i = 0; i < PlayerNames.length; i = i + 1) {
            g2d.drawString(PlayerNames[i] + ": " + Points, 10, y += 15);
        }
    }
    public void drawScore2() {
        //TODO esetleg sorrendbe irni ki a scoreokat
        roundLabel.setText("Round "+currentRound+"/"+roundNum);
        for (int i = 0; i < numOfPlayers; i = i + 1) {
            ScoreLabels[i].setText(PlayerNames[i]+": "+Points[i]);
            ScoreLabels[i].setForeground(Colors[i]);
        }
    }
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver
        // because Component implements the ImageObserver interface, and JPanel
        // extends from Component. So "this" Board instance, as a Component, can
        // react to imageUpdate() events triggered by g.drawImage()

        // draw our graphics.
        setBackground(BACKGROUND);
        //drawScore(g); //commented out to test drawscore 2
        drawScore2();
        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }
}
