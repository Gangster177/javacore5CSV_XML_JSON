package homework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
//        //TODO Task1
//        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
//        String fileName = "data.csv";
//        List<Employee> list = parseCSV(columnMapping, fileName);
//        String json = listToJson(list);
//        writeString(json);
//
//        //TODO Task2
//        List<Employee> list2 = parseXML("new_data.xml");
//        String json2 = listToJson(list2);
//        writeString(json2);

        //TODO Task3
        String json3 = readString("data.json");
        System.out.println(json3);
    }
//Выполнение задачи следует начать с получения JSON из файла.
// Сделайте это с помощью метода readString():
//
//String json = readString("new_data.json");

//Метод readString() реализуйте самостоятельно с использованием BufferedReader и FileReader.
// Метод должен возвращать прочитанный из файла JSON типа String.
//
//Прочитанный JSON необходимо преобразовать в список сотрудников.
// Сделайте это с помощью метода jsonToList():
//
//List<Employee> list = jsonToList(json);

//При реализации метода jsonToList() вам потребуются такие объекта как:
// JSONParser, GsonBuilder, Gson. JSONParser даст вам возможность с помощью
// метода parse() получить из строчки json массив JSONArray.
// GsonBuilder будет использован исключительно для создания экземпляра Gson.
// Пройдитесь циклом по всем элементам jsonArray и преобразуйте
// все jsonObject в Employee.class с помощью метода gson.fromJson().
// Полученные экземпляры класса Employee добавляйте в список,
// который должен быть выведен из метода после его окончания.
//
//Далее, выведите содержимое полученного списка в консоль.
// Не забудьте переопределить метод toString() в классе Employee.
    private static String readString(String s) {
        String readJSON = null;
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader(s));
            JSONObject jsonObject = (JSONObject) obj;
            readJSON = jsonObject.toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return readJSON;
    }

    public static List<Employee> parseXML(String s) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> employeeList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(s));

        //Получим корневой узел документа:
        Node root = doc.getDocumentElement();
        //Получить список дочерних узлов можно при помощи метода getChildNodes():
        NodeList nodeList = root.getChildNodes();
        //Пробежаться по всем дочерним узлам текущего узла можно с помощью цикла:
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                long id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                String country = element.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                Employee employee = new Employee(id, firstName, lastName, country, age);
                employeeList.add(employee);
            }
        }
        return employeeList;
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
        try (FileWriter writer = new FileWriter("new_data.json")) {
            writer.write(text);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
