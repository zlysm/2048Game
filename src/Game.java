import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Game implements KeyListener {
    public static SingleGrid[][] grid = new SingleGrid[4][4];
    public static MainView view;
    private static File bestScoreFile;
    public static int[] bestScoreList;
    public static boolean gameOver = false;

    public Game() {
        view = new MainView();
        view.addKeyListener(this);
        new ButtonController();
        init();
    }

    public static void init() {
        //init basic grid
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                grid[i][j] = new SingleGrid();
                grid[i][j].setLoc(55 + 100 * j, 210 + 100 * i);
            }
        addRandomGrid();
        addRandomGrid();

        //init best score
        bestScoreFile = new File("./Rank.rank");
        if (!bestScoreFile.exists())
            try {
                bestScoreFile.createNewFile();

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(bestScoreFile));
                bufferedWriter.write("0 0 0 0 0");
                bufferedWriter.close();

                bestScoreList = new int[5];
                for (int i = 0; i < 5; i++)
                    bestScoreList[i] = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }

        bestScoreList = new int[5];
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(bestScoreFile));
            String content = bufferedReader.readLine();
            bufferedReader.close();
            String[] contentArray = content.split(" ");
            for (int i = 0; i < 5; i++)
                bestScoreList[i] = Integer.parseInt(contentArray[i]);
            Arrays.sort(bestScoreList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //init score
        gameOver = false;
        MainView.bestScore = bestScoreList[4];
        MainView.score = 0;

        view.repaint();
        view.requestFocus();
    }

    private static void addRandomGrid() {
        if (gameOver)
            return;
        int cnt = 0;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (grid[i][j].getGridValue() == 0)
                    cnt++;
        if (cnt == 0)
            return;
        int x = (int) (Math.random() * 4);
        int y = (int) (Math.random() * 4);
        int value = (int) (Math.random() * 2 + 1) * 2;
        if (grid[x][y].getGridValue() == 0)
            grid[x][y].setGridValue(value);
        else
            addRandomGrid();
    }

    private boolean gameFail() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)  //check 0 grid
                if (grid[i][j].getGridValue() == 0)
                    return false;

        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)  //check same grid
                if (grid[i][j].getGridValue() == grid[i + 1][j].getGridValue() ||
                        grid[i][j].getGridValue() == grid[i][j + 1].getGridValue())
                    return false;

        for (int i = 0; i < 3; ++i)  //check last row
            if (grid[i][3].getGridValue() == grid[i + 1][3].getGridValue())
                return false;

        for (int j = 0; j < 3; ++j)  //check last column
            if (grid[3][j].getGridValue() == grid[3][j + 1].getGridValue())
                return false;

        return true;
    }

    private boolean gameWin() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (grid[i][j].getGridValue() == 2048)
                    return true;
        return false;
    }

    private boolean gameJudge() {
        if (gameWin() || gameFail()) {
            gameOver = true;

            String[] options = {"Try Again", "Exit"};
            int userChoice = JOptionPane.showOptionDialog(null,
                    gameWin() ? "You Win!" : "You Lose!",
                    gameWin() ? "Congratulations!" : "Game Over!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
            if (userChoice == JOptionPane.YES_OPTION)
                ButtonController.newGame();
            else
                ButtonController.exitGame();
            return true;
        }
        return false;
    }

    public static void updateBestScoreFile() {
        if (MainView.score > bestScoreList[0]) {
            bestScoreList[0] = MainView.score;
            Arrays.sort(bestScoreList);
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(bestScoreFile));
                bufferedWriter.write(bestScoreList[0] + " " + bestScoreList[1] + " " + bestScoreList[2] + " "
                        + bestScoreList[3] + " " + bestScoreList[4]);
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveLeft() {
        for (int i = 0; i < 4; ++i)
            for (int j = 1; j < 4; ++j) {
                if (grid[i][j].getGridValue() == 0)
                    continue;
                int k = j;
                while (k > 0 && grid[i][k - 1].getGridValue() == 0) {
                    grid[i][k - 1].setGridValue(grid[i][k].getGridValue());
                    grid[i][k].setGridValue(0);
                    k--;
                }
                if (k > 0 && grid[i][k - 1].getGridValue() == grid[i][k].getGridValue()) {
                    MainView.score += grid[i][k].getGridValue();
                    MainView.bestScore = Math.max(MainView.bestScore, MainView.score);
                    grid[i][k - 1].setGridValue(grid[i][k - 1].getGridValue() * 2);
                    grid[i][k].setGridValue(0);
                }
            }

        if (gameJudge()) {
            addRandomGrid();
            addRandomGrid();
        } else
            addRandomGrid();
        view.repaint();
    }

    private void moveRight() {
        for (int i = 0; i < 4; ++i)
            for (int j = 2; j >= 0; --j) {
                if (grid[i][j].getGridValue() == 0)
                    continue;
                int k = j;
                while (k < 3 && grid[i][k + 1].getGridValue() == 0) {
                    grid[i][k + 1].setGridValue(grid[i][k].getGridValue());
                    grid[i][k].setGridValue(0);
                    k++;
                }
                if (k < 3 && grid[i][k + 1].getGridValue() == grid[i][k].getGridValue()) {
                    MainView.score += grid[i][k].getGridValue();
                    MainView.bestScore = Math.max(MainView.bestScore, MainView.score);
                    grid[i][k + 1].setGridValue(grid[i][k + 1].getGridValue() * 2);
                    grid[i][k].setGridValue(0);
                }
            }

        if (gameJudge()) {
            addRandomGrid();
            addRandomGrid();
        } else
            addRandomGrid();
        view.repaint();
    }

    private void moveUp() {
        for (int j = 0; j < 4; ++j)
            for (int i = 1; i < 4; ++i) {
                if (grid[i][j].getGridValue() == 0)
                    continue;
                int k = i;
                while (k > 0 && grid[k - 1][j].getGridValue() == 0) {
                    grid[k - 1][j].setGridValue(grid[k][j].getGridValue());
                    grid[k][j].setGridValue(0);
                    k--;
                }
                if (k > 0 && grid[k - 1][j].getGridValue() == grid[k][j].getGridValue()) {
                    MainView.score += grid[k][j].getGridValue();
                    MainView.bestScore = Math.max(MainView.bestScore, MainView.score);
                    grid[k - 1][j].setGridValue(grid[k - 1][j].getGridValue() * 2);
                    grid[k][j].setGridValue(0);
                }
            }

        if (gameJudge()) {
            addRandomGrid();
            addRandomGrid();
        } else
            addRandomGrid();
        view.repaint();
    }

    private void moveDown() {
        for (int j = 0; j < 4; ++j)
            for (int i = 2; i >= 0; --i) {
                if (grid[i][j].getGridValue() == 0)
                    continue;
                int k = i;
                while (k < 3 && grid[k + 1][j].getGridValue() == 0) {
                    grid[k + 1][j].setGridValue(grid[k][j].getGridValue());
                    grid[k][j].setGridValue(0);
                    k++;
                }
                if (k < 3 && grid[k + 1][j].getGridValue() == grid[k][j].getGridValue()) {
                    MainView.score += grid[k][j].getGridValue();
                    MainView.bestScore = Math.max(MainView.bestScore, MainView.score);
                    grid[k + 1][j].setGridValue(grid[k + 1][j].getGridValue() * 2);
                    grid[k][j].setGridValue(0);
                }
            }

        if (gameJudge()) {
            addRandomGrid();
            addRandomGrid();
        } else
            addRandomGrid();
        view.repaint();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A, KeyEvent.VK_H -> moveLeft();
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D, KeyEvent.VK_L -> moveRight();
            case KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_K -> moveUp();
            case KeyEvent.VK_DOWN, KeyEvent.VK_S, KeyEvent.VK_J -> moveDown();
            case KeyEvent.VK_ESCAPE -> {
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to exit the game?",
                        "Exit", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                    ButtonController.exitGame();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}