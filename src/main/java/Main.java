import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, CsvException {
        Path csvFilePath = Paths.get("src/main/java/file.csv");
        CsvOperations csvOperations = new CsvOperations(csvFilePath);

        System.out.println("Initial Animals:");
        List<String[]> records = csvOperations.readAll();
        for (String[] record : records) {
            System.out.println(String.join(", ", record));
        }
        System.out.println("\nAdding new animals ");
        csvOperations.create(new Animal("Cow", "Daisy"));

        records = csvOperations.readAll();
        for (String[] record : records) {
            System.out.println(String.join(", ", record));
        }

        System.out.println("\nUpdating animal");
        csvOperations.update(new Animal("Dog", "Tommy"));


        records = csvOperations.readAll();
        for (String[] record : records) {
            System.out.println(String.join(", ", record));
        }

        System.out.println("\nDelete an animal");
        csvOperations.delete("Cow");
        records = csvOperations.readAll();
        for (String[] record : records) {
            System.out.println(String.join(", ", record));
        }
    }


}