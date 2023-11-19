
import java.util.ArrayList;
import java.util.List;

public class Environment {
    private final List<Robot> robots;
    private final List<Circle> circleList;

    private Circle outside;

    private RobotSimulation panel;

    public Environment(RobotSimulation panel) {
        this.panel = panel;
        robots = new ArrayList<>();
        circleList = new ArrayList<>();
        initRobots();
        initCircles();
        double maxCircleRadius = getMaxCircleRadius();
        Circle outside = null;
        for (Circle circle : circleList) {
            if (circle.isInScope(maxCircleRadius)) {
                if(outside==null || outside.getCircleRadius() < circle.getCircleRadius()){
                    outside = circle;
                }
            }
        }
        this.outside = outside;
        for(Robot robot : robots){
            robot.setMaxcircle(outside);
        }
    }


    private void initRobots() {
        for (int i = 0; i < 100; i++) {
            // 创建机器人并随机分配到两个组
            String group = i % 2 == 0 ? "gathering" : "circle";
            double x = Math.random() * 400;
            double y = Math.random() * 400;
            Robot robot = new Robot(x, y, i,false,1.0, group, this);
            robots.add(robot);
        }
    }
    public boolean hasCircle(double len){
        for (Circle circle : circleList) {
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
                circleList.add(circle);
                for(Robot robot2: robots){
                    //落在圆上的
                    if(circle.isInScope(robot2.distanceToOrigin())){
                        circle.addRobot(robot2);
                        robot2.setCircle(circle);
                    }
                }
            }
        }
        setToPivot(circleList);
    }

    public void update(int width, int height) {
        // 更新所有机器人的状态
        for (Robot robot : robots) {
            if(robot.getGroup().equals("circle")&& outside.isInScope(robot.distanceToOrigin())){
                robot.setActive(false);
                robot.setFinish(true);
                continue;
            }
            robot.move(width, height);
        }
    }



    public List<Robot> getRobots() {
        return robots;
    }

    public List<Circle> getCircleList() {
        return circleList;
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


    // Calculate the closest pivot robots
    public void findClosestPivot(double valueY,List<Robot> robotList){
        double leftMin = Integer.MAX_VALUE;
        double rightMin = Integer.MAX_VALUE;
        Robot robotP = null;
        Robot robotP2 = null;
        for(Robot robot : robotList){
            double distance=Math.sqrt(Math.pow(robot.getX(),2) + Math.pow((robot.getY() - valueY) ,2));
            //right side
            if(robot.getX()> (double) panel.getWidth()/2){
                if(distance < rightMin){
                    rightMin = distance;
                    robotP = robot;
                }
            }
            //left side
            if(robot.getX()<(double) panel.getWidth()/2) {
                if (distance < leftMin) {
                    leftMin = distance;
                    robotP2 = robot;
                }

            }
        }
            if(robotP != null) {
                robotP.setPivot(true);
            }
            if(robotP2 != null) {
                robotP2.setPivot(true);
            }
    }

    // Set pivot robots
    public void setToPivot(List <Circle> circles){
        Robot p1 =null;
        Robot p2 = null;
        for(int i =0 ; i< circles.size();i++){
            Circle circle = circles.get(i);
            int robotNumber= circle.getRobotCount();
            List <Robot> robotList = circle.getRobots();
            if (robotNumber<=4){
                for(Robot robot : robotList){
                    robot.setPivot(true);
                }
            }else{
                for(Robot robot : robotList){
                    //Find p1, p2
                    if(robot.getX() == 0){
                        if(robot.getY() < (double) panel.getHeight() /2){
                            p2 = robot;
                        }else {
                            p1 = robot;
                        }
                    }
                }
                //condition 1
                if((p2 == null&&p1!=null)||(p2!=null&&p1==null)) {
                    double y_axis;
                    if (p1 == null) {
                        p2.setPivot(true);
                        y_axis = (double) panel.getHeight() / 2 + circle.getCircleRadius();
                    } else {
                        p1.setPivot(true);
                        y_axis = (double) panel.getHeight() / 2 - circle.getCircleRadius();
                    }
                    findClosestPivot(y_axis, robotList);
                }
                //condition 2:
                else if (p1 != null) {
                    p1.setPivot(true);
                    p2.setPivot(true);
                //Condition 3
                }else{
                    double highestY = (double) panel.getHeight() / 2 + circle.getCircleRadius();
                    double lowestY = (double) panel.getHeight() / 2 - circle.getCircleRadius();
                    findClosestPivot(highestY, robotList);
                    findClosestPivot(lowestY, robotList);
                }
            }
            }
        }

    //Implement reduction phase
    public void reductionPhase(boolean multiplicityPoint, List<Circle> circlesList) {
        if (multiplicityPoint) {
            if (circlesList.size() == 1) {
                Circle outermost = circlesList.get(0);
                List<Robot> robotList = outermost.getRobots(); //判断是否在最外层圆上
                if (outermost.getRobotCount() > 4) {
                    for (Robot robot : robotList) {
                        if (robot.getPivot() == false) {
                            robot.move(800, 600);
                            //不在圈上
                            robotList.remove(robot);
                        }
                    }
                }
            } else {
                int lastID = circlesList.size() - 1;
                Circle outermost = circlesList.get(lastID);
                Circle inner = circlesList.get(lastID - 1);
                List<Robot> robotList = outermost.getRobots();
                if (outermost.getRobotCount() > 4) {
                    for (Robot robot : robotList) {
                        if (!robot.getPivot()) {
                            //move 1/2 distance
                            double distance = outermost.getCircleRadius() - inner.getCircleRadius();
                            double slope = robot.getY() / robot.getX();
                            double targetX = 0.5 * Math.sqrt(Math.pow(distance, 2) / (1 + Math.pow(slope, 2)));
                            double targetY = 0.5 * (-slope * targetX);

                            double x = robot.getX();
                            double y = robot.getY();

                            if (x < (double) panel.getWidth() / 2) {
                                x += targetX;
                                if (y > (double) panel.getHeight() / 2) {
                                    y -= targetY;
                                } else {
                                    y += targetY;
                                }
                            } else if (x > (double) panel.getWidth() / 2) {
                                x -= targetX;
                                if (y > (double) panel.getHeight() / 2) {
                                    y -= targetY;
                                } else {
                                    y += targetY;
                                }
                            }
                            robot.setX(x);
                            robot.setY(y);
                            robotList.remove(robot);
                        }
                        //没有写1/2 distance
                    }
                }

            }
        }

    }

}
