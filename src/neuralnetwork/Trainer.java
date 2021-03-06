package neuralnetwork;
import Menu.Menu;
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
        int numberOfGenerations = 1000;
        int generationSize = 750;
        //networkSize[0] must always be 7!
        int[] networkSize = new int[]{10 ,30 ,10 ,4 };
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
            int generation = i+1;
            Menu.appendScore(generation,winner);

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

        /** in snake Simulater onderstaande methodes(bijf. snakeSimulater.distanceToFood()).
         *
         * In playSnake deze gegevens life afbeelden.
         *
         Input should be:
         4 directions.
         Every direction snake will look for:   Distance to food (2 inputs: x and y axis)
                                                Distance to its own body (1 input: total distance)
                                                Distance to a wall (4 inputs: top, bottom, left and right wall)

         7 inputs in total
         */
        int inputLength = 10;
        double[] inputs = new double[inputLength];
        inputs[0] = snake.getDistanceToBottomWall();
        inputs[1] = snake.getDistanceToLeftWall();
        inputs[2] = snake.getDistanceToRightWall();
        inputs[3] = snake.getDistanceToTopWall();
        inputs[4] = snake.getxDistanceToApple();
        inputs[5] = snake.getyDistanceToApple();
        inputs[6] = snake.getRightDistanceToSnake();
        inputs[7] = snake.getLeftDistanceToSnake();
        inputs[8] = snake.getUpDistanceToSnake();
        inputs[9] = snake.getDownDistanceToSnake();


        /*int inputLength = snake.getWidth() * snake.getHeight();
        double[] inputs = new double[inputLength];

        for(int i = 0; i < snake.dots; i++){
            int dotX = snake.getX(i);
            int dotY = snake.getY(i);

            int position = dotY * snake.getWidth() + dotX;
            if(position < 0){
                System.out.println("Error.");
            }

            if( i == 0){
                inputs[position] = 1.0;//head
            }else{
                inputs[position] = 0.666;//body
            }

            int applePosition = snake.apple_y * snake.getWidth() + snake.apple_x;

            inputs[applePosition] = 0.333;//apple
        }*/

        return network.calculate(inputs);

    }
}
