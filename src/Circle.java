import java.util.ArrayList;

public class Circle {
    /*private double x;
    private double y;*/
    private double radius;
    private ArrayList<Robot> robotsOnCircle;
    public Circle(double x,double y, double radius){
      /*  this.x=x;
        this.y=y;*/
        this.radius=radius;

    }

   /* public double getX(){
        return x;
    }

    public double getY() {
        return y;
    }
*/
    public double getRadius() {
        return radius;
    }

    // add robots into the circle
    public void addRobotsToCircle(Robot robot){
        robotsOnCircle.add(robot);

    }
    //return robots on the circle
    public ArrayList<Robot> showRobots(){
        return this.robotsOnCircle ;
    }

    //return robot size on the Circle
    public int robotsSizeOnCircle(){
        return robotsOnCircle.size();
    }

}
