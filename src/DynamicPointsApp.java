
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


    public DynamicPointsApp() {

        JFrame frame = new JFrame("Group Points Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(800, 800);
        frame.setVisible(true);

        // Start a thread to add random points continuously
        Thread pointGenerator = new Thread(new RobotsGenerator());
        pointGenerator.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        for (Point2D point : gatheringRobots) {
            g.fillOval((int) point.getX(), (int) point.getY(), 5, 5);
        }
        g.setColor(Color.BLUE);
        for (Point2D point:circleRobots){
            g.fillOval((int) point.getX(),(int)point.getY(),5,5);
        }
    }

    private class RobotsGenerator implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            int counter = 0;
            while (gatheringRobots.size()+circleRobots.size() < maxPoints) {
                double x = random.nextInt(getWidth());
                double y = random.nextInt(getHeight());
                Robot robots = new Robot(true, counter++, x, y, false, false);//initializing robots
                if (gatheringRobots.size()<maxPoints/2){
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
            for (Robot robot : robotList) {
                System.out.println(robot);
            }


        }

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DynamicPointsApp();
        });
    }

}
