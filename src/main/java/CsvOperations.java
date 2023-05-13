import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class CsvOperations {
    private final Path csvFilePath;

    public CsvOperations(Path csvFilePath) throws IOException {
        this.csvFilePath = csvFilePath;
        createFileIfNotExists();
    }

    public List<String[]> readAll() throws IOException, CsvException {
        try (CSVReader reader = new CSVReaderBuilder(Files.newBufferedReader(csvFilePath))
                .withSkipLines(0)
                .build()) {
            return reader.readAll();
        }
    }

    public void create(Animal animal) throws IOException, CsvException {
        if (searchIfAnimalExists(animal.getAnimalType()) == -1) {
            try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(csvFilePath, java.nio.file.StandardOpenOption.APPEND))) {
                writer.writeNext(new String[]{animal.getAnimalType(),animal.getAnimalName()});
            }
        }else
        {
            System.out.println("Animal " + animal.getAnimalType() + " already exists");
        }
    }


    public int searchIfAnimalExists(String animalType) throws IOException, CsvException {
        List<String[]> records = readAll();
        int firstNameIndex = 0;

        for (int i = 1; i < records.size(); i++) {
            String[] record = records.get(i);
            if (record.length > firstNameIndex && record[firstNameIndex].equals(animalType)) {
                return i;
            }
        }

        return -1;
    }


    public void update(Animal animal) throws IOException, CsvException {
        int animalRow = searchIfAnimalExists(animal.getAnimalType());
        if (animalRow != -1) {
            List<String[]> records = readAll();
            records.set(animalRow, new String[]{animal.getAnimalType(),animal.getAnimalName()});
            try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(csvFilePath))) {
                writer.writeAll(records);
            }
        }else {
            System.out.println("Animal doesn't exist");
        }
    }

    public void delete(String animalType) throws IOException, CsvException {
        int animalRow = searchIfAnimalExists(animalType);
        List<String[]> records = readAll();
        if (animalRow != -1) {
            records.remove(animalRow);
            try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(csvFilePath))) {
                writer.writeAll(records);
            }
        }else {
            System.out.println("Animal doesn't exist");
        }
    }

    private void createFileIfNotExists() throws IOException {
        if (!Files.exists(csvFilePath)) {
            Files.createFile(csvFilePath);
            try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(csvFilePath))) {
                writer.writeNext(new String[]{"Animal", "Name"});
            }
        }
    }
}
