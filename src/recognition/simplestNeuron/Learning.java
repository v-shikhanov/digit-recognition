package recognition.simplestNeuron;

public class Learning {
    private double[][] deltaWeights = new double[10][15];
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



    public Learning () {
    }

    public SimplestNeuron[] correctWeights(SimplestNeuron[] outputNeurons){
        for (int outNeuronIndex = 0; outNeuronIndex < 10; outNeuronIndex++) {
            for (int imageNumber = 0; imageNumber < 10; imageNumber++) {
                double sigma = countSigmoidFunction(outputNeurons[outNeuronIndex], learningImages[imageNumber]);
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

                newWeight = outputNeurons[outNeuronIndex].getWeight()[pixelNumber] + meanDeltaWeight / 10;
                outputNeurons[outNeuronIndex].setWeight(pixelNumber, newWeight);
            }
        }
        return outputNeurons;
    }

    public double countSigmoidFunction(SimplestNeuron n, int[] inputNeurons){
        return  1 / (1 + Math.exp(-n.outputNeuron(inputNeurons)));
    }

    private double foundDeltaWight(int imageNumber, int pixelNumber, int idealValue, double sigma) {
        double educationSpeed = 0.5;
        return educationSpeed * learningImages[imageNumber][pixelNumber]*(idealValue - sigma);
    }
}
