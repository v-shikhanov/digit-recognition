package recognition;

import recognition.neural_network.NeuralNetwork;

import java.util.Random;
import java.util.Scanner;

public class ImagesManager {
    private Scanner scanner = new Scanner(System.in);
    private int[] networkSizes = {15, 12, 10};
    private NeuralNetwork neuralNetwork = new NeuralNetwork(networkSizes);

    private int selectedImage;

    public void selectEnteringMethod() {

        int[] image = new int[15];
        int enteringMethod = -1;
        while (enteringMethod < 0 || enteringMethod > 3) {
            System.out.println("\n\nPlease select operation: " +
                    "\n0 = manual image entering,\n" +
                    "1 = random generator,\n" +
                    "2 = pre-defined images \n" +
                    "3 = Self learning mode with pre-defined images\n"
            );
            if (scanner.hasNextInt()) {
                enteringMethod = scanner.nextInt();
            } else {
                scanner.next();
            }
        }

        switch (enteringMethod) {
            case 0 : image = getUserImage(); break;

            case 1 : image = createRandomImage(); break;

            case 2 : image = usePredefinedImages(new Random().nextInt(10)); break;

            case 3 : selfLearn(); return;

        }

        printImage(image);
        digitRecognize(image);
    }

    private int[] getUserImage() {
        int[] image = new int[15];
        String line;
        System.out.println("Input 3x5 grid('_' for white, any other for black).Five strings, 3 symbol in one, or one ");
        for (int string = 0; string < 5; string++) {
            do {
                line = scanner.next();
                if (line.length() != 3) {
                    System.out.println("String should contents exactly 3 symbols");
                }
            } while (line.length() !=3);

            for (int i = 0; i < 3; i++) {
                int index = i + string*3;
                image[index] = line.charAt(i);
            }
        }
        selectedImage = 10;
        return image;
    }

    private int[] createRandomImage() {
        int[] image = new int[15];
        for (int i = 0; i < 15; i++) {
            if (new Random().nextBoolean()){
                image[i] = 0;
            } else {
                image[i] = 1;
            }
        }
        selectedImage = 10;
        return image;
    }

    private void  selfLearn () {
        int totalAttempts = 0;
        int learningCycles = 0;

        boolean learningComplete = false;

        System.out.println("Learning is started, please wait");
        while (!learningComplete) {
            learningComplete = true;
            for (int i = 0; i < 10; i++) {
                totalAttempts++;
                int[] img = usePredefinedImages(i);

                if ( neuralNetwork.recognizeDigit(img) != i) {
                    neuralNetwork.learn();
                    learningCycles++;
                    learningComplete = false;
                    break;
                }
            }

        }

        System.out.println("Learning finished! Total attempts: " + totalAttempts +
                " Learning cycles: " + learningCycles );
        neuralNetwork.save();

    }

    private int[] usePredefinedImages(int id) {
        int[] i0 =  {
                1,1,1,
                1,0,1,
                1,0,1,
                1,0,1,
                1,1,1,
        };

        int[] i1=  {
                0,1,0,
                0,1,0,
                0,1,0,
                0,1,0,
                0,1,0,
        };

        int[] i2 =  {
                1,1,1,
                0,0,1,
                1,1,1,
                1,0,0,
                1,1,1,
        };

        int[] i3 =  {
                1,1,1,
                0,0,1,
                1,1,1,
                0,0,1,
                1,1,1,
        };

        int[] i4 =  {
                1,0,1,
                1,0,1,
                1,1,1,
                0,0,1,
                0,0,1,
        };

        int[] i5 =  {
                1,1,1,
                1,0,0,
                1,1,1,
                0,0,1,
                1,1,1,
        };

        int[] i6 =  {
                1,1,1,
                1,0,0,
                1,1,1,
                1,0,1,
                1,1,1,
        };

        int[] i7 =  {
                1,1,1,
                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,
        };

        int[] i8 =  {
                1,1,1,
                1,0,1,
                1,1,1,
                1,0,1,
                1,1,1,
        };

        int[] i9 =  {
                1,1,1,
                1,0,1,
                1,1,1,
                0,0,1,
                1,1,1,
        };

        selectedImage = id;
        switch (id) {
            case 0 : return i0;
            case 1 : return i1;
            case 2 : return i2;
            case 3 : return i3;
            case 4 : return i4;
            case 5 : return i5;
            case 6 : return i6;
            case 7 : return i7;
            case 8 : return i8;
            case 9 : return i9;
        }
        return i9;
    }

    private void printImage (int[] img) {
        System.out.printf("\n");
        for(int i =1; i <= img.length; i++) {
            if (img[i-1] == 0) {
                System.out.print("  ");
            } else {
                System.out.print("* ");
            }

            if (i%3 == 0) {
                System.out.print("\n");
            }
        }
    }


    private void digitRecognize(int[] inputNeurons) {
        int userReaction = -1;
        int recognizedDigit;

        recognizedDigit = neuralNetwork.recognizeDigit(inputNeurons);

        System.out.println("\nThe digit on picture is " + recognizedDigit);

        if (selectedImage != recognizedDigit && selectedImage < 10) {
            System.out.println("Recognized incorrect, going learning!");
            neuralNetwork.learn();
            return;
        }

        if (selectedImage == 10) {
            while (userReaction < 0 || userReaction >1) {
                System.out.println("Recognition result is correct? Type 1 if ok, or 0 if not");
                if (scanner.hasNextInt()) {
                    userReaction = scanner.nextInt();
                } else {
                    scanner.next();
                }
            }

            if (userReaction == 0) {
                System.out.println("Recognized incorrect, going learning!");
                neuralNetwork.learn();
            } else {
                System.out.println("Really good!");
            }
        }
    }

}
