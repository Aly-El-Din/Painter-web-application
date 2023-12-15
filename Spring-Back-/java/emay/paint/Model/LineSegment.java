package emay.paint.Model;

public class LineSegment extends Shape{
    private double[] points;
    public LineSegment(){}
    public LineSegment(ShapeRequest shapeRequest){
        super(shapeRequest);
        if(shapeRequest.getPoints() == null){
            points = new double[]{0, 0, 200, 0};
        }
        else{
            this.points = shapeRequest.getPoints();
        }
    }
    public double[] getPoints(){
        return points;
    }
    public void setPoints(double[] points) {
        this.points = points;
    }
}