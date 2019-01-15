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
        for (int i = 0; i < learningImages.length; i++) {
            /*
                in future drawn digit and index in collection of training images could be a different numbers
             */
            updateDeltaWeights(i,learningImages[i]);
        }
        correctWeights(learningImages.length);
        return layers;
    }

    private void updateDeltaWeights(int digit, int[] img) {
        layers[0].mountImageToLayer(img);

        findIdealOutputs(digit);

        /*
         * Weights correction layer by layer
         */
        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            double[] prevLayerValues = layers[layerIndex-1].getValues();
            layers[layerIndex].updateValues(prevLayerValues);
            layers[layerIndex].castValuesWithSigmoid();
            layers[layerIndex].updateDeltaWights(0.5,prevLayerValues);
        }
    }



    private void correctWeights(int divider) {

        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            layers[layerIndex].correctWights(divider);
        }
    }


    private void findIdealOutputs(int digit) {
        /*
           set ideal outputs to output layer
         */
        layers[layers.length-1].setIdealValues(digit);


        /*
          count ideal outputs to intermediate layers in backward order
         */
        for (int layerIndex = layers.length -2; layerIndex > 0; layerIndex--) {
            layers[layerIndex].setIdealOutputs(layers[layerIndex+1]);
        }
    }
}

