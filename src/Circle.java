import java.util.List;
import java.util.ArrayList;
import java.awt.*;

public class Circle {
    private double circleRadius; // 半径
    private boolean active; // 是否激活
    private static final int CIRCLE_SIZE = 5; //代表机器人的圆点尺寸
    private List<Robot> robots; //环境中的机器人

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
        return circleRadius - (CIRCLE_SIZE/2) < len && circleRadius + (CIRCLE_SIZE/2) > len;
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
