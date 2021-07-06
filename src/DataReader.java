import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataReader {
    public static Scanner readFile(String fileName) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        Scanner scanner = new Scanner(fileInputStream);
        return scanner;
    }
}
