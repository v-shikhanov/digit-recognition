package recognition.simplestNeuron;

public class Learning {

    private int[][] learningImages = {
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
                    1, 1, 1,
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

    private SimplestNeuron[] outNeurons = new SimplestNeuron[10];
    private double[][] deltaWeights = new double[10][15];
    private double[] meanDeltaWeight = new double[15];

    public void neuronsInit() {
        int[] weight = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (int i =0; i < 10; i++) {
            outNeurons[i] = new SimplestNeuron(weight);
        }
    }

    private void  correctWeights(){
        for (int outNeuronIndex =0; outNeuronIndex < 10; outNeuronIndex++){
            for (int imageNumber =0; imageNumber < 10; imageNumber++) {
                double sigma = countSigmoidFunction(outNeurons[outNeuronIndex], learningImages[imageNumber]);
                int idealNumber;
                if (imageNumber == outNeuronIndex) {
                    idealNumber = 1;
                } else {
                    idealNumber = 0;
                }
                for (int pixelNumber = 0; pixelNumber < 15; pixelNumber++){
                    deltaWeights[imageNumber][pixelNumber] = foundDeltaWight(imageNumber, pixelNumber, idealNumber, sigma);
                }
            }
            for (int pixelNumber = 0; pixelNumber < 15; pixelNumber++) {
                for (int imageNumber = 0; imageNumber < 10; imageNumber++) {
                    meanDeltaWeight[pixelNumber] +=
                }
            }

        }
    }

    private double countSigmoidFunction(SimplestNeuron n, int[] inputNeurons){
        return  1 / (1 + Math.exp(-n .outputNeuron(inputNeurons)));
    }

    private double foundDeltaWight(int imageNumber, int pixelNumber, int idealValue, double sigma) {
        double educationSpeed = 0.5;
        return educationSpeed * learningImages[imageNumber][pixelNumber]*(idealValue - sigma);
    }



}
