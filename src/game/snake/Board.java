package game.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DELAY = 140;

    private final int ALL_DOTS = 900;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 29;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    private SnakeSimulator snakeSimulator = new SnakeSimulator(30, 30);

    public Board() {
        //initBoard();
    }

    public void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setBorder(BorderFactory.createLineBorder(Color.WHITE));

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();
    }

    private void initGame() {
        snakeSimulator.initGame();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (snakeSimulator.getIngame()) {
            g.drawImage(apple, snakeSimulator.apple_x * DOT_SIZE, snakeSimulator.apple_y * DOT_SIZE, this);

            for (int z = 0; z < snakeSimulator.dots; z++) {
                if (z == 0) {
                    g.drawImage(head, snakeSimulator.getX(z) * DOT_SIZE, snakeSimulator.getY(z) * DOT_SIZE, this);
                } else {
                    g.drawImage(ball, snakeSimulator.getX(z) * DOT_SIZE, snakeSimulator.getY(z) * DOT_SIZE, this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        System.out.println(snakeSimulator.newScore.elapsedGameTime());
        System.out.println(snakeSimulator.newScore.totalScore());

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.GREEN);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snakeSimulator.step();
        if (!snakeSimulator.getIngame()) {
            timer.stop();
        }
        repaint();
    }


    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_LEFT) {
                    snakeSimulator.left();
                }

                if (key == KeyEvent.VK_RIGHT) {
                    snakeSimulator.right();
                }

                if (key == KeyEvent.VK_UP) {
                    snakeSimulator.up();
                }

                if (key == KeyEvent.VK_DOWN) {
                    snakeSimulator.down();
                }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println("called 3");
        }
    }
}