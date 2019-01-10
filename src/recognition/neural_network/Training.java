package recognition.neural_network;

public class Training {
    private double[][] deltaWeights;
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



    public Training () {
        deltaWeights = new double[10][15];
    }

    public Layer correctWeights(Layer outputNeurons){
        for (int outNeuronIndex = 0; outNeuronIndex < 10; outNeuronIndex++) {
            for (int imageNumber = 0; imageNumber < 10; imageNumber++) {
                double sigma = countSigmoidFunction(outputNeurons.getNeurons()[outNeuronIndex], learningImages[imageNumber]);
                int idealNumber;

                if (imageNumber == outNeuronIndex) {
                    idealNumber = 1;
                } else {
                    idealNumber = 0;
                }

                for (int pixelNumber = 0; pixelNumber < 15; pixelNumber++) {
                    deltaWeights[imageNumber][pixelNumber] = foundDeltaWight(imageNumber, pixelNumber, idealNumber, sigma);
                }
            }
            for (int pixelNumber = 0; pixelNumber < 15; pixelNumber++) {
                double meanDeltaWeight = 0;
                double newWeight;

                for (int imageNumber = 0; imageNumber < 10; imageNumber++) {
                    meanDeltaWeight += deltaWeights[imageNumber][pixelNumber];
                }

                newWeight = outputNeurons.getNeurons()[outNeuronIndex].getWeights()[pixelNumber] + meanDeltaWeight / 10;
                outputNeurons.getNeurons()[outNeuronIndex].setWeight(pixelNumber, newWeight);
            }
        }
        return outputNeurons;
    }

    private double countSigmoidFunction(Neuron n, int[] inputNeurons){
        n.countOutput(inputNeurons);
        return  1 / (1 + Math.exp(-n.getValue()));
    }

    private double foundDeltaWight(int imageNumber, int pixelNumber, int idealValue, double sigma) {
        double educationSpeed = 0.5;
        return educationSpeed * learningImages[imageNumber][pixelNumber]*(idealValue - sigma);
    }
}
