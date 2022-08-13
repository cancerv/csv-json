import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
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

        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);
        writeString(json, "data.json");

        list = parseXML("data.xml");
        json = listToJson(list);
        writeString(json, "data2.json");
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

    public static List<Employee> parseXML(String fileName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Employee> employees = new ArrayList<>();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            FileInputStream fis = new FileInputStream(fileName);
            InputSource is = new InputSource(fis);
            Document doc = builder.parse(is);

            Element element = doc.getDocumentElement();
            NodeList nodes = element.getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) {
                NodeList itemNode = nodes.item(i).getChildNodes();
                if (itemNode.getLength() > 0) {
                    Employee employee = new Employee();
                    for (int j = 0; j < itemNode.getLength(); j++) {
                        switch (itemNode.item(j).getNodeName()) {
                            case "id" -> employee.id = Integer.parseInt(itemNode.item(j).getTextContent());
                            case "firstName" -> employee.firstName = itemNode.item(j).getTextContent();
                            case "lastName" -> employee.lastName = itemNode.item(j).getTextContent();
                            case "country" -> employee.country = itemNode.item(j).getTextContent();
                            case "age" -> employee.age = Integer.parseInt(itemNode.item(j).getTextContent());
                        }
                    }
                    employees.add(employee);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return employees;
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
