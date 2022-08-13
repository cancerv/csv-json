import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> columnMapping = new HashMap<String, String>();
        columnMapping.put("id", "id");
        columnMapping.put("firstName", "firstName");
        columnMapping.put("lastName", "lastName");
        columnMapping.put("country", "country");
        columnMapping.put("age", "age");

        String fileName = "./data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);
        writeString(json, "./data.json");
    }

    public static List<Employee> parseCSV(Map<String, String> column, String fileName) {
        HeaderColumnNameTranslateMappingStrategy<Employee> strategy = new HeaderColumnNameTranslateMappingStrategy<Employee>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping(column);

        try {
            CSVReader csvReader = new CSVReader(new FileReader(fileName));
            CsvToBean csvToBean = new CsvToBean();
            csvToBean.setCsvReader(csvReader);
            csvToBean.setMappingStrategy(strategy);
            List<Employee> list = csvToBean.parse();
            return list;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public static String listToJson(List<Employee> list) {
        return new Gson().toJson(list);
    }

    public static void writeString(String string, String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(string);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
