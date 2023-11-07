
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DynamicPointsApp extends JPanel {
    // private Map<String,List<Point>> robotsFamily;
    private List<Point2D> gatheringRobots = new ArrayList<>();
    private List<Point2D> circleRobots=new ArrayList<>();
    private List<Robot> robotList  = new ArrayList<>();
    private final int maxPoints = 100;

    private static final int PREF_W = 800;
    private static final int PREF_H = 800;
    private static final int POINT_RADIUS = 5;
    private static final int GRID_GAP = 50;


    public DynamicPointsApp() {
        JFrame frame = new JFrame("Group Points Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(this);

        frame.setSize(PREF_W, PREF_H);
        frame.setVisible(true);

        // Start a thread to add random points continuously
        Thread pointGenerator = new Thread(new RobotsGenerator());
        pointGenerator.start();
    }


    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 画中心线
        g2d.drawLine(PREF_W / 2, 0, PREF_W / 2, PREF_H);
        g2d.drawLine(0, PREF_H / 2, PREF_W, PREF_H / 2);

        // 设置网格和坐标刻度
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = GRID_GAP; i < PREF_W / 2; i += GRID_GAP) {
            g2d.drawLine(PREF_W / 2 + i, 0, PREF_W / 2 + i, PREF_H); // x正轴网格
            g2d.drawLine(PREF_W / 2 - i, 0, PREF_W / 2 - i, PREF_H); // x负轴网格
            g2d.drawLine(0, PREF_H / 2 + i, PREF_W, PREF_H / 2 + i); // y正轴网格
            g2d.drawLine(0, PREF_H / 2 - i, PREF_W, PREF_H / 2 - i); // y负轴网格
        }

        // 设置坐标轴数字
        g2d.setColor(Color.BLACK);
        for (int i = GRID_GAP; i < PREF_W / 2; i += GRID_GAP) {
            g2d.drawString(String.valueOf(i), PREF_W / 2 + i - 3, PREF_H / 2 + 15); // x正轴数字
            g2d.drawString(String.valueOf(-i), PREF_W / 2 - i - 10, PREF_H / 2 + 15); // x负轴数字
            g2d.drawString(String.valueOf(-i), PREF_W / 2 + 5, PREF_H / 2 + i + 3); // y正轴数字
            g2d.drawString(String.valueOf(i), PREF_W / 2 + 5, PREF_H / 2 - i + 3); // y负轴数字
        }
        g.setColor(Color.RED);
        for (Point2D point : gatheringRobots) {
            System.out.println(point);
            int x = PREF_W / 2 + (int) point.getX() - POINT_RADIUS; // 调整点的x坐标
            int y = PREF_H / 2 - (int) point.getY() - POINT_RADIUS; // 调整点的y坐标，注意y方向相反
            g.fillOval(x,y, 5, 5);
        }
        g.setColor(Color.BLUE);
        for (Point2D point: circleRobots){
            int x = PREF_W / 2 + (int) point.getX() - POINT_RADIUS; // 调整点的x坐标
            int y = PREF_H / 2 - (int) point.getY() - POINT_RADIUS; // 调整点的y坐标，注意y方向相反
            g.fillOval(x,y,5,5);
        }



    }

    private class RobotsGenerator implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            int counter = 0;
            while (gatheringRobots.size()+circleRobots.size() < maxPoints) {

                double x = random.nextInt(getWidth()) - 400;
                double y = random.nextInt(getHeight()) - 400;
                Robot robots = new Robot(true, counter++, x, y, false, false);//initializing robots
                if (counter < maxPoints/2){
                    robots.setGroup(true);
                    robotList.add(robots);
                    gatheringRobots.add(robots.getLocation());
                }
                else {
                    robots.setGroup(false);
                    robotList.add(robots);
                    circleRobots.add(robots.getLocation());
                }

                repaint();

                try {
                    Thread.sleep(100); // Adjust the delay as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //测试，原点robot
            Robot robots = new Robot(true, counter++, 0, 0, false, false);
            gatheringRobots.add(robots.getLocation());
            repaint();
        }

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DynamicPointsApp();
        });


    }

}