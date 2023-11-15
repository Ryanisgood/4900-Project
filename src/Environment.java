
import java.util.ArrayList;
import java.util.List;

public class Environment {
    private final List<Robot> robots;
    private final List<Circle> circles;

    private final Circle outside;

    private RobotSimulation.SimulationPanel panel;

    public Environment() {
        robots = new ArrayList<>();
        circles = new ArrayList<>();
        initRobots();
        initCircles();
        double maxCircleRadius = getMaxCircleRadius();
        Circle outside = null;
        for (Circle circle : circles) {
            if (circle.isInScope(maxCircleRadius)) {
                if(outside==null || outside.getCircleRadius() < circle.getCircleRadius()){
                    outside = circle;
                }
            }
        }
        this.outside = outside;
    }

    private void initRobots() {
        for (int i = 0; i < 100; i++) {
            // 创建机器人并随机分配到两个组
            String group = i % 2 == 0 ? "gathering" : "circle";
            double x = Math.random() * 400;
            double y = Math.random() * 400;
            Robot robot = new Robot(x, y, 1.0, group, this);
            robots.add(robot);
        }
    }
    public boolean hasCircle(double len){
        for (Circle circle : circles) {
            if(circle.isInScope(len)){
                return true;
            }
        }
        return false;
    }
    public boolean hasRobot(Circle circle){
        for (Robot robot : robots) {
            if(circle.isInScope(robot.distanceToOrigin())){
                return true;
            }
        }
        return false;
    }
    private void initCircles() {
        //获取所有的要绘制圆圈的半径
        //根据半径生成圆圈
        for (Robot robot : robots) {
            double distance = robot.distanceToOrigin();
            if(!hasCircle(distance)){
                Circle circle = new Circle(distance);
                circles.add(circle);
                for(Robot robot2: robots){
                    if(circle.isInScope(robot2.distanceToOrigin())){
                        circle.addRobot(robot2);
                        robot2.setCircle(circle);
                    }
                }
            }
        }
    }

    public void update(int width, int height) {
        // 更新所有机器人的状态
        for (Robot robot : robots) {
            if(robot.getGroup().equals("circle")&& outside.isInScope(robot.distanceToOrigin())){
                robot.setActive(false);
                continue;
            }
            robot.move(width, height);
        }
    }


    public List<Robot> getRobots() {
        return robots;
    }

    public List<Circle> getCircles() {
        return circles;
    }

    public double getMaxCircleRadius() {
        double maxRadius = 0;
        for (Robot robot : robots) {
            if ("circle".equals(robot.getGroup())) {
                double distance = robot.distanceToOrigin();
                maxRadius = Math.max(maxRadius, distance);
            }
        }
        return maxRadius;
    }

    public void setPanel(RobotSimulation.SimulationPanel panel) {
        this.panel = panel;
    }
}
