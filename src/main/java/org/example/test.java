package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class test {
    public static void main(String[] args) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/main/java/org/example/test.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONObject employeeList = (JSONObject) obj;
            System.out.println(employeeList);

            //Iterate over employee array
//            employeeList.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private static void parseEmployeeObject(JSONObject emp) {
        JSONObject employeeObject = (JSONObject) emp.get("d");
        System.out.println(employeeObject.get("message_reference"));
    }
}
