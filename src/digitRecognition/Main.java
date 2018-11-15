package digitRecognition;

public class Main {
    public static void main(String[] args) {
        ReadIDX readIDX = new ReadIDX();
        ErrorFunction errorFunction = new ErrorFunction();
        readIDX.getImages(false, true);
        errorFunction.randomClassifier();
    }
}