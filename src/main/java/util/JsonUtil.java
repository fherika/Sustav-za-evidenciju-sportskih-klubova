package util;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final Jsonb jsonb = JsonbBuilder.create();

    public static <T> List<T> loadList(String path) {
        try {
            String json = Files.readString(Paths.get(path));
            return jsonb.fromJson(
                    json,
                    new ArrayList<T>() {}.getClass().getGenericSuperclass()
            );
        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
            e.printStackTrace();
        }catch (JsonbException e) {
            System.err.println("Greška pri deserijalizaciji JSON-a: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static <T> void saveList(String filePath, List<T> list) {
        try (FileWriter writer = new FileWriter(filePath)) {
            jsonb.toJson(list, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON file: " + filePath, e);
        }
    }
}
