import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

public class Robot {
    private boolean group;
    private int codeNum;//编号
    private double speed;
    private ArrayList<Point2D> maps;

    private final Point2D.Double location;
    private boolean pivot;
    private boolean detectedCollision;

    private boolean activated = false;

    private Robot[] robotsArray =new Robot[20];
    private int size =0;

    public Robot(boolean group,int codeNum,double x, double y, boolean p, boolean detected){
        //boolean true => gathering; false=>formation
        this.group=group;
        location = new Point2D.Double(x, y);
        this.codeNum=codeNum;
        this.pivot=p;
        this.detectedCollision=detected;
    }

    //receive a  list of all the other robots;
    public void look(ArrayList<Robot> robots) {
        if(activated) {
            for (Robot r : robots) {
                maps.add(r.getLocation());
            }
        }
    }

    public void compute() { //return destination and angle of move
        if(activated) {
            if (!detectedCollision) {
                double slopeToOrgin = calSlope(0, 0);

            }
        }
    }//

    //move the robots;
    public void move(double x, double y){
        if(activated) {
            location.setLocation(x, y);
        }
    }

    //get the code number of the robot
    public int getCodeNum() {
        return codeNum;
    }

    @Override
    public String toString() {
        if(group) {
            return ("Gathering " + codeNum + " " + location.getX() + " " + location.getY() + " " + pivot + " " + "Collision: "+detectedCollision);
        }else return ("Circle" + " " + codeNum + " " + location.getX() + " " + location.getY() + " " + pivot + " " + "Collision: "+detectedCollision);
    }
    //Get coordinate of the x-axis
    public double getX(){
        return location.getX();
    }

    //Get coordinate of the y-axis
    public double getY(){
        return location.getY();
    }

    public boolean getGroup() {return group;}

    public boolean getPivot(){return pivot;}

    public Point2D getLocation() { return location;};
    public double getDistanceOrigin(){ return location.distance(0,0);}
    public double getDistance(double x, double y){ return location.distance(location.getX(), location.getY(), x, y);}
    public double calSlope(double x, double y){
        if(location.getX() == x) {
            return Double.POSITIVE_INFINITY;
        }else{
            return (location.getY() - y) / (location.getX() - x);
        }
    }
    public void setPivot(boolean flag){
        this.pivot=flag;
    }

    public void setGroup(boolean group) {this.group = group;}
    public void setDetectedCollision(boolean flag){
        this.detectedCollision=flag;
    }

    public void activate(boolean flag){ activated = flag;}
    public boolean getStatus() {return activated;}





}


