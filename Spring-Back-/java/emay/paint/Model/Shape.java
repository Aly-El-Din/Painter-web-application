package emay.paint.Model;

 public abstract class Shape implements Cloneable{
    
    //Shape attributes
    private String id;
    private double x, y;
    private String fill;
    private String type, stroke;
    
    //Default constructor
    public Shape(){}
    
    //Customized constructor
    public Shape(ShapeRequest shapeRequest){
        this.x = shapeRequest.getX();
        this.y = shapeRequest.getY();
        this.type = shapeRequest.getType();
        this.id = shapeRequest.getId();
        this.fill = shapeRequest.getFill();
        this.stroke = "black";
    }
    
    //Copying a shape
    public Object Clone(){
        try{
            return super.clone();
        }
        catch (CloneNotSupportedException e){
            throw new InternalError();
        }
    
    }

    //Setters
    public void setType(String type) {
        this.type = type;
    }
    public void setfill(String fill){
        this.fill = fill;
    }
    public void setId(String id){
        this.id=id;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    //Getters
    public String getType() {
        return type;
    }
    public String getfill(){
        return fill;
    }
    public String getId(){
        return id;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public String getStroke() {
        return stroke;
    }
}