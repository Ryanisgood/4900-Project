import java.util.List;
import java.util.ArrayList;
import java.awt.*;

public class Circle {
    private double circleRadius; // Radius
    private boolean active; // Whether it is active
    private static final int ROBOT_SIZE = 5; // Represents the size of the robot dot
    private List<Robot> robots; // Robots in the environment
    long circleTime;
    public Circle(double circleRadius) {
        this.circleRadius = circleRadius; 
        this.active = true;

        robots = new ArrayList<>();
    }

    public void draw(int x, int y,Graphics g) {
        if (!active) { return;}
        g.setColor(Color.BLUE);
        g.drawOval((int) (x - circleRadius), (int) (y-circleRadius),
                (int) (2 * circleRadius), (int) (2 * circleRadius));
    }
    // 在圆圈上
    public boolean isInScope(double len) {
        return circleRadius - (ROBOT_SIZE /2) < len && circleRadius + (ROBOT_SIZE /2) > len;
    }
    
    public void addRobot(Robot robot){
        robots.add(robot);
    }


    public void removeRobot(Robot robot){
        robots.remove(robot);
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public int getRobotCount(){
        return robots.size();
    }

    public void setActive(boolean active) {
        circleTime = System.currentTimeMillis();
        long duration = circleTime - RobotSimulation.getStartTime();
        long tmp = RobotSimulation.getTmp();
        long duration2 = (circleTime - tmp);
        if(tmp == 0){
            duration2 = 0;
        }
        //System.out.println("Time consuming since the last inner circle been deactivated: "+ duration2 + " millisecond");
        //System.out.println("Running time for deactivate inner most circle: "+ duration  + " millisecond");
        RobotSimulation.setTmp(circleTime);
        this.active = active;
    }

    // 省略getter和setter方法

    public boolean isActive() {
        return active;
    }

    public double getCircleRadius() {
        return circleRadius;
    }


}
