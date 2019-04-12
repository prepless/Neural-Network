package neuralnetwork;

import com.zedcode.SnakeSimulator;

import java.util.Comparator;

public class GameResult implements Comparable<GameResult> {


    private SnakeSimulator game;
    private Network network;

    public GameResult(SnakeSimulator game, Network network){
        this.game = game;
        this.network = network;
    }

    public SnakeSimulator getGame() {
        return game;
    }

    public Network getNetwork() {
        return network;
    }

    public double calculateScore(){
        double totalScore = game.newScore.totalScore();
       // double timePlayed = (game.newScore.elapsedGameTimeMin()/60) + game.newScore.elapsedGameTimeSec();

        return totalScore;//-(timePlayed/100);
    }

    @Override
    public int compareTo(GameResult o) {
        double ownScore = calculateScore();
        double otherScore = o.calculateScore();
        if(ownScore < otherScore){
            return -1;
        }
        if(ownScore > otherScore){
            return 1;
        }

        return 0;
    }
}
