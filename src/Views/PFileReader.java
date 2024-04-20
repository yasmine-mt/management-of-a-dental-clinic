package Views;
import src.entity.Patient;
import src.entity.enums.CategorieAntecedentsMedicaux;
import src.entity.enums.GroupeSanguin;
import src.entity.enums.Mutuelle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PFileReader {


    public static List<Patient> readPatientsFromFile(String filePath) throws IOException {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|"); // Assuming "|" as the delimiter
                if (parts.length >= 11) { // Ensure there are at least 11 elements

                    // Extract the 11th part and trim any extra spaces
                    String categoryString = parts[10].trim();

                    // Convert the string to the corresponding enum value for antecedents
                    CategorieAntecedentsMedicaux category;
                    try {
                        category = CategorieAntecedentsMedicaux.valueOf(categoryString);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid category: " + categoryString + " for line: " + line);
                        continue; // Skip this iteration or handle as needed
                    }

                    Patient patient = new Patient(
                            parts[1],
                            parts[2],
                            parts[3],
                            Long.parseLong(parts[0]),
                            parts[4],
                            parts[5],
                            parts[6],
                            LocalDate.parse(parts[7]),
                            Mutuelle.valueOf(parts[8]),
                            GroupeSanguin.valueOf(parts[9]),
                            Collections.singletonList(category),  // Use the enum value
                            parts[11]
                    );

                    patients.add(patient);
                } else {
                    System.err.println("Invalid line format: " + line); // Log a warning message
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error reading file: " + e.getMessage()); // Log the error message
            throw e; // Optionally rethrow the exception or handle it as appropriate
        }
        return patients;
    }



}
