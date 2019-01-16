package recognition.neural_network;

import recognition.ImageIDX;
import recognition.ReadIDX;

import java.io.File;
import java.util.ArrayList;

public class Training {

    private Layer[] layers;
    private ArrayList<ImageIDX> trainingCol;


    public Training () {


        trainingCol = new ArrayList<>();
        File trainingImages = new File("train-images.idx3-ubyte");
        File trainingLabels = new File("train-labels.idx1-ubyte");

        trainingCol = new ReadIDX().updateCollection(trainingLabels,trainingImages,trainingCol);

    }

    public Layer[] train(Layer[] layers) {
        this.layers = layers;
        System.out.println("Please wait,learning was started..");
        for (int i = 0; i < trainingCol.size(); i++) {
            /*
                in future drawn digit and index in collection of training images could be a different numbers
             */
            ImageIDX img = trainingCol.get(i);
            updateDeltaWeights(img.getLabel(),img.getPixels());
        }
        correctWeights(trainingCol.size());
        System.out.println("Learning finished, completed for " + trainingCol.size() + " images.");
        return this.layers;
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

