package Menu;

import game.snake.Board;
import game.snake.SnakeReplay;
import neuralnetwork.Trainer;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.ArrayList;

public class Menu extends JFrame {

    private static int bestSnake = 0;
    private static double highestHighscore = 0.0;
    private static JLabel highScoreLabel = new JLabel();
    private static JTextArea scoreBoardTextArea = new JTextArea();
    private JPanel menuPanel = new JPanel();
    private static boolean training = false;

    public static ArrayList<SnakeReplay> replays = new ArrayList();
    public static boolean isPlayer = false;

    private Trainer trainer;

        public Menu() {
            initUI();
        }

    public void initUI() {
            //buttons
            JButton playGame = new JButton("Play Snake");
            JButton AIPlay = new JButton("Train Snake");
            JButton rePlay = new JButton("Watch Replay");
            playGame.setBackground(Color.GREEN);
            AIPlay.setBackground(Color.GREEN);
            playGame.setForeground(Color.BLACK);
            AIPlay.setForeground(Color.BLACK);
            rePlay.setBackground(Color.GREEN);
            rePlay.setForeground(Color.BLACK);
            rePlay.setPreferredSize(new Dimension(90,20));
            rePlay.setFont(new Font("Helvetica", Font.BOLD, 8));

            //score board
            highScoreLabel.setText("Highest highscore: generation "+bestSnake+" with a score of "+highestHighscore);
            highScoreLabel.setForeground(Color.GRAY);
            highScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
            highScoreLabel.setVerticalAlignment(SwingConstants.CENTER);
            highScoreLabel.setPreferredSize(new Dimension(365,20));

            JScrollPane scroll = new JScrollPane(scoreBoardTextArea);
            scroll.setPreferredSize(new Dimension(480,180));
            scroll.getVerticalScrollBar().setBackground(Color.black);
            scroll.getHorizontalScrollBar().setBackground(Color.black);
            scoreBoardTextArea.setBackground(Color.black);
            scoreBoardTextArea.setForeground(Color.gray);
            scoreBoardTextArea.setEditable(false);
            scoreBoardTextArea.setLineWrap(true);
            scoreBoardTextArea.setWrapStyleWord(true);
            DefaultCaret caret = (DefaultCaret)scoreBoardTextArea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            //menu panel
            menuPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            menuPanel.setBackground(Color.black);
            menuPanel.setPreferredSize(new Dimension(500, 340));
            menuPanel.setFocusable(true);
            menuPanel.add(playGame);
            menuPanel.add(AIPlay);

            menuPanel.add(highScoreLabel);
            menuPanel.add(rePlay);
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
                        rePlay.setVisible(false);
                        scroll.setVisible(false);
                        AIPlay.setVisible(false);
                        playGame.setVisible(false);
                        isPlayer = true;
                        startSnake();
                    });
            AIPlay.addActionListener(
                    e -> {
                            if(!training) {
                                isPlayer = false;
                                trainSnake();
                            }
                    });
            rePlay.addActionListener(
                    e -> {
                        if(!training) {
                            if(replays.size() == 0){scoreBoardTextArea.append("No replays saved.\n");}
                            else{
                                System.out.println(replays.get(bestSnake-1).moves.size());
                                //0 = left, 1 = right, 2 = up,3 = down
                                int move;
                                for(int i = 0; i<replays.get(bestSnake-1).moves.size(); i++) {
                                    move = replays.get(bestSnake - 1).moves.get(i);
                                    switch (move){
                                        case 0://left
                                            System.out.println("Snake went left");
                                            break;
                                        case 1://right
                                            System.out.println("Snake went right");
                                            break;
                                        case 2://up
                                            System.out.println("Snake went up");
                                            break;
                                        case 3://down
                                            System.out.println("Snake went down");
                                            break;
                                    }

                                }
                            }
                        }
                        else{scoreBoardTextArea.append("Snakes are currently training.\n");}
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

        private void trainSnake() {
            try {
                    training = true;
                    trainer = new Trainer();
                    trainer.startNeuralNetwork();
            }
            catch (Exception ex){System.out.println(ex);}
        }

        public static void appendScore(int generation, double highScore){

                if (highScore > highestHighscore) {
                   highestHighscore = highScore;
                    bestSnake = generation;
                }
                highScoreLabel.setText("Highest highscore: generation " + bestSnake + " with a score of " + highestHighscore + ".");
                scoreBoardTextArea.append("Generation " + generation + " has finished. High score: " + highScore + ".\n");
        }

        public static void setTrainingDone(){
            training = false;
            scoreBoardTextArea.append("Snakes are done training, you can now see a replay of the best snake.\n");
        }

        public static void appendPlayerScore(int totalScore) {
            highScoreLabel.setText("Score: "+totalScore);
        }


    public static void main(String[] args) {
            EventQueue.invokeLater(() -> {
                JFrame frame = new Menu();
            frame.setVisible(true);
         });

    }

}