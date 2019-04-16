package game.snake;

public class SnakeSimulator {


    public int apple_x;
    public int apple_y;

    public Score newScore = new Score();

    private boolean inGame = true;



    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private int[] x;
    private int[] y;

    public int dots;
    private int width;
    private int height;

    SnakeReplay replay = new SnakeReplay();

    public SnakeSimulator(int width, int height){

        this.width = width;
        this.height = height;

        y = new int[width*height];
        x = new int[width*height];
    }

    public void initGame() {
        dots = 3;
        for (int z = 0; z < dots; z++) {
            x[z] = 5 - z;
            y[z] = 5;
        }
        locateApple();
        //newScore.setGameStartTime();
    }

    public void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= 1;
        }

        if (rightDirection) {
            x[0] += 1;
        }

        if (upDirection) {
            y[0] -= 1;
        }

        if (downDirection) {
            y[0] += 1;
        }
    }

    public void checkCollision() {

        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= height) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= width) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
    }

    public boolean getIngame(){
        return inGame;
    }

    public void locateApple() {
        apple_x = (int) (Math.random() * width);
        apple_y = (int) (Math.random() * height);
        replay.addApples(apple_x,apple_y);
    }

    public void left(){
        if(!rightDirection){
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }
    }

    public void right() {
        if(!leftDirection){
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }
    }

    public void up(){
        if(!downDirection) {
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }

    public void down() {
        if(!upDirection) {
            downDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }


    public void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();

            newScore.setPoints();
        }
    }


    public void step() {
        if (this.getIngame()) {

            this.checkApple();
            this.move();
            this.checkCollision();
        }
    }

    public int getX(int index) {

        return x[index];
    }

    public int getY(int index) {
        return y[index];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}


