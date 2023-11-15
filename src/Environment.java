
import java.util.ArrayList;
import java.util.List;

public class Environment {
    private final List<Robot> robots;
    private final List<Circle> circles;

    private Circle outside;

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
                    //落在圆上的
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
                robot.setFinish(true);
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


    // Calculate the closest pivot robots
    public void findClosestPivot(double valueY,List<Robot> robotList){
        double leftMin = Integer.MAX_VALUE;
        double rightMin = Integer.MAX_VALUE;
        int leftCodeNum = 0;
        int rightCodeNum =0;
        for(Robot robot : robotList){
            double distance=Math.sqrt(Math.pow(robot.getX(),2) + Math.pow((valueY-robot.getY()) ,2));
            //right side
            if(robot.getX()>0){
                if(distance<rightMin){
                    rightMin = distance;
                    rightCodeNum = robot.getCodeNum();

                }
            }

            //left side
            if(robot.getX()<0) {
                if (distance < leftMin) {
                    leftMin = distance;
                    leftCodeNum = robot.getCodeNum();
                }

            }
        }

        for(Robot robot : robotList){
            if(robot.getCodeNum()==rightCodeNum){
                robot.setPivot(true);
            }else if(robot.getCodeNum()==rightCodeNum){
                robot.setPivot(true);
            }
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
                    if(robot.getX()==0 && robot.getY() ==  circle.getCircleRadius()){
                        p1=robot;
                    }else if(robot.getX()==0 && robot.getY() == - circle.getCircleRadius()) {
                        p2 = robot;
                    }
                }



                //Condition 1:
                if(p2 == null){
                    double y_axis=-circle.getCircleRadius();
                    p1.setPivot(true);
                    findClosestPivot(y_axis,robotList);

                }

                //Condition 2:
                else if(p1!= null && p2!=null){
                    p1.setPivot(true);
                    p2.setPivot(true);
                }

                //Condition 3
                else if (p1 == null && p2 ==null){
                    double highestY = circle.getCircleRadius();
                    double lowestY = circle.getCircleRadius();
                    findClosestPivot(highestY,robotList);
                    findClosestPivot(lowestY,robotList);

                }

            }

        }
    }

    //Implement reduction phase
    public void reductionPhase(boolean multiplicityPoint, List<Circle> circlesList){

        if (multiplicityPoint){
            if(circlesList.size()==1){
                Circle outermost = circlesList.get(0);
                List<Robot> robotList = outermost.getRobots();
                if(outermost.getRobotCount()>4){
                    for (Robot robot: robotList){
                        if(robot.getPivot()==false){
                            robot.move(0,0);
                        }
                    }

                }
            }else{
                int lastID =circlesList.size()-1;
                Circle outermost =circlesList.get(lastID);
                Circle inner = circlesList.get(lastID-1);
                List<Robot> robotList = outermost.getRobots();
                if(outermost.getRobotCount()>4){
                    for (Robot robot: robotList){
                        if(robot.getPivot()==false){

                            //没有写1/2 distance
                            robot.move(,0);
                        }
                    }

                }
            }

        }

    }
}
