package digitRecognition;

import java.io.*;
import java.util.Scanner;

public class ReadIDX {
    public static void main(String[] args) {
        readIDX();
    }

    static void readIDX() {
        File[] file = GetFiles();



        File file1 = new File("C:\\java_learn\\lessons\\file.txt");

        try (FileInputStream stream = new FileInputStream(file[0])) {
            try (FileWriter writer = new FileWriter(file1)) {
                byte[] gotInfo = stream.readAllBytes();
                for (int i = 0; i < gotInfo.length; i++) {
                    writer.write(gotInfo[i]);
                }


            } catch (IOException exc) {
            }

        } catch (IOException exc) {
            System.out.println("Problem with file reading");
        }


//        for (int i =0; i < file.length; i++) {
//
//        }

    }


    static File[] GetFiles() {
        String folderPath = getFolderPath();
        File directory = new File(folderPath);
        System.out.println("Selected folder path: " + folderPath);

        if (directory.isDirectory() && directory.list() != null) {
            File[] file = directory.listFiles();
            System.out.println("Next files were found in folder:");
            for (int i = 0; i < file.length; i++) {
                System.out.println(file[i].getName());
            }
            return file;
        } else {
            System.out.println("File path is incorrect");
        }
        return null;
    }


    static String getFolderPath(){
        Scanner sc = new Scanner(System.in);
        String path = new String();
        String standardPath = "C:\\java_learn\\digit-recognition\\images";

        System.out.println("Path to folder with images set \n");
        System.out.println("Type N to skip this process and use the default path");
        System.out.println("Type any symbol to continue");

        if (sc.hasNext("N")){
            return standardPath;
        }
        sc.nextLine();



        System.out.println("Please, Enter the disk name \n Type esc to skip this process and use the default path");
        path = path.concat(sc.nextLine());

        if ( path.contentEquals("esc")) {
            return standardPath;
        }

        path = path.concat(":\\");

        for ( int i = 0; i < 100; i++ ){
            System.out.println("Please, Enter the folder number " + i + " name \n" +
                    "Type esc if you finished");

            if (sc.hasNext("esc")){
                sc.nextLine();
                return path;
            } else {
                path = path.concat(sc.nextLine() + "\\");
            }
        }
        return path;
    }

}
