package recognition.simplestNeuron;


import java.io.*;

public class OutputNeuronsCollection implements Serializable {
    private static final long serialVersionUID = 123L;
    private SimplestNeuron[] outputNeurons = new SimplestNeuron[10];

    public OutputNeuronsCollection() {
        File savedNeurons = new File("neurons.tmp");
        if (!savedNeurons.isFile()) {
            for (int i =0; i < 10; i++) {
                outputNeurons[i] = new SimplestNeuron();
            }
            return;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(savedNeurons);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            outputNeurons = (SimplestNeuron[])objectInputStream.readObject();
            System.out.println("\nNeurons are load from file");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void learnNeurons() {
        outputNeurons = new Learning().correctWeights(outputNeurons);
        save();

    }

    public void save() {
        File savedNeurons = new File("neurons.tmp");
        if (!savedNeurons.isFile()) {
            try {
                savedNeurons.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("\nFile creation is impossible!");
                return;
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(savedNeurons);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(outputNeurons);
            objectOutputStream.close();
            System.out.println("Neurons saved to file");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SimplestNeuron[] getOutputNeurons() {
        return outputNeurons;
    }



}
