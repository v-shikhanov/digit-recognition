package recognition.simplestNeuron;

import java.util.Random;
import java.util.Scanner;

public class ImagesManager {
    private Scanner scanner = new Scanner(System.in);

    public void selectEnteringMethod() {

        char[][] image = new char[3][3];
        int enteringMethod = -1;
        while (enteringMethod < 0 || enteringMethod >3) {
            System.out.println("Please select entering method: " +
                    "\n0 = manual image entering,\n" +
                    "1 = random generator,\n" +
                    "2 = pre-defined images");
            if (scanner.hasNextInt()) {
                enteringMethod = scanner.nextInt();
            } else {
                scanner.next();
            }

        }

        switch (enteringMethod) {
            case 0 : image = getUserImage(); break;

            case 1 : image = createRandomImage(); break;

            case 2 : image = usePredefinedImages(); break;

        }

        System.out.println("\nNumber printed on image is " + convertImage(image) + "\n");
    }

    private char[][] getUserImage() {
        char[][] image = new char[3][3];
        String line;
        System.out.println("Input 3x3 grid ('_' for white, any other for black). Three strings, 3 symbol in one");
        for (int string = 0; string < 3; string++) {
            do {
                line = scanner.next();
                if (line.length() != 3) {
                    System.out.println("String should contents exactly 3 symbols");
                }
            } while (line.length() !=3);

            for (int row = 0; row < 3; row++) {
                image[string][row] = line.charAt(row);
            }
        }
        return image;
    }

    private char[][] createRandomImage() {
        char[][] image = new char[3][3];
        for (int string = 0; string < 3; string++) {
            for (int row = 0; row < 3; row++) {
                if (new Random().nextBoolean()){
                    image[string][row] = '_';
                } else {
                    image[string][row] = 'X';
                }
            }
        }
        return image;
    }

    private char[][] usePredefinedImages() {
        char[][] i1 =  {{'X','X','X'},
                        {'X','_','X'},
                        {'X','X','X'}};

        char[][] i2 =  {{'_','X','X'},
                        {'X','_','X'},
                        {'X','X','X'}};

        char[][] i3 =  {{'_','X','_'},
                        {'_','X','_'},
                        {'_','X','_'}};

        char[][] i4 =  {{'X','X','_'},
                        {'_','X','_'},
                        {'_','X','_'}};

        char[][] i5 =  {{'_','X','_'},
                        {'_','X','_'},
                        {'X','X','_'}};

        char[][] i6 =  {{'_','X','_'},
                        {'X','_','X'},
                        {'_','X','_'}};

        switch (new Random().nextInt(6)) {
            case 0 : return i1;
            case 1 : return i2;
            case 2 : return i3;
            case 3 : return i4;
            case 4 : return i5;
            case 5 : return i6;
        }
        return i6;
    }



    private int convertImage(char[][] img) {
        int[][] inputNeurons = new int[3][3];
        char cell;


        for (int string = 0; string < 3; string++) {
            System.out.print("\n");
            for (int row = 0; row < 3; row++) {
                cell = img[string][row];
                System.out.print(cell);
                if (cell == '_') {
                    inputNeurons[string][row] = 0;
                } else {
                    inputNeurons[string][row] = 1;
                }
            }
        }

        return new SimplestNeuron().outputNeuron(inputNeurons);
    }

}
