
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DynamicPointsApp extends JPanel {
   // private Map<String,List<Point>> robotsFamily;

    private List<Point> gatheringRobots = new ArrayList<>();
    private List<Point> circleRobots=new ArrayList<>();

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
        for (Point point : gatheringRobots) {
            g.fillOval(point.x, point.y, 5, 5);
        }
        g.setColor(Color.BLUE);
        for (Point point:circleRobots){
            g.fillOval(point.x,point.y,5,5);
        }
    }

    private class RobotsGenerator implements Runnable {
        @Override
        public void run() {
            Random random = new Random();

            while (gatheringRobots.size()+circleRobots.size() < maxPoints) {
                int x = random.nextInt(getWidth());
                int y = random.nextInt(getHeight());

                if (gatheringRobots.size()<maxPoints/2){
                    gatheringRobots.add(new Point(x, y));
                }
                else {
                    circleRobots.add(new Point(x,y));
                }




                repaint();

                try {
                    Thread.sleep(100); // Adjust the delay as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(circleRobots.size());
            System.out.println(gatheringRobots.size());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DynamicPointsApp();
        });
    }
}
