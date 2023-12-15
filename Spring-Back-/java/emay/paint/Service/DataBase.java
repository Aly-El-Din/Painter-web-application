package emay.paint.Service;

import emay.paint.Model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataBase {

    private static DataBase dataBase;
    private String filePath;
    private ObjectMapper json;
    private XmlMapper xml;

    private DataBase(){
        xml = new XmlMapper();
        json = new ObjectMapper();
    }

    public static DataBase getInstance() {
        if(dataBase == null)
            dataBase = new DataBase();
        return dataBase;
    }

    public void setFilePath(String filePath) {
        filePath = filePath.replaceAll("\\\\","\\\\\\\\");
        filePath = filePath.replaceAll("\"","");
        this.filePath = filePath;
    }

    public void JsonSave(ArrayList<Shape> shapes) throws IOException {
        json.writeValue(new File(filePath), shapes);
    }
    public void xmlSave(ArrayList<Shape> shapes) throws IOException {
        xml.writeValue(new File(filePath), shapes);
    }

    private ArrayList<ShapeRequest> loadXml() throws IOException{
        return xml.readValue(new File(filePath), new TypeReference<>() {});
    }
    private ArrayList<ShapeRequest> loadJson() throws IOException{
        return json.readValue(new File(filePath), new TypeReference<>() {});
    }

    public void SaveDataBase(ArrayList<Shape> shapes,String type) throws IOException {
        switch(type){
            case "json" -> JsonSave(shapes);
            case "xml" -> xmlSave(shapes);
        }
    }
    public ArrayList<ShapeRequest> loadDataBase(String type) throws IOException {
        return switch(type){
            case "json" -> loadJson();
            case "xml" -> loadXml();
            default -> null;
        };
    }
}