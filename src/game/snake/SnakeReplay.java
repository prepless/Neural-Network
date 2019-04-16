package game.snake;
import java.util.ArrayList;

public class SnakeReplay {
    public ArrayList<Integer>moves = new ArrayList<>();
    private ArrayList<Integer[]>appleLocations = new ArrayList<>();

    public void addMoves(int move){
        //0 = left, 1 = right, 2 = up,3 = down
        moves.add(move);
    }

    public void addApples(int appleX, int appleY){
        Integer[] appleLocation = new Integer[2];
        appleLocation[0] = appleX;
        appleLocation[1]= appleY;
        appleLocations.add(appleLocation);
    }

    public void startReplay(){
        //Board board = new Board();
        //board.isPlayer = false;
    }

    public void checkNextMove(){

    }


}
