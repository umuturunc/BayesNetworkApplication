import java.io.FileWriter;
import java.util.ArrayList;

public class Results {
    private static ArrayList<String> originalRecords = new ArrayList<>();
    private static ArrayList<String> predictions = new ArrayList<>();
    private static ArrayList<Float> predictionProbabilities = new ArrayList<>();
    private static Integer TP = 0, TN = 0, FP = 0, FN = 0;
    private static Float TPrate = 0f, TNrate = 0f, accuracy = 0f;


    public void addOriginalRecords(ArrayList<String> originalRecords) {
        this.originalRecords = originalRecords;

    }

    public void addPredictions(ArrayList<String> predictions) {
        this.predictions = predictions;
    }

    public void addPredictionProbabilities(ArrayList<Float> predictionProbabilities) {
        this.predictionProbabilities = predictionProbabilities;
    }

    //Confusion matrix
    public void calculate() {
        for (int i = 0; i < originalRecords.size(); i++) {
            String record = originalRecords.get(i);
            String prediction = predictions.get(i);
            if (record.equals(prediction)) {
                if (record.equals("good")) {
                    TP++;
                } else {
                    TN++;
                }
            } else {
                if (record.equals("good")) {
                    FN++;
                } else {
                    FP++;
                }
            }
        }

        TPrate = (Float.valueOf(TP) / (TP + FP));
        TNrate = (Float.valueOf(TN) / (TN + FN));

        accuracy = (Float.valueOf(TP + TN) / (TP + TN + FP + FN));

    }

    public void printValues() {
        try {
            FileWriter fileWriter = new FileWriter("Results.txt");
            fileWriter.write("Toplam Doğru Pozitif(TP) adedi:\t" + TP);
            fileWriter.write("\n");
            fileWriter.write("Toplam Doğru Negatif(TN) adedi:\t" + TN);
            fileWriter.write("\n");
            fileWriter.write("Doğru Pozitif oranı(TP rate)\t" + TPrate);
            fileWriter.write("\n");
            fileWriter.write("Doğru Negatif oranı(TN rate)\t" + TNrate);
            fileWriter.write("\n");
            fileWriter.write("Doğruluk oranı(Accuray):\t" + accuracy);
            fileWriter.write("\n");
            fileWriter.write("Overall Score için tahmin ettiği değer:\n");
            fileWriter.write("Orjinal\t\tTahmin\t\tolasılık\n");

            for (int i = 0; i < originalRecords.size(); i++) {
                fileWriter.write(originalRecords.get(i) + "\t\t" + predictions.get(i) + "\t\t" + predictionProbabilities.get(i) + "\n");
            }
            fileWriter.write("");


            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

