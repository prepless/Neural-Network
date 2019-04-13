package neuralnetwork;

import game.snake.Board;
import game.snake.SnakeSimulator;

import java.util.ArrayList;
import java.util.List;



public class Main {

    public static void main(String[] args) throws Exception {
        int numberOfGenerations = 1000;
        int generationSize = 500;
        int[] networkSize = new int[]{900 ,50 ,50 ,4 };
        Main main = new Main();
        main.createGenerations(numberOfGenerations,generationSize,networkSize);
    }

    public void createGenerations(int numberOfgenerations, int generationSize, int[] size) throws Exception {
        double highestHighscore = 0.0;
        int highestHighscoreGeneration = 0;

        List<GameResult> resultList = executeNetworks(createFirstGeneration(generationSize, size));
        for (int i = 0; i < numberOfgenerations; i++){
            GameResult winner = determineWinner(resultList);
            double highScore =winner.calculateScore();
            if(highScore>highestHighscore){
                highestHighscore=highScore;
                highestHighscoreGeneration = i;
            }
            System.out.println("Generation "+(i+1)+" has finished. High score: " + highScore + ".      Highest highscore: (gen): "+(highestHighscoreGeneration+1)+" (score): " + highestHighscore);
            List<Network> networkList= createNextGeneration(winner.getNetwork(), generationSize);
            resultList = executeNetworks(networkList);
        }
        System.out.println("Finished");
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

            while(snake.getIngame()){
                double[] output = fillInput(snake,network);
                selectDirection(snake, output);
                snake.step();
            }
            gameResults.add(new GameResult(snake,network));
        }
        return gameResults;
    }

    public void selectDirection(SnakeSimulator snake, double[]output){
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
                snake.left();
                break;
            case 1:
                snake.right();
                break;
            case 2:
                snake.up();
                break;
            default:
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
