package Menu;

import game.snake.Board;
import neuralnetwork.Trainer;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame implements Runnable{

    private static Menu menu = new Menu();

    public int bestSnake = 0;
    private double highestHighscore = 0.0;
    private JLabel highScoreLabel = new JLabel();
    private JTextArea scoreBoardTextArea = new JTextArea();
    private JPanel menuPanel = new JPanel();

    private Trainer trainer;

        public Menu() {
            initUI();
        }

        public void initUI() {
            //buttons
            JButton playGame = new JButton("Play Snake");
            JButton AIPlay = new JButton("Train Snake");
            playGame.setBackground(Color.GREEN);
            AIPlay.setBackground(Color.GREEN);
            playGame.setForeground(Color.BLACK);
            AIPlay.setForeground(Color.BLACK);

            //score board
            highScoreLabel.setText("Highest highscore: generation "+bestSnake+" with a score of "+highestHighscore+".");
            highScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
            highScoreLabel.setVerticalAlignment(SwingConstants.CENTER);
            highScoreLabel.setPreferredSize(new Dimension(480,20));

            JScrollPane scroll = new JScrollPane(scoreBoardTextArea);
            scroll.setPreferredSize(new Dimension(480,180));
            scroll.getVerticalScrollBar().setBackground(Color.black);
            scroll.getHorizontalScrollBar().setBackground(Color.black);
            scoreBoardTextArea.setBackground(Color.black);
            scoreBoardTextArea.setForeground(Color.gray);
            scoreBoardTextArea.setEditable(false);
            scoreBoardTextArea.setLineWrap(true);
            scoreBoardTextArea.setWrapStyleWord(true);

            //menu panel
            menuPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            menuPanel.setBackground(Color.black);
            menuPanel.setPreferredSize(new Dimension(500, 340));
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
            playGame.addActionListener(
                    e -> {
                        highScoreLabel.setText("Score:");
                        scroll.setVisible(false);
                        AIPlay.setVisible(false);
                        playGame.setVisible(false);
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
            menuPanel.add(board);
            board.initBoard();
            board.requestFocus();
            pack();
            setTitle("Snake");
            setLocationRelativeTo(null);
        }

        private void trainSnake() throws Exception{
            trainer = new Trainer();
            //new Thread(new CheckScore(menu, trainer)).start();
            new Thread(this::run).start();
            trainer.startNeuralNetwork();

        }

        public void appendScore(int generation, double highScore){
            if(highScore>this.highestHighscore){
                this.highestHighscore=highScore;
                bestSnake = generation;
            }


            System.out.println("Generation "+generation+" has finished. High score: " + highScore +".");

            highScoreLabel.setText("Highest highscore: generation "+bestSnake+" with a score of "+highestHighscore+".");
            scoreBoardTextArea.append("Generation "+generation+" has finished. High score: " + highScore +".");
        }

        public static void main(String[] args) {
            EventQueue.invokeLater(() -> {
                JFrame frame = new Menu();
            frame.setVisible(true);
         });

    }

    public void run() {
        while(true){
            try {
                    appendScore(this.trainer.generation, this.trainer.highScore);
                //this.currentGen = getGeneration();
            }
            catch (Exception e){System.out.println("no score yet");}
        }
    }

}