import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public static JButton newGameButton;
    public static JButton saveButton;
    public static JButton loadButton;
    public static JButton exitButton;
    public static JButton rankListButton;
    public static JLabel scoreValueLabel;
    public static JLabel bestScoreValueLabel;
    public static int score;
    public static int bestScore;

    public MainView() {
        super("2048 Game by Yiming Zhang");
        JPanel panel = (JPanel) getContentPane();
        setSize(500, 700);
        setResizable(false);

        panel.setLayout(null);
        panel.setBackground(new Color(0xF8F6ED));

        Font font = new Font("Arial", Font.BOLD, 70);
        Color backgroundColor = new Color(0x8E7966);

        JLabel title = new JLabel("2048", JLabel.CENTER);
        title.setFont(font);
        title.setForeground(new Color(0x766D65));

        //top panel
        saveButton = new JButton("Save");
        font = new Font("Arial", Font.BOLD, 20);
        saveButton.setFont(font);
        Color foregroundColor = new Color(0xF7F4F0);
        saveButton.setForeground(foregroundColor);
        saveButton.setBackground(backgroundColor);
        saveButton.setFocusPainted(false);

        loadButton = new JButton("Load");
        loadButton.setFont(font);
        loadButton.setForeground(foregroundColor);
        loadButton.setBackground(backgroundColor);
        loadButton.setFocusPainted(false);

        newGameButton = new JButton("New");
        newGameButton.setFont(font);
        newGameButton.setForeground(foregroundColor);
        newGameButton.setBackground(backgroundColor);
        newGameButton.setFocusPainted(false);

        rankListButton = new JButton("Rank");
        rankListButton.setFont(font);
        rankListButton.setForeground(foregroundColor);
        rankListButton.setBackground(backgroundColor);
        rankListButton.setFocusPainted(false);

        //score panel
        JLabel scoreLabel = new JLabel("Score", JLabel.CENTER);
        scoreLabel.setFont(font);
        scoreLabel.setOpaque(true);
        scoreLabel.setForeground(new Color(0xECE2D9));
        scoreLabel.setBackground(new Color(0xBAAC9F));

        JLabel bestScoreLabel = new JLabel("Best", JLabel.CENTER);
        bestScoreLabel.setFont(font);
        bestScoreLabel.setOpaque(true);
        bestScoreLabel.setForeground(new Color(0xECE2D9));
        bestScoreLabel.setBackground(new Color(0xBAAC9F));

        scoreValueLabel = new JLabel(String.valueOf(score), JLabel.CENTER);
        scoreValueLabel.setFont(font);
        scoreValueLabel.setOpaque(true);
        scoreValueLabel.setForeground(new Color(0xFDFDFD));
        scoreValueLabel.setBackground(new Color(0xBAAC9F));

        bestScoreValueLabel = new JLabel(String.valueOf(bestScore), JLabel.CENTER);
        bestScoreValueLabel.setFont(font);
        bestScoreValueLabel.setOpaque(true);
        bestScoreValueLabel.setForeground(new Color(0xFDFDFD));
        bestScoreValueLabel.setBackground(new Color(0xBAAC9F));

        //rank panel
        rankListButton = new JButton("Ranking List");
        rankListButton.setFont(font);
        rankListButton.setForeground(foregroundColor);
        rankListButton.setBackground(backgroundColor);
        rankListButton.setFocusPainted(false);

        //exit panel
        exitButton = new JButton("Exit");
        exitButton.setFont(font);
        exitButton.setForeground(foregroundColor);
        exitButton.setBackground(backgroundColor);
        exitButton.setFocusPainted(false);

        panel.add(title);
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(newGameButton);
        panel.add(scoreLabel);
        panel.add(bestScoreLabel);
        panel.add(scoreValueLabel);
        panel.add(bestScoreValueLabel);
        panel.add(rankListButton);
        panel.add(exitButton);

        title.setBounds(45, 0, 200, 90);
        scoreLabel.setBounds(295, 15, 70, 30);
        scoreValueLabel.setBounds(295, 45, 70, 30);
        bestScoreLabel.setBounds(375, 15, 70, 30);
        bestScoreValueLabel.setBounds(375, 45, 70, 30);
        saveButton.setBounds(50, 110, 90, 40);
        loadButton.setBounds(150, 110, 90, 40);
        exitButton.setBounds(350, 110, 90, 40);
        newGameButton.setBounds(250, 110, 90, 40);
        rankListButton.setBounds(50, 600, 390, 40);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void updateAllScore() {
        scoreValueLabel.setText(String.valueOf(score));
        bestScoreValueLabel.setText(String.valueOf(bestScore));
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(0xBAAC9F));
        g.fillRoundRect(45, 200, 410, 410, 15, 15);
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                SingleGrid grid = Game.grid[i][j];
                g.setColor(grid.getBackground());
                g.fillRoundRect(grid.xCoord, grid.yCoord, 90, 90, 5, 5);
                g.setColor(grid.getFontColor());
                g.setFont(grid.getFont());
                String value = String.valueOf(grid.getGridValue());
                //stay in middle
                int x = grid.xCoord + (90 - g.getFontMetrics().stringWidth(value)) / 2;
                int y = grid.yCoord + (90 - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                g.drawString(value, x, y);
            }
        requestFocus();
    }

    public void repaint() {
        updateAllScore();
        update(getGraphics());
    }
}