package emay.paint.Controller;

import emay.paint.Service.*;
import emay.paint.Model.*;

import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/paint")
@CrossOrigin(origins = "http://localhost:3000")
public class ShapeController {

    private ShapeFactory shapeFactory;
    private ShapeService shapeService;
    
    public ShapeController(ShapeFactory shapeFactory, ShapeService shapeService){
        this.shapeFactory = shapeFactory;
        this.shapeService = shapeService;
    }

    @PostMapping("/create")
    public Shape addShape(@RequestBody ShapeRequest shapeRequest){
        Shape shape = shapeFactory.getShape(shapeRequest);
        return shapeService.addShape(shape);
    }

    @DeleteMapping("/delete/{id}")
    public ArrayList<Shape> deleteShape(@PathVariable String id){
        return shapeService.deleteShape(id);
    }

    @DeleteMapping("/clear")
    public ArrayList<Shape>clear(){return shapeService.clearAll();}


    @GetMapping("/copy/{id}")
    public Shape copyShape(@PathVariable String id){
        return shapeService.getClone(id);
    }

    @PostMapping("/modify")
    public ArrayList<Shape> modifyShape(@RequestBody ShapeRequest shapeRequest){
        Shape modifiedShape = shapeFactory.getShape(shapeRequest);
        return shapeService.modifyShape(modifiedShape);
    }

    @GetMapping("/save")
    public void save(@RequestParam String filePath,@RequestParam String fileType) throws IOException {
        shapeService.SaveShapes(filePath,fileType);
    }

    @GetMapping("/load")
    public ArrayList<Shape> load(@RequestParam String filePath,@RequestParam String fileType) throws IOException {
        return shapeService.loadShapes(filePath,fileType);
    }

    @GetMapping("/undo")
    public ArrayList<Shape> undo(){
        return shapeService.undo();
    }

    @GetMapping("/redo")
    public ArrayList<Shape> redo(){
        return shapeService.redo();
    }
    
    @GetMapping("/refresh")
    public ArrayList<Shape>refresh() {
        return shapeService.getShapes();
    }
}