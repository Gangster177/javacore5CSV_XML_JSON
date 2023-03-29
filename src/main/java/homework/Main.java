package homework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting(); //-метод для форматирования
        Gson gson = builder.create();
        //Для преобразования списка объектов в JSON,
        //требуется определить тип этого спика:
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        //Получить JSON из экземпляра класса Gson можно с помощтю метода toJson()
        return gson.toJson(list, listType);
    }

    public static void writeString(String text) {
        try (FileWriter writer = new FileWriter("lectureResources/data.json")) {
            writer.write(text);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
