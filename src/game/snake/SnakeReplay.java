package game.snake;
import game.snake.SnakeSimulator;
import java.util.ArrayList;

public class SnakeReplay {
    public ArrayList<Direction>moves = new ArrayList<>();
    private ArrayList<Coordinates>appleLocations = new ArrayList<>();
    private int curMove = 0;
    private SnakeSimulator snakeSimulator;

    public void addMoves(Direction move){
        //0 = left, 1 = right, 2 = up,3 = down
        moves.add(move);
    }

    public void addApples(int appleX, int appleY){
        appleLocations.add(new Coordinates(appleX,appleY));
    }

    public void startReplay(){
        snakeSimulator = new SnakeSimulator(30,30);
        //1snake does a move
        //2do a step
    }


    public void doNextMove(){

        //0 = left, 1 = right, 2 = up,3 = down
        switch (moves.get(curMove)){
            case Left:
                snakeSimulator.left();
                break;
            case  Right:
                snakeSimulator.right();
                break;
            case Up:
                snakeSimulator.up();
                break;
            case Down :
                snakeSimulator.down();
                break;
                default:
        }
        curMove++;
    }


}
