package recognition;

import recognition.neural_network.NeuralNetwork;
import recognition.neural_network.Training;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ImagesManager {
    private NeuralNetwork neuralNetwork;
    private ArrayList<ImageIDX> testCol;
    private Training training;
    private Scanner scanner = new Scanner(System.in);
    private int[] networkSizes = {784, 10};



    ImagesManager() {
        this.neuralNetwork = new NeuralNetwork(networkSizes);
        this.testCol = new ArrayList<>();
        this.training = new Training();

        File testImages = new File("t10k-images.idx3-ubyte");
        File testLabels = new File("t10k-labels.idx1-ubyte");

        testCol = new ReadIDX().updateCollection(testLabels,testImages,testCol);
    }

    public void selectEnteringMethod() {


        int enteringMethod = -1;
        while (enteringMethod < 0 || enteringMethod > 2) {
            System.out.println("\n\nPlease select operation: " +
                    "\n0 = Learning,\n" +
                    "1 = Random image,\n" +
                    "2 = Prediction accuracy check\n"
            );
            if (scanner.hasNextInt()) {
                enteringMethod = scanner.nextInt();
            } else {
                scanner.next();
            }
        }

        switch (enteringMethod) {
            case 0 : learn(); break;

            case 1 : randomImageRecongize(); break;

            case 2 :  break;
        }
    }

    private void learn() {
        neuralNetwork.setLayers(training.train(neuralNetwork.getLayers()));
        neuralNetwork.save();
    }

    private void randomImageRecongize() {
        ImageIDX img = testCol.get(new Random().nextInt(testCol.size()));
        int recognizedDigit;

        img.printImage();

        recognizedDigit = neuralNetwork.recognizeDigit(img.getPixels());

        System.out.println("\nThe digit on picture is " + recognizedDigit);

        if (img.getLabel() != recognizedDigit) {
            System.out.println("Recognized incorrect, going learning!");
            learn();
        }
    }

//    private void digitRecognize(int[] inputNeurons) {
//        int userReaction = -1;
//        int recognizedDigit;
//
//        recognizedDigit = neuralNetwork.recognizeDigit(inputNeurons);
//
//        System.out.println("\nThe digit on picture is " + recognizedDigit);
//
//        if (selectedImage != recognizedDigit && selectedImage < 10) {
//            System.out.println("Recognized incorrect, going learning!");
//            neuralNetwork.learn();
//            return;
//        }
//
//        if (selectedImage == 10) {
//            while (userReaction < 0 || userReaction >1) {
//                System.out.println("Recognition result is correct? Type 1 if ok, or 0 if not");
//                if (scanner.hasNextInt()) {
//                    userReaction = scanner.nextInt();
//                } else {
//                    scanner.next();
//                }
//            }
//
//            if (userReaction == 0) {
//                System.out.println("Recognized incorrect, going learning!");
//                neuralNetwork.learn();
//            } else {
//                System.out.println("Really good!");
//            }
//        }
//    }

}
