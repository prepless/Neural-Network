package neuralnetwork;
import Menu.Menu;
import game.snake.SnakeReplay;
import game.snake.SnakeSimulator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Trainer extends SwingWorker<Object, Object> {

    public void startNeuralNetwork() {
        execute(); // start to do the "doInBackground stuff"
    }

    //do In Background so that JFrame does not freeze
    @Override
    protected Object doInBackground() throws Exception {
        int numberOfGenerations = 20;
        int generationSize = 2000;
        int[] networkSize = new int[]{900 ,18 ,18 ,4 };
        Trainer trainer = new Trainer();
        trainer.createGenerations(numberOfGenerations,generationSize,networkSize);
        return null;
    }

    @Override
    protected void done() {
        Menu.setTrainingDone();
    }

    public void createGenerations(int numberOfgenerations, int generationSize, int[] size) throws Exception {
        List<GameResult> resultList = executeNetworks(createFirstGeneration(generationSize, size));
        for (int i = 0; i < numberOfgenerations; i++){
            //determine winner and update scoreboard
            GameResult winner = determineWinner(resultList);
            double highScore =winner.calculateScore();
            int generation = i+1;
            Menu.appendScore(generation,highScore);

            //create next generation as a copy of the winner
            List<Network> networkList= createNextGeneration(winner.getNetwork(), generationSize);
            resultList = executeNetworks(networkList);

        }
    }

    public List<Network> createFirstGeneration(int generationSize, int[] size){
        List<Network> networkList = new ArrayList<>();
        for(int i=0; i< generationSize; i++) {
            Network net = new Network(size);
            net.initializeRandom();
            networkList.add(net);
        }
        return networkList;
    }

    public List<Network> createNextGeneration(Network winner, int generationSize){
        List<Network> networkList = new ArrayList<>();
        for(int i=0; i< generationSize; i++) {
            Network newNetwork = new Network(winner);
            newNetwork.randomize();
            networkList.add(newNetwork);
        }
        return networkList;
    }

    public List<GameResult> executeNetworks(List<Network> networkList) throws Exception{

        ArrayList<GameResult> gameResults = new ArrayList<>();
        for (Network network: networkList) {
            SnakeSimulator snake = new SnakeSimulator(30, 30);
            SnakeReplay replay = new SnakeReplay();
            while(snake.getIngame()){
                double[] output = fillInput(snake,network);
                selectDirection(snake, output,replay);
                snake.step();
            }
            gameResults.add(new GameResult(snake,network));
            Menu.replays.add(replay);
        }
        return gameResults;
    }

    public void selectDirection(SnakeSimulator snake, double[]output,SnakeReplay replay){
        double lastVal = 0;
        int outputNumber = 0;
        for(int i = 0; i < output.length; i++){
            if(output[i]>lastVal){
                lastVal=output[i];
                outputNumber=i;
            }
        }
        //[0]left [1]right [2]up [3]down
        switch (outputNumber){
            case 0:
                replay.addMoves(0);
                snake.left();
                break;
            case 1:
                replay.addMoves(1);
                snake.right();
                break;
            case 2:
                replay.addMoves(2);
                snake.up();
                break;
            default:
                replay.addMoves(3);
                snake.down();
                break;
        }
    }

    public GameResult determineWinner(List<GameResult> resultList){
        return resultList.stream().max(GameResult::compareTo).get();
    }

    public double[] fillInput(SnakeSimulator snake, Network network) throws Exception {
        int inputLength = snake.getWidth() * snake.getHeight();
        double[] inputs = new double[inputLength];

        for(int i = 0; i < snake.dots; i++){
            int dotX = snake.getX(i);
            int dotY = snake.getY(i);

            int position = dotY * snake.getWidth() + dotX;
            if(position < 0){
                System.out.println("Error.");
            }

            if( i == 0){
                inputs[position] = 1.0;
            }else{
                inputs[position] = 0.666;
            }

            int applePosition = snake.apple_y * snake.getWidth() + snake.apple_x;

            inputs[applePosition] = 0.333;
        }

        return network.calculate(inputs);

    }
}
