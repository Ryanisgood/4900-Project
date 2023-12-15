

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Robot implements Runnable {
    
    private double x, y; // Position coordinates
    private final double speed; // Speed
    private double angle; // Moving angle
    private final String group; // Group: "gathering" or "circle"
    private boolean active; // Whether active
    private boolean isObstacle; // Whether encountering obstacle


    private final Environment environment;
    private Circle circle; // Current circle
    List<Circle>circles;
    List<Robot> robots;

    RobotSimulation.SimulationPanel panel;
    private Circle Maxcircle;
    private Circle nextCircle;
    private Circle innerSide;
    private static final int DOT_SIZE = 5; // Dot size representing the robot
    private boolean pivot;
    private int robotID;

    double targetX;
    double targetY;
    private Robot collisionRobot; // Collision, need to change angle
    private Robot adjacentRobot; // Adjacent with collisionRobot
    private boolean needCompute;
    private  double centerX;
    private  double centerY;

    private List<Robot> robotsOnCircle;
    public Robot(double x, double y, int robotID,boolean pivot, double speed, String group, Environment environment) {
        // Set initial position near the center of the window
        this.x = 400 + x - 200; // Assuming window width is 800
        this.y = 300 + y - 200; // Assuming window height is 600
        this.robotID =robotID;
        this.pivot = pivot;
        this.speed = speed;
        this.group = group;
        this.environment = environment;
        this.active = false;
        this.circle = null;
        this.isObstacle = false;
        robotsOnCircle = new ArrayList<>();
        needCompute = true;
    }


    public void look(){
        // Observe all robots
        robots = environment.getRobots();
        // Record data
    }


    public void compute(){
        this.panel = RobotSimulation.panel;
        centerX= panel.getWidth()/2;
        centerY= panel.getHeight()/2;
        circles=environment.getCircleList();
        innerSide=circles.get(0);
        if(circles.size()>1) {
            nextCircle = circles.get(1);
        }

        if (circle == null) return;
        if (!active) return; // If the robot is inactive, do not move
        // Formation Phrase
        // The innermost circle
        if(!circle.equals(innerSide)){return;}

        // Calculate the target point
        if ("gathering".equals(group)) {
            targetX = panel.getWidth() / 2.0;
            targetY = panel.getHeight() / 2.0;
        } else {// "circle" group
            double slope = (this.y - centerY) /(this.x- centerX);
            double radDiff =nextCircle.getCircleRadius()-circle.getCircleRadius();
            targetX = x + radDiff / Math.sqrt(1 + Math.pow(slope, 2));
            targetY = y + (radDiff * slope) / Math.sqrt(1 + Math.pow(slope, 2));
           /* double upperSlope =(y+5 - panel.getHeight() / 2) / (x-panel.getWidth() / 2);
            double lowerSlope =(y-5 - panel.getHeight() / 2) / (x-panel.getWidth() / 2);
            //if ((Math.abs(robotC2Slope)<=Math.abs(upperSlope))&&(Math.abs(robotC2Slope)>=Math.abs(lowerSlope))){*/
            //Detect Collision
            List<Robot> c2Robots =nextCircle.getRobots();
            for (Robot robotOnC2 : c2Robots) {
                double targetRange=Math.sqrt(Math.pow(robotOnC2.x-targetX,2)+Math.pow(robotOnC2.y-targetY,2));
                if (Math.abs(targetRange)<6) {
                    this.isObstacle = true;
                    break;
                }



            }

            // if collision, find one of the two adjective angles
            if(isObstacle) {
                if(needCompute) {
                    System.out.println("Detect collision "+ this);
                    //Detect Collision
                    double nextRadius = nextCircle.getCircleRadius();
                    // compute the difference of all robots with collisionRobot in the circle
                    // and find the min diff,so they are adjective
                    double minDiff = Integer.MAX_VALUE;
                    double oneThirdAngle = 0;
                    if (robotsOnCircle.size() > 1) {
                        for (Robot robot : robotsOnCircle) {
                            if (!robot.equals(this)) {
                                adjacentRobot = robot;
                                double difference = Math.sqrt(Math.pow(Math.abs(robot.x - x), 2)
                                        + Math.pow(Math.abs(robot.y - y), 2));
                                if (difference < minDiff) {
                                    minDiff = difference;
                                    adjacentRobot.x = robot.x;
                                    adjacentRobot.y = robot.y;
                                }
                            }
                        }
                        // Calculate the angle between adjacent robot(A) and  collision robot(C) with respect to the center

                        double angleAC = Math.atan2(y - centerY, x - centerX)
                                - Math.atan2(adjacentRobot.y - centerY, adjacentRobot.x - centerX);
                        System.out.println("The positional data of adjacent Robot: "+adjacentRobot);
                        // ensure angle is positive
                        if (angleAC < 0) {
                            angleAC += 2 * Math.PI;
                        }
                        // 1/3 of the angle
                        oneThirdAngle = angleAC / 3.0;
                    } else {
                        oneThirdAngle = Math.toRadians(30);
                    }
                    // Calculate the new coordinate for collision robot at 1/3 of the angle in next circle.
                    targetX = nextRadius * Math.cos(oneThirdAngle);
                    targetY = nextRadius * Math.sin(oneThirdAngle);
                    System.out.println("Target on next circle:( "+targetX + "，  "+ targetY +" )");
                    System.out.println("Radius "+ nextRadius);
                    needCompute = false;
                    isObstacle=false;
                }
            }else{
                targetX= x + radDiff/Math.sqrt(1+Math.pow(slope,2));
                targetY = y + radDiff*slope/Math.sqrt(1+Math.pow(slope,2));
            }
            angle =Math.atan2(targetY - panel.getHeight()/2, targetX - panel.getWidth()/2);
        }
        
        // Calculate the distance to the target point
        double distanceToTarget = Math.hypot(targetX - x, targetY - y);
        // For the blue robot (Circle group), set the target point to the center of the window
        if ("gathering".equals(group)) {
            angle = Math.atan2(targetY-y , targetX-x );
            if (distanceToTarget < speed) {
                //If the distance is less than the step, move directly to the target point and stop
                x = targetX;
                y = targetY;
            }
        } else {
            angle = Math.atan2(y - panel.getHeight()/2, x - panel.getWidth()/2);
            if (distanceToTarget < speed) {
                x = targetX;
                y = targetY;
            }
        }
    
    }

    @Override
    public void run() {
        new Timer(10, e -> {
            //If the robot is not active, do not move
            look();
            compute();
            move();

        }).start();
    }

    public void move() {
        if(circle == null) return;
        if(!circle.equals(innerSide)){return;}
        if (!active) return; // If the robot is inactive, do not move
        // 
        robots = circle.getRobots();
        if(group.equals("gathering") && this.distanceToOrigin() > 30){
            x += 3* speed * Math.cos(angle);
            y += 3* speed * Math.sin(angle);
        }else {
            x += speed * Math.cos(angle);
            y += speed * Math.sin(angle);
        }
        // If the robot is out of the circle, remove it
        if(!circle.isInScope(this.distanceToOrigin())){
            robots.remove(this);
        }
    }

    public void draw(Graphics g) {
        if ("gathering".equals(group)) {
            g.setColor(Color.BLUE);
        } else if (isObstacle) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.RED);
        }
        g.fillOval((int) x - (DOT_SIZE / 2), (int) y - (DOT_SIZE / 2), DOT_SIZE, DOT_SIZE); // 画一个小圆代表机器人
    }

    // 计算到原点的距离
    public double distanceToOrigin() {
        double centerX = 393;
        double centerY = 281;
        return Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
    }

    public void setCircle(Circle circle){
        this.circle = circle;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setActive(boolean active) {
        if(!active){
            this.active = active;
            this.isObstacle = false;
            needCompute = true;
        }else {
            this.active = active;
        }

    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public String getGroup() {
        return group;
    }

    public boolean isActive() {
        return active;
    }

    public void setMaxcircle(Circle circle){
        Maxcircle = circle;
    }
    public void setInnerSide(Circle circle) {innerSide = circle;}

    public void setPivot(boolean flag) {
        this.pivot=flag;
    }
    public boolean getPivot(){
        return this.pivot;
    }

    public int getRobotID(){
        return this.robotID;
    }
    public void setRobotsOnCircle(List<Robot> robotsOnCircle) {
        for(Robot robot: robotsOnCircle){
            Robot robot1 = robot.deepCopy();
            this.robotsOnCircle.add(robot1);
        }
    }

    public Robot deepCopy() {
        return new Robot(this.x, this.y, this.robotID, this.pivot, this.speed, this.group , this.environment);
    }

    @Override
    public String toString() {
        return "Robot{" +
                "x=" + x +
                ", y=" + y +
                ", group='" + group + '\'' +
                ", active=" + active +
                ", robotID=" + robotID +
                '}';
    }
}
