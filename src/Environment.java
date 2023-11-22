
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Environment {
    private final List<Robot> robots;
    private final List<Circle> circleList;

    private Circle outside;

    public Circle getInnerSide() {
        return innerSide;
    }

    public void setInnerSide(Circle innerSide) {
        this.innerSide = innerSide;
    }

    private Circle innerSide;
    private RobotSimulation panel;
    private Circle nextCircle;

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

        circleList.sort(new Comparator<Circle>() {
            @Override
            public int compare(Circle circle1, Circle circle2) {
                // Compare circles based on their radius
                double radius1 = circle1.getCircleRadius();
                double radius2 = circle2.getCircleRadius();
                // Ascending order (for descending order, reverse the comparison)
                if (radius1 < radius2) {
                    return -1;
                } else if (radius1 > radius2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        this.innerSide = circleList.get(0);
        this.nextCircle =circleList.get(1);
        System.out.println(innerSide.getCircleRadius()+"121321");
        for(Robot robot : robots){
            robot.setMaxcircle(outside);
            robot.setInnerSide(innerSide);
            if(robot.getCircle().equals(innerSide)) {
                robot.setActive(true);
            }
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
        Robot test = new Robot(211.4225530889187, 175.15595534821585, 999, false, 1.0 ,"gathering", this);
        robots.add(test);
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
                        if(!circle.getRobots().contains(robot2)) {
                            circle.addRobot(robot2);
                        }
                        if(robot2.getCircle()==null) {
                            robot2.setCircle(circle);
                        }
                    }
                }
            }
        }
        setToPivot(circleList);
    }

    public void update(int width, int height) {
        // 更新所有机器人的状态
        for (Robot robot : robots) {
            if (nextCircle.isInScope(robot.distanceToOrigin()) && robot.getGroup().equals("circle")) {
                if(robot.isActive()) {
                    System.out.println("circle false");
                    robot.setActive(false);
                }
                //如果离下一个圆太近，直接将下一个圆设定为最内圈圆
                if(nextCircle.isInScope((robot.distanceToOrigin())) && robot.getCircle() != null && robot.getCircle().equals(innerSide)){
                    if(checkActive()) {
                        robot.setCircle(nextCircle);
                        innerSide.setActive(false);
                        circleList.remove(0);
                        innerSide = circleList.get(0);
                        nextCircle = circleList.get(1);
                        for (Robot robot1 : robots) { //遍历机器人，如果他们全都完成了自己的任务，激活下一圈上的机器人
                            robot1.setInnerSide(circleList.get(0));
                            if (robot1.getCircle() != null && robot1.getCircle().equals(innerSide)) {
                                System.out.println("太近激活");
                                robot1.setActive(true);
                            }
                        }
                    }
                }
                robot.setCircle(nextCircle);//不在circle上了,下一个circle设置为当前机器人circle
                if(!nextCircle.getRobots().contains(robot)) {
                    nextCircle.addRobot(robot);
                }
                rebootRobot();
            }else
            if(robot.getGroup().equals("gathering") && robot.distanceToOrigin() < 3){
                if(robot.isActive()) {
                    robot.setActive(false);
                }
                if(nextCircle.isInScope((robot.distanceToOrigin())) && robot.getCircle() != null && robot.getCircle().equals(innerSide)){
                    if(checkActive()) {
                        innerSide.setActive(false);
                        circleList.remove(0);
                        innerSide = circleList.get(0);
                        nextCircle = circleList.get(1);
                        for (Robot robot1 : robots) { //遍历机器人，如果他们全都完成了自己的任务，激活下一圈上的机器人
                            robot1.setInnerSide(circleList.get(0));
                            if (robot1.getCircle() != null && robot1.getCircle().equals(innerSide)) {
                                robot1.setActive(true);
                            }
                        }
                    }
                }
                robot.setCircle(null);//不在circle上了
                rebootRobot();
            }
            if(robot.getCircle() != null && robot.getCircle().equals(innerSide)){ //检查机器人状态
                System.out.println(robot);
                System.out.println(innerSide.getRobots().size());
            }


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
                            robot.move();
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

    public void rebootRobot(){
        if(!hasRobot(innerSide) && checkActive()){
            innerSide.setActive(false);
            circleList.remove(0);
            innerSide = circleList.get(0);
            nextCircle = circleList.get(1);
            for(Robot robot1 : robots){ //遍历机器人，如果他们全都完成了自己的任务，激活下一圈上的机器人
                robot1.setInnerSide(circleList.get(0));
                if(robot1.getCircle() !=null && robot1.getCircle().equals(innerSide)){
                    System.out.println(robot1.getRobotID()+"   True1");
                    robot1.setActive(true);
                }

            }
        }
    }

    public boolean checkActive(){
        for(Robot robot:robots){
            if(robot.isActive()){
                return false;
            }
        }
        return true;
    }

}
