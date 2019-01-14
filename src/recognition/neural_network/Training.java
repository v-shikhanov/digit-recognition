package recognition.neural_network;

public class Training {

    private Layer[] layers;
    private final int[][] learningImages = {
            {
                    1,1,1,
                    1,0,1,
                    1,0,1,
                    1,0,1,
                    1,1,1
            },
            {
                    0,1,0,
                    0,1,0,
                    0,1,0,
                    0,1,0,
                    0,1,0
            },
            {
                    1,1,1,
                    0,0,1,
                    1,1,1,
                    1,0,0,
                    1,1,1
            },
            {
                    1,1,1,
                    0,0,1,
                    1,1,1,
                    0,0,1,
                    1,1,1
            },
            {
                    1,0,1,
                    1,0,1,
                    1,1,1,
                    0,0,1,
                    0,0,1
            },
            {
                    1,1,1,
                    1,0,0,
                    1,1,1,
                    0,0,1,
                    1,1,1,
            },
            {
                    1,1,1,
                    1,0,0,
                    1,1,1,
                    1,0,1,
                    1,1,1
            },
            {
                    1,1,1,
                    0,0,1,
                    0,0,1,
                    0,0,1,
                    0,0,1
            },
            {
                    1,1,1,
                    1,0,1,
                    1,1,1,
                    1,0,1,
                    1,1,1
            },
            {
                    1,1,1,
                    1,0,1,
                    1,1,1,
                    0,0,1,
                    1,1,1
            }
    };



    public Training (Layer[] layers) {
        this.layers = layers;
    }

    public Layer[] train() {
        resetDeltaWeights();
        for (int i = 0; i < learningImages.length; i++) {
            /*
                in future drawn digit and index in collection of training images could be a different numbers
             */
            trainWithImage(i,learningImages[i]);
        }
        correctWeights();
        return layers;
    }

    private void trainWithImage(int digit, int[] img) {
        layers[0].mountImageToLayer(img);
        findIdealOutputs(digit);

        /*
         * Weights correction layer by layer
         */
        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            int prevLayerLength = layers[layerIndex-1].getNeurons().length;
            int currentLayerLength = layers[layerIndex].getNeurons().length;

            for (int neuronIndex = 0; neuronIndex < currentLayerLength; neuronIndex++) {
                Neuron neuron = layers[layerIndex].getNeurons()[neuronIndex];
                neuron.countOutput(layers[layerIndex-1].getNeurons());
                double sigma = countSigmoidFunction(neuron);
                /*
                    update weight for given input neuron to delta value
                 */
                for (int inNeuronIndex = 0; inNeuronIndex < prevLayerLength; inNeuronIndex++) {
                    Neuron inputNeuron = layers[layerIndex-1].getNeurons()[inNeuronIndex];
                    double weight = layers[layerIndex].getNeurons()[neuronIndex].getDeltaWeights()[inNeuronIndex];
                    weight += foundDeltaWight(inputNeuron, neuron, sigma);
                    layers[layerIndex].getNeurons()[neuronIndex].setDeltaWeight(inNeuronIndex, weight);
                }
            }
        }
    }

    private void resetDeltaWeights() {

        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            for (int neuronIndex = 0; neuronIndex < layers[layerIndex].getNeurons().length; neuronIndex++) {
                layers[layerIndex].getNeurons()[neuronIndex].resetDeltaWeights();
            }
        }
    }

    private void correctWeights() {

        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            int prevLayerLength = layers[layerIndex-1].getNeurons().length;
            int currentLayerLength = layers[layerIndex].getNeurons().length;

            for (int neuronIndex = 0; neuronIndex < currentLayerLength; neuronIndex++) {
                Neuron neuron = layers[layerIndex].getNeurons()[neuronIndex];
                neuron.countOutput(layers[layerIndex-1].getNeurons());
                double sigma = countSigmoidFunction(neuron);
                /*
                    update weight for given input neuron to delta value
                 */
                for (int inNeuronIndex = 0; inNeuronIndex < prevLayerLength; inNeuronIndex++) {
                    double deltaWeight = layers[layerIndex].getNeurons()[neuronIndex].getDeltaWeights()[inNeuronIndex];
                    double meanWeight = deltaWeight/10;
                    double weight = layers[layerIndex].getNeurons()[neuronIndex].getWeights()[inNeuronIndex];
                    layers[layerIndex].getNeurons()[neuronIndex].setWeight(inNeuronIndex, weight + meanWeight);
                }
            }
        }
    }


    private void findIdealOutputs(int digit) {
        /*
           set ideal outputs to output layer
         */
        for (int i = 0; i < layers[layers.length-1].getNeurons().length; i++) {
            int idealOutput = 0;
            if (i == digit) {
                idealOutput = 1;
            }
            layers[layers.length-1].getNeurons()[i].setIdealOutput(idealOutput);
        }

        /*
          count ideal outputs to intermediate layers in backward order
         */
        for (int layerIndex = layers.length -2; layerIndex > 0; layerIndex--) {
            for (int neuronIndex = 0; neuronIndex < layers[layerIndex].getNeurons().length; neuronIndex++) {
                layers[layerIndex].getNeurons()[neuronIndex].countIdealOutput(layers[layerIndex+1].getNeurons(), neuronIndex);
            }
        }
    }



    private double countSigmoidFunction(Neuron n){
        return  1 / (1 + Math.exp(-n.getValue()));
    }

    private double foundDeltaWight(Neuron inputNeuron, Neuron neuron, double sigma) {
        double educationSpeed = 0.5;
        /*
        Maybe need a sigma for intermediate layers val and ideal values. Check this!!!!
         */
        return educationSpeed * inputNeuron.getValue() * (neuron.getIdealOutput() - sigma);
    }
}

