package emay.paint.Model;

public class Circle extends Shape{
    private double radius;
    public Circle(){}
    public Circle(ShapeRequest shapeRequest) {
        super(shapeRequest);
        if(shapeRequest.getRadius()==0){
            this.radius = 50;
        }
        else{
            this.radius=shapeRequest.getRadius();
        }
    }
    public void setRadius(double radius){
        this.radius=radius;
    }
    public double getRadius(){
        return radius;
    }
}