

import java.awt.*;
import java.util.List;
import javax.swing.*;


public class Robot implements Runnable {
    private double x, y; // 位置坐标
    private final double speed; // 速度
    private double angle; // 移动角度
    private final String group; // 组别："gathering" 或 "circle"
    private boolean active; // 是否激活
    private boolean isObstacle; //是否遇到障碍物
    private final Environment environment;
    private Circle circle; //所在圆圈
    List<Circle>circles;
    List<Robot> robots;

    RobotSimulation.SimulationPanel panel;
    private Circle Maxcircle;

    private Circle innerSide;
    private static final int DOT_SIZE = 5; //代表机器人的圆点尺寸
    private boolean pivot;
    private int robotID;

    boolean finish = false;
    double targetX;
    double targetY;
    int start = 0;


    public Robot(double x, double y, int robotID,boolean pivot, double speed, String group, Environment environment) {
        // 将初始位置设置为窗口中心附近
        this.x = 400 + x - 200; // 假设窗口宽度为800
        this.y = 300 + y - 200; // 假设窗口高度为600
        this.robotID =robotID;
        this.pivot = pivot;
        this.speed = speed;
        this.group = group;
        this.environment = environment;
        this.active = false;
        this.circle = null;
        this.isObstacle = false;

    }


    public void look(){
        //观察所有机器人
        List<Robot> robots = environment.getRobots();
        //记录数据
    }


    public void compute(){
        panel = RobotSimulation.panel;
        circles=environment.getCircleList();
        innerSide=circles.get(0);

        if (circle == null) return;
        if (!active) return; // 如果机器人处于非激活状态，则不移动
        //Formation Phrase
        //the innermost circle

        if(!circle.equals(innerSide)){return;}
        System.out.println(innerSide.getCircleRadius());
        System.out.println(x+"  "+y);
        // 计算目标点
        if ("gathering".equals(group)) {
            targetX = panel.getWidth() / 2.0;
            targetY = panel.getHeight() / 2.0;
        }/*else {
            if(!isObstacle) {
                //有问题
                //compute the default direction angle of circling group
                double slope = y/x;
                targetX = Math.sqrt(Math.pow(Maxcircle.getCircleRadius(), 2) / (1 + Math.pow(slope, 2)));
                targetY =  -slope * targetX;
                if (x < 0) {
                    targetX = -targetX;
                    targetY = -targetY;
                }

            }else {
                targetX =0;
                targetY =0;
            }
        }*/
        // 计算到目标点的距离
        double distanceToTarget = Math.hypot(targetX - x, targetY - y);
        // 对于蓝色机器人（聚集组），设置目标点为窗口中心
        if ("gathering".equals(group)) {
            angle = Math.atan2(targetY-y , targetX-x );
            if (distanceToTarget < speed) {
                // 如果距离小于速度步长，直接移动到目标点并停止
                x = targetX;
                y = targetY;
            }
        } else {
            angle = Math.atan2(y - panel.getHeight()/2, x - panel.getWidth()/2);
            if (distanceToTarget < speed) {
                // 如果距离小于速度步长，直接移动到目标点并停止
                x = targetX;
                y = targetY;
            }
        }
        // 计算到目标点的距离
    }

    @Override
    public void run() {
        new Timer(10, e -> { // 减少定时器延迟以增加机器人刷新率
            //如果激活false，则睡眠

                look();
                compute();
                move();

        }).start();
    }

    public void move() {
        if(circle == null) return;
        if(!circle.equals(innerSide)){return;}
        if (!active) return; // 如果机器人处于非激活状态，则不移动
        // 根据角度和速度更新位置

        robots = circle.getRobots();

        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
        System.out.println(robots.size());

       // 判断在不在当前圆上
        if(!circle.isInScope(this.distanceToOrigin())){
            robots.remove(this);
        }


        if(robots.size()==0){
            circles.remove(circle);
        }



        System.out.println(robots.size());


    }

    public void draw(Graphics g) {
        if ("gathering".equals(group)) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.RED);
        }
        g.fillOval((int) x - (DOT_SIZE / 2), (int) y - (DOT_SIZE / 2), DOT_SIZE, DOT_SIZE); // 画一个小圆代表机器人
    }

    // 计算到原点的距离
    public double distanceToOrigin() {
        double centerX = RobotSimulation.WINDOW_WITH / 2.0;
        double centerY = RobotSimulation.WINDOW_HEIGHT / 2.0;
        return Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
    }

    public void setCircle(Circle circle){
        this.circle = circle;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // 省略getter和setter方法

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

    public void setFinish(boolean flag){
        finish = flag;
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

}
