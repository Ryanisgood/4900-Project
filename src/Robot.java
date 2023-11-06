import java.awt.*;
import java.awt.geom.Point2D;

public class Robot {
    private boolean group;
    private int codeNum;//编号
    private double speed;

    private final Point2D.Double location;
    private boolean pivot;
    private boolean detectedMoreThanOne;
    public Robot(boolean group,int codeNum,double x, double y, boolean p, boolean detected){
        //boolean true => gathering; false=>formation
        this.group=group;
        location = new Point2D.Double(x, y);
        this.codeNum=codeNum;
        this.pivot=p;
        this.detectedMoreThanOne=detected;
    }

    @Override
    public String toString() {
        if(group) {
            return ("Gathering " + codeNum + " " + location.getX() + " " + location.getY() + " " + pivot + " " + detectedMoreThanOne);
        }else return ("Circle" + " " + codeNum + " " + location.getX() + " " + location.getY() + " " + pivot + " " + detectedMoreThanOne);
    }
    //Get coordinate of the x-axis
    public double getX(){
        return location.getX();
    }

    //Get coordinate of the y-axis
    public double getY(){
        return location.getY();
    }

    public boolean isPivot() {return pivot;}

    public boolean getGroup() {return group;}

    public Point2D getLocation() { return location;};
    public void set_axis(double x,double y){
        location.setLocation(x, y);
    }
    public double getDistanceOrigin(){ return location.distance(0,0);}
    public double getDistance(double x, double y){ return location.distance(location.getX(), location.getY(), x, y);}

    public void setPivot(boolean flag){
        this.pivot=flag;
    }

    public void setGroup(boolean group) {this.group = group;}
    public void setMoreThanOne(boolean flag){
        this.detectedMoreThanOne=flag;
    }

}
