/**
 * @brief This is class for control images recognition
 * @author Vladislav Shikhanov
 **/
package recognition;
import recognition.neural_network.NeuralNetwork;
import recognition.training.TrainingDispatcher;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ImagesRecognition {
    private NeuralNetwork neuralNetwork;
    private ArrayList<ImageIDX> testCol;
    private TrainingDispatcher trainingDispatcher;
    private  Scanner scanner = new Scanner(System.in);
    private static final double educationSpeed = 0.8;

    /**
     * Class constructor
     */
    ImagesRecognition() {
        int[] networkSizes = {784, 16, 16, 10};
        neuralNetwork = new NeuralNetwork(networkSizes);
        testCol = new ArrayList<>();
        trainingDispatcher = new TrainingDispatcher(neuralNetwork.getLayers());

        File testImages = new File("t10k-images.idx3-ubyte");
        File testLabels = new File("t10k-labels.idx1-ubyte");

        testCol = new ReadIDX().updateCollection(testLabels,testImages);
    }

    /**
     * This is method for selecting action with neural network- trying to recognise image, or learn network
     */
    public void selectEnteringMethod() {
        int enteringMethod = (int)getNumber(1, 3,
                "\n\nPlease select operation: \n" +
                "1 = Random image recognition,\n" +
                "2 = Prediction accuracy check,\n" +
                "3 = Learning to goal.\n");

        switch (enteringMethod) {
            case 1 : recognizeRandomImage(); break;

            case 2 : checkAccuracy(); break;

            case 3 : learnToGoal(); break;
        }
    }

    /**
     * Method makes one learning cycle for neural network.
     * @param mode is mode if learning - Delta algorithm or Back propagation
     * @param trainSize is a size of training set(formed by training collection with 60000 images). 0 for full use.
     */
    private void learn(TrainingDispatcher.mode mode, int trainSize) {
        neuralNetwork.setLayers(trainingDispatcher.train(neuralNetwork.getLayers(), educationSpeed, trainSize, mode));
        neuralNetwork.save();
    }

    /**
     *  Method that selects random image from test collection and trying to recognize it. Going learning in case of
     *  incorrect result
     */
    private void recognizeRandomImage() {
        ImageIDX img = testCol.get(new Random().nextInt(testCol.size()));
        img.printImage();
        int recognizedDigit = neuralNetwork.recognizeDigit(img.getPixels());
        System.out.println("\nThe digit on picture is " + recognizedDigit);

        if (img.getLabel() != recognizedDigit) {
            System.out.println("Recognized incorrect, going learning!");
            learn(TrainingDispatcher.mode.BACKPROP, 1000);
        }
    }

    /**
     * That method for recognise every image in test collection and define accuracy  of recognition
     * @return accuracy in percents
     */
    private double checkAccuracy() {
        double checkingResult;
        double success = 0;

        for (ImageIDX img : testCol) {
            if (img.getLabel() == neuralNetwork.recognizeDigit(img.getPixels())) {
                success++;
            }
        }

        checkingResult = success / testCol.size() * 100;
        System.out.println("Prediction accuracy is " + checkingResult + "%");
        return checkingResult;
    }

    /**
     *  Method completes learning cycles and trying to reach goal of accuracy for neural network.
     *  User can set accuracy in percents, max number of attempts and number of learning cycles in one attempt
     */
    private void learnToGoal() {
        int tryNumber = 0;
        double accuracy;
        double goal = getNumber(0, 100,
                "Please, insert prediction accuracy learning goal in percent");
        int maxAttempts = (int)getNumber(1, Integer.MAX_VALUE,
                "Please, insert max tryings in learning process");
        int learnsBetweenChecks = (int)getNumber(1, Integer.MAX_VALUE,
                "Please, insert number of learning cycles between accuracy checks");

        int trainColSize = (int)getNumber(0, 60000,
                "Please, insert size(1-60000) of learning collection for one learning cycle." +
                        " 0 For full 60000 collection use");

        TrainingDispatcher.mode mode;

        if (getNumber(0, 1,
                "Please, insert learning mode 0- for Delta mode, 1- for Back propagation mode") == 0) {
            mode = TrainingDispatcher.mode.DELTA;
        } else {
            mode = TrainingDispatcher.mode.BACKPROP;
        }

        while (true) {
            System.out.println("Learning try number " + tryNumber + " with " + learnsBetweenChecks + " learning" +
                    " cycles in one try was started \n");

            for (int i = 0; i < learnsBetweenChecks; i++) {
                learn(mode, trainColSize);
            }

            accuracy = checkAccuracy();

            if (accuracy > goal) {
                System.out.println("Learning reached goal, number of learning cycles is " + tryNumber * learnsBetweenChecks);
                break;
            }

            tryNumber++;

            if (tryNumber > maxAttempts) {
                System.out.println("Trying number is bigger than max value, learning not reached goal :((");
                break;
            }
        }
    }

    /**
     * Method for get number selected by user. Number lays inside of diapason.
     * @param minLimit - lower limit of diapason
     * @param highLimit - higher limit of diapason
     * @param text - text printed for user
     * @return selected number
     */
    private double getNumber(double minLimit, double highLimit, String text) {
        double number = -1;
        while (number < minLimit || number > highLimit) {
            System.out.println(text);
            if (scanner.hasNextDouble()) {
                number = scanner.nextDouble();
                break;
            } else {
                scanner.next();
            }
        }
        return number;
    }
}
