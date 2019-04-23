package neuralnetwork;

public class Network {

    private double[][] output;  //1ste is Layer, 2de is Neuron
    private double[][][] weights;  //1ste Layer, 2de neuron,3de vorige neuron vorige layer
    private double[][] bias;  //per neuron 1 bios

    private double[][] errorSignal;
    private double[][] outputDerivative;

    public final int[] networkLayerSizes;
    public final int inputSize;
    public final int outputSize;
    public final int networkSize;

    public Network(int... networkLayerSizes){
        this.networkLayerSizes = networkLayerSizes;
        this.inputSize = networkLayerSizes[0];
        this.networkSize = networkLayerSizes.length;
        this.outputSize = networkLayerSizes[networkSize-1];

        this.output = new double[networkSize][];
        this.weights = new double[networkSize][][];
        this.bias = new double[networkSize][];

        this.errorSignal = new double[networkSize][];
        this.outputDerivative = new double[networkSize][];

        //// de output, bios en weight arrays vullen.
        for(int i=0;i<networkSize;i++){
            this.output[i] = new double[networkLayerSizes[i]];
            this.errorSignal[i] = new double[networkLayerSizes[i]];
            this.outputDerivative[i] = new double[networkLayerSizes[i]];

            this.bias[i] = NetworkTool.createRandomArray(networkLayerSizes[i],0.0,1.0);

            if(i>0){
                int prevSize = networkLayerSizes[i-1];
                weights[i] = NetworkTool.createRandomArray(networkLayerSizes[i], prevSize, -1.0,1.0/prevSize);
            }
        }
    }

    public void randomize() {
        int fistBiasLayer = NetworkTool.randomValue(1,this.bias.length);
        int secondBiasLayer = NetworkTool.randomValue(0,this.bias[fistBiasLayer].length);
        this.bias[fistBiasLayer][secondBiasLayer] = NetworkTool.randomValue(0.0,1.0);

        int fistWeightsLayer = NetworkTool.randomValue(1,this.weights.length);
        int secondWeightsLayer = NetworkTool.randomValue(0,this.weights[fistWeightsLayer].length);
        int thirdWeightsLayer = NetworkTool.randomValue(0,this.weights[fistWeightsLayer][secondWeightsLayer].length);
        this.weights[fistWeightsLayer][secondWeightsLayer][thirdWeightsLayer] = NetworkTool.randomValue(-1.0,1.0);
    }

    public Network(Network originalNetwork){
        this.networkLayerSizes = originalNetwork.networkLayerSizes.clone();//1
        this.inputSize = originalNetwork.inputSize;
        this.networkSize = originalNetwork.networkSize;
        this.outputSize = originalNetwork.outputSize;

        this.output = cloneArray(originalNetwork.output);//2
        this.weights = cloneArray(originalNetwork.weights);//3
        this.bias = cloneArray(originalNetwork.bias);//2

        this.errorSignal = cloneArray(originalNetwork.errorSignal);//2
        this.outputDerivative = cloneArray(originalNetwork.outputDerivative);//2
    }

    private double[][] cloneArray(double[][] source){
        if(source == null){return null;}
        double[][] destination = new double[source.length][];
        for(int i = 0; i <source.length; i++){
            destination[i] = source[i].clone();
        }
        return destination;
    }

    private double[][][] cloneArray(double[][][] source){
        double[][][] destination = new double[source.length][][];
        for(int i = 0; i <source.length; i++){
            destination[i] = cloneArray(source[i]);
        }
        return destination;
    }

    public double[] calculate(double... input) throws Exception{
        if(input.length!= this.inputSize) throw new Exception("Invalid number of inputs");//return null;
        this.output[0] = input;
        for(int layer =1 ; layer<networkSize; layer++){
            for (int neuron = 0; neuron<networkLayerSizes[layer]; neuron++){
                double sum = bias[layer][neuron];

                for (int prevNeuron = 0; prevNeuron<networkLayerSizes[layer-1]; prevNeuron++){
                    sum += output[layer-1][prevNeuron] * weights[layer][neuron][prevNeuron];
                }
                output[layer][neuron] = sigmoid(sum);
                outputDerivative[layer][neuron] = output[layer][neuron] * (1 -output[layer][neuron]);
            }
        }
        return output[networkSize-1];
    }
    //----------------------////////////////

    public void initializeRandom(){
        for(int layer =1 ; layer<networkSize; layer++){
            for (int neuron = 0; neuron<networkLayerSizes[layer]; neuron++){
                 this.bias[layer][neuron] = (Math.random()*2)-1;

                for (int prevNeuron = 0; prevNeuron<networkLayerSizes[layer-1]; prevNeuron++){
                    this.weights[layer][neuron][prevNeuron]= (Math.random()*2)-1;
                }
            }
        }
    }

    //--------------------//////////////////////////////////////////////////////////--------------------//

    public void train(double[] input, double[] target, double eta) throws Exception{
        if(input.length != inputSize || target.length != outputSize)return;
        calculate(input);
        backpropError(target);
        updateWeights(eta);
    }

    public void backpropError(double[] target){
        for(int neuron = 0; neuron<networkLayerSizes[networkSize-1]; neuron++){
            errorSignal[networkSize-1][neuron] = (output[networkSize-1][neuron]-target[neuron]) * outputDerivative[networkSize-1][neuron];
        }
        for(int layer = networkSize-2; layer>0; layer--){
            for(int neuron = 0; neuron<networkLayerSizes[layer]; neuron++){
                double sum = 0;
                for (int nextNeuron=0; nextNeuron<networkLayerSizes[layer+1];nextNeuron++){
                    sum+=weights[layer+1][nextNeuron][neuron] * errorSignal[layer+1][nextNeuron];
                }
                this.errorSignal[layer][neuron] = sum * outputDerivative[layer][neuron];
            }
        }
    }

    public void updateWeights(double eta){
        for(int layer = 1; layer<networkSize; layer++){
            for(int neuron=0; neuron<networkLayerSizes[layer]; neuron++){
                double delta = - eta *errorSignal[layer][neuron];
                bias[layer][neuron]+=delta;
                for(int prevNeuron=0; prevNeuron< networkLayerSizes[layer-1];prevNeuron++){
                    //weights[layer][neuron][prevNeuron]
                    weights[layer][neuron][prevNeuron]+=delta * output[layer-1][prevNeuron];
                }
            }
        }
    }

    private double sigmoid(double x){
        return 1d/(1+Math.exp(-x));
    }
}
