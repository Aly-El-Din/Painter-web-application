package emay.paint.Service;

import emay.paint.Model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

@Service
public class ShapeService {
    private static ArrayList<Shape> shapes;                         //The main list which stores all created shapes
    private static Stack<ArrayList<Shape>>shapesContainer;          //Stack which stores all actions done
    private static Stack<ArrayList<Shape>>undoneShapesContainer;    //Stack which stores undo actions
    private int number_of_undo=0;
    //Number of undo operations done to ensure that no redo operation could be
    //done without undo operation, and no further undo operations could be done
    //after doing a different action after an undo operation
    private ShapeFactory shapeFactory;
    public ShapeService(){
        shapes = new ArrayList<>();
        shapesContainer=new Stack<>();
        undoneShapesContainer=new Stack<>();
        shapeFactory=new ShapeFactory();
    }
    //Creating shape service
    //Function which added a created shape
    public Shape addShape(Shape shape){
        ArrayList<Shape> currentShapes = new ArrayList<>(shapes);
        currentShapes.add(shape);
        number_of_undo-=undoneShapesContainer.size();
        undoneShapesContainer.clear();
        shapesContainer.push(new ArrayList<>(currentShapes));
        shapes=currentShapes;
        return shape;
    }
    //Delete service
    //Deleting an existed shape else return null
    public ArrayList<Shape> deleteShape(String id){
        for(int i=0;i<shapes.size();i++){
            if(shapes.get(i).getId().equals(id)){
                ArrayList<Shape> currentShapes = new ArrayList<>(shapes);              //Create a new list of current shapes
                currentShapes.remove(i);                                               //Remove the targeted shape
                shapesContainer.push(new ArrayList<>(currentShapes));                  //Adding the list of shapes to the stack
                shapes=currentShapes;                                                  //Making the shapes as the current shapes after removing the targeted shape
                number_of_undo-=undoneShapesContainer.size();
                undoneShapesContainer.clear();
                return shapes;
            }
        }
        return null;
    }
    //Getting the created shapes service
    //Returning the shapes as they are
    public  ArrayList<Shape> getShapes() {
        return shapes;
    }
    //Cloning Service
    //Copying an existed shape else return null
    public Shape getClone(String id){
        Shape clonedShape;
        for(Shape shape:shapes){
            if(shape.getId().equals(id)){
                clonedShape=(Shape)shape.Clone();                                   //Cloning the requested shape
                clonedShape.setId(Long.toString(System.currentTimeMillis()));       //Setting a new ID to the cloned shape
                clonedShape.setX(shape.getX()+15);                                  //Setting new shifted coordinates to the cloned shape
                clonedShape.setY(shape.getY()+15);                                  //...
                addShape(clonedShape);                                             //Adding the cloned shape to the list of shapes
                number_of_undo-=undoneShapesContainer.size();
                undoneShapesContainer.clear();
                return (Shape) clonedShape.Clone();                                 //Returning a copy of the cloned shape
            }
        }
        return null;
    }
    //Modifying an existing shape service
    //Modifying a shape with the new attributes by substituting its place in the list with the new list
    public ArrayList<Shape> modifyShape(Shape modifiedShape){
        for(int i=0;i<shapes.size();i++){
            if(shapes.get(i).getId().equals(modifiedShape.getId())){
                ArrayList<Shape> currentShapes = new ArrayList<>(shapes);
                currentShapes.set(i,modifiedShape);
                shapesContainer.push(new ArrayList<>(currentShapes));
                shapes=currentShapes;
                number_of_undo-=undoneShapesContainer.size();
                undoneShapesContainer.clear();
                return shapes;
            }
        }
        return null;
    }
    //Load service
    public ArrayList<Shape> loadShapes(String filePath, String type) throws IOException {
        // Obtain a singleton instance of the database
        DataBase dataBase = DataBase.getInstance();
        // Set the file path for the database
        dataBase.setFilePath(filePath);
        // Clear the existing shapes ArrayList
        shapes.clear();
        // Load shapes from the database based on the specified type
        for (ShapeRequest shapeRequest : dataBase.loadDataBase(type)) {
            // Create a Shape object using the ShapeFactory
            Shape shape = shapeFactory.getShape(shapeRequest);

            // Add the created shape to the shapes ArrayList
            addShape(shape);
        }
        // Clear the shapesContainer and undoneShapesContainer stacks to prevent user from getting back to created (but not saved) design
        while (!shapesContainer.empty()) {
            shapesContainer.pop();
        }
        while (!undoneShapesContainer.empty()) {
            undoneShapesContainer.pop();
        }
        // Reset the number of undo operations
        number_of_undo = 0;
        // Push the current state of shapes to the shapesContainer stack
        shapesContainer.push(shapes);
        // Return the loaded shapes
        return shapes;
    }

    //Save service
    public  void SaveShapes (String filePath,String type) throws IOException {
        DataBase dataBase=DataBase.getInstance();
        dataBase.setFilePath(filePath);
        dataBase.SaveDataBase(shapes, type);
    }

    //Undo service
    public ArrayList<Shape> undo(){
        if(!shapesContainer.empty()){
            undoneShapesContainer.push(shapesContainer.peek());
            shapesContainer.pop();
            number_of_undo++;
            if(!shapesContainer.empty()){
                ArrayList<Shape>currentShapes=shapesContainer.peek();
                shapes=currentShapes;
            }
            else{
                shapes=new ArrayList<Shape>();
            }
            return shapes;
        }
        return new ArrayList<Shape>();
    }

    //Redo service
    public ArrayList<Shape> redo(){
        if(!undoneShapesContainer.empty() && number_of_undo!=0){
            ArrayList<Shape> currentShapes=undoneShapesContainer.peek();
            undoneShapesContainer.pop();
            number_of_undo--;
            shapesContainer.push(currentShapes);
            shapes=currentShapes;
            return shapes;
        }
        return null;
    }
    //Clear service
    public ArrayList<Shape>clearAll(){
        ArrayList<Shape> currentShapes = new ArrayList<>();
        shapesContainer.push(new ArrayList<>(currentShapes));
        shapes=currentShapes;
        number_of_undo-=undoneShapesContainer.size();
        undoneShapesContainer.clear();
        return shapes;
    }
}