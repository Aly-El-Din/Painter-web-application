package emay.paint.Model;

public class ShapeRequest {
    private String type, id, fill, stroke;
    private double x, y, radius, radiusX, radiusY;
    private double height, width;
    private double[] points;

    public ShapeRequest(){}

    //Setters
    public void setType(String type){this.type = type;}
    public void setId(String id) {this.id = id;}
    public void setFill(String fill){this.fill=fill;}
    public void setStroke(String stroke){this.stroke=stroke;}
    public void setX(double x){this.x = x;}
    public void setY(double y){this.y = y;}
    public void setRadius(double radius){this.radius=radius;}
    public void setRadiusX(double radiusX){this.radiusX=radiusX;}
    public void setRadiusY(double radiusY){this.radiusY=radiusY;}
    public void setHeight(double height){this.height=height;}
    public void setWidth(double width){this.width=width;}
    public void setPoints(double[] points) {this.points = points;}
    
    //Getters
    public String getType(){return type;}
    public String getId(){return id;}
    public String getFill(){return fill;}
    public String getStroke(){return stroke;}
    public double getX(){return x;}
    public double getY(){return y;}
    public double getRadius(){return radius;}
    public double getRadiusX(){return radiusX;}
    public double getRadiusY(){return radiusY;}
    public double getHeight(){return height;}
    public double getWidth(){return width;}
    public double[] getPoints(){return points;}
}