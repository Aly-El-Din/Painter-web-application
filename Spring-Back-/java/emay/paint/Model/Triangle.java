package emay.paint.Model;

public class Triangle extends Shape {
    private double radius;
    public Triangle(){}
    public Triangle(double radius){
        this.radius = radius;
    }
    public Triangle(ShapeRequest shapeRequest) {
        super(shapeRequest);
        if(shapeRequest.getRadius()==0){
            this.radius = 100;
        }
        else{
            this.radius=shapeRequest.getRadius();
        }
    }
    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
}