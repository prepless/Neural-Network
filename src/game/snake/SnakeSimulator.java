package game.snake;

import Menu.Menu;

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
        dots = 35;
        for (int z = 0; z < dots; z++) {
            x[z] = 5 - z;
            y[z] = 5;
        }
        locateApple();
        //newScore.setGameStartTime();
    }

    public void move() {

      //  double up = getUpDistanceToSnake();
      //  double down = getDownDistanceToSnake();
      //  double right = getRightDistanceToSnake();
       // double left = getLeftDistanceToSnake();
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= 1;
            replay.addMoves(Direction.Left);
        }

        if (rightDirection) {
            x[0] += 1;
            replay.addMoves(Direction.Right);
        }

        if (upDirection) {
            y[0] -= 1;
            replay.addMoves(Direction.Up);
        }

        if (downDirection) {
            y[0] += 1;
            replay.addMoves(Direction.Down);
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
            replay.moves.add(Direction.Left);
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }
    }

    public void right() {
        if(!leftDirection){
            replay.moves.add(Direction.Right);
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }
    }

    public void up(){
        if(!downDirection) {
            replay.moves.add(Direction.Up);
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }

    public void down() {
        if(!upDirection) {
            replay.moves.add(Direction.Down);
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
            if(Menu.isPlayer == true) {
                Menu.appendPlayerScore(newScore.totalScore());
            }
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

    public SnakeReplay getReplay() {
        return replay;
    }

    public double getyDistanceToApple() {
        return Math.abs(((double)y[0]-apple_y)/height);
    }

    public double getxDistanceToApple() {
        return Math.abs(((double)x[0]-apple_x)/width);
    }

    public double getDistanceToTopWall() {
        return ((double)y[0]/height);
    }

    public double getDistanceToBottomWall() {
        return ((double)height - y[0])/height;
    }

    public double getDistanceToLeftWall() {
        return ((double)x[0])/width;
    }

    public double getDistanceToRightWall() {
        return ((double)width - x[0])/width;
    }

    public double getLeftDistanceToSnake(){
        int xPos = this.x[0];
        for(int i =xPos-1; i>=0;i--){
            if(snakeExistsOnPos(i,this.y[0])){
               // System.out.println("LeftDistance:"+(xPos-i)/(double)width);
                return (xPos-i)/(double)width;
            }
        }
        return 1;
    }

    public double getRightDistanceToSnake(){
        int xPos = this.x[0];
        for(int i =xPos+1; i<=width;i++){
            if(snakeExistsOnPos(i,this.y[0])){
              //  System.out.println("RightDistance:"+(i-xPos)/(double)width);
                return (i-xPos)/(double)width;
            }
        }
        return 1;
    }

    public double getUpDistanceToSnake(){
        int yPos = this.y[0];
        for(int i =yPos-1; i>=0;i--){
            if(snakeExistsOnPos(this.x[0],i)){
              //  System.out.println("UpDistance:"+(yPos-i)/(double)width);
                return (yPos-i)/(double)width;
            }
        }
        return 1;
    }

    public double getDownDistanceToSnake(){
        int yPos = this.y[0];
        for(int i =yPos+1; i<=width;i++){
            if(snakeExistsOnPos(this.x[0],i)){
              //  System.out.println("DownDistance:"+(i-yPos)/(double)width);
                return (i-yPos)/(double)width;
            }
        }
        return 1;
    }

    public boolean snakeExistsOnPos(int x, int y){
        for (int z = dots; z > 0; z--) {
            if (this.x[z]==x && this.y[z]==y) {
            return true;
            }
        }
        return false;
    }

    /*public double getYDistanceToSnake(){
        int distance=4;
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (y[0] == y[z]) && (rightDirection || leftDirection)) {
                if(Math.abs(x[0] - x[z])<3) {
                    distance=Math.abs(x[0] - x[z]);
                }
            }
        }
        return (double)distance/10;
    }*/
}


