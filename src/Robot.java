

import java.awt.*;
import java.util.List;

public class Robot extends Thread{
    private double x, y; // 位置坐标
    private final double speed; // 速度
    private double angle; // 移动角度
    private final String group; // 组别："gathering" 或 "circle"
    private boolean active; // 是否激活
    private boolean isObstacle; //是否遇到障碍物
    private final Environment environment;
    private Circle circle; //所在圆圈
    private Circle Maxcircle;
    private boolean shouldLookAndCompute = false; //是否需要观察和计算移动方向
    private static final int DOT_SIZE = 5; //代表机器人的圆点尺寸
    private boolean pivot;
    private int codeNum;
    private int height = 600;
    private int width =800;
    boolean finish = false;


    public Robot(double x, double y, int codeNum,boolean pivot, double speed, String group, Environment environment) {
        // 将初始位置设置为窗口中心附近
        this.x = 400 + x - 200; // 假设窗口宽度为800
        this.y = 300 + y - 200; // 假设窗口高度为600
        this.codeNum =codeNum;
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

    public double compute(){
        //计算移动方向
        if (!isObstacle) {
            return angle = Math.random() * 2 * Math.PI;
        }
        return angle;
    }

    @Override
    public void run() {
        super.run();
        while (!finish){
            wakeUp();
            if (finish) {
                this.interrupt();
                break;
            }
        }

    }

    public void wakeUp(){
        while (active) {
            if (shouldLookAndCompute) {
                look(); //激活时观察一次
                compute(); //计算移动方向
                shouldLookAndCompute = false;
                System.out.println("1");
            }
            move(width,height);
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        shouldLookAndCompute = true;
    }
    public void move(int width, int height) {
        if (!active) return; // 如果机器人处于非激活状态，则不移动
        double targetX;
        double targetY;
        // 计算目标点
        if ("gathering".equals(group)) {
            targetX = width / 2.0;
            targetY = height / 2.0;
        }else {
            if(!isObstacle) {
                //计算Circling group 的默认前进方向，现在有问题
                double slope = y / x;
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
        }

        // 计算到目标点的距离
        double distanceToTarget = Math.hypot(targetX - x, targetY - y);

        // 对于蓝色机器人（聚集组），设置目标点为窗口中心
        if ("gathering".equals(group)) {
            angle = Math.atan2(targetY - y, targetX - x);
            if (distanceToTarget < speed) {
                // 如果距离小于速度步长，直接移动到目标点并停止
                x = targetX;
                y = targetY;
                finish = true;
                return;
            }
        } else {
            angle = Math.atan2(targetY - y, targetX - x);
            if (distanceToTarget < speed) {
                // 如果距离小于速度步长，直接移动到目标点并停止
                x = targetX;
                y = targetY;
                finish = true;
                return;
            }
        }
        // 根据角度和速度更新位置
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
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

    public void setPivot(boolean flag) {
        this.pivot=flag;
    }
    public boolean getPivot(){
        return this.pivot;
    }

    public int getCodeNum(){
        return this.codeNum;
    }
}