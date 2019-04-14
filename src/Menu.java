import game.snake.Board;
import neuralnetwork.Trainer;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    public int highestHighscoreGeneration = 0;
    private double highestHighscore = 0.0;
    private JLabel highScoreLabel = new JLabel();
    private JTextArea scoreBoard = new JTextArea();

        public Menu() {
            initUI();
        }

        private void initUI() {
            //buttons
            JButton playGame = new JButton("Play Snake");
            JButton AIPlay = new JButton("Train Snake");
            playGame.setBackground(Color.GREEN);
            AIPlay.setBackground(Color.GREEN);
            playGame.setForeground(Color.BLACK);
            AIPlay.setForeground(Color.BLACK);

            //score board
            highScoreLabel.setText("Highest highscore: generation "+highestHighscoreGeneration+" with a score of "+highestHighscore+".");
            highScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
            highScoreLabel.setVerticalAlignment(SwingConstants.CENTER);
            highScoreLabel.setPreferredSize(new Dimension(480,20));

            JScrollPane scroll = new JScrollPane(scoreBoard);
            scroll.setPreferredSize(new Dimension(480,180));
            scroll.getVerticalScrollBar().setBackground(Color.black);
            scroll.getHorizontalScrollBar().setBackground(Color.black);
            scoreBoard.setBackground(Color.black);
            scoreBoard.setForeground(Color.gray);

            //menu panel
            JPanel menuPanel = new JPanel();
            menuPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            menuPanel.setBackground(Color.black);
            menuPanel.setPreferredSize(new Dimension(500, 300));
            menuPanel.setFocusable(true);
            menuPanel.add(playGame);
            menuPanel.add(AIPlay);

            menuPanel.add(highScoreLabel);
            menuPanel.add(scroll);

            //frame
            add(menuPanel);
            setResizable(false);
            pack();
            setTitle("Snake Neural Network");
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //action listeners
            //////snake werkt wel zonder onderstaande ActionListner... maar niet met... :S
            playGame.addActionListener(
                    e -> {
                        menuPanel.setVisible(false);
                        startSnake();
                    });
            AIPlay.addActionListener(
                    e -> {
                        menuPanel.setVisible(false);
                        try {
                            trainSnake();
                        }
                        catch (Exception ex){System.out.println(ex);}
                    });
        }

        private void startSnake(){
            Board board = new Board();
            add(board);
            board.initBoard();
            pack();
            setTitle("Snake");
            setLocationRelativeTo(null);
        }

        private void trainSnake() throws Exception{
            Trainer trainer = new Trainer();
            trainer.startNeuralNetwork();
        }

        public void appendScore(int generation, double highScore){
            if(highScore>highestHighscore){
                highestHighscore=highScore;
                highestHighscoreGeneration = generation;
            }
            highScoreLabel.setText("Highest highscore: generation "+highestHighscoreGeneration+" with a score of "+highestHighscore+".");
            scoreBoard.append("Generation "+generation+" has finished. High score: " + highScore +".");
        }

        public static void main(String[] args) {
            EventQueue.invokeLater(() -> {
                JFrame frame = new Menu();
            frame.setVisible(true);
         });

    }

}