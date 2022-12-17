import javax.swing.*;
import java.io.*;

public class ButtonController {
    public ButtonController() {
        MainView.saveButton.addActionListener(e -> saveGame());
        MainView.loadButton.addActionListener(e -> loadGame());
        MainView.newGameButton.addActionListener(e -> newGame());
        MainView.rankListButton.addActionListener(e -> RankList());
        MainView.exitButton.addActionListener(e -> exitGame());
    }

    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setSelectedFile(new File("SaveGame.save"));
        fileChooser.setDialogTitle("Save Game");
        fileChooser.setApproveButtonText("Save");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.CANCEL_OPTION) {
            Game.view.requestFocus();
            return;
        }

        String filePath = fileChooser.getSelectedFile().getPath();
        File saveFile = new File(filePath);

        if (!saveFile.exists())
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(saveFile));
            int[] saveList = new int[17];
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    saveList[i * 4 + j] = Game.grid[i][j].getGridValue();
            saveList[16] = MainView.score;
            for (int i = 0; i < 17; i++)
                bufferedWriter.write(saveList[i] + " ");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Game.view.requestFocus();
    }

    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setDialogTitle("Load Game");
        fileChooser.setApproveButtonText("Load");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.CANCEL_OPTION) {
            Game.view.requestFocus();
            return;
        }

        String filePath = fileChooser.getSelectedFile().getPath();
        File loadFile = new File(filePath);
        if (!loadFile.exists()) {
            JOptionPane.showMessageDialog(null, "File does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(loadFile));
            String content = bufferedReader.readLine();
            bufferedReader.close();
            String[] contentArray = content.split(" ");
            int[] loadList = new int[17];
            for (int i = 0; i < 17; i++)
                loadList[i] = Integer.parseInt(contentArray[i]);
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    Game.grid[i][j].setGridValue(loadList[i * 4 + j]);
            MainView.score = loadList[16];

            Game.view.requestFocus();
            Game.view.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Game.view.requestFocus();
    }

    public static void newGame() {
        Game.updateBestScoreFile();
        Game.init();
    }

    private void RankList() {
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage("Rank List:\n" + Game.bestScoreList[4] + "\n" + Game.bestScoreList[3] + "\n"
                + Game.bestScoreList[2] + "\n" + Game.bestScoreList[1] + "\n" + Game.bestScoreList[0]);
        JDialog dialog = optionPane.createDialog("RankList");
        dialog.setVisible(true);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());

        Game.view.requestFocus();
    }

    public static void exitGame() {
        Game.updateBestScoreFile();
        System.exit(0);
    }
}
