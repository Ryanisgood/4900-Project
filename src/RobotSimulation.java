
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
public class RobotSimulation extends JFrame {
    protected static SimulationPanel panel;
    private final Environment environment;

    public ThreadPoolExecutor robotsPoolExecutor;
    public static final int WINDOW_WITH = 800;
    public static final int WINDOW_HEIGHT = 600;


    private java.util.List<Robot> robots;
    public RobotSimulation() {
        environment = new Environment(this);
        panel = new SimulationPanel();
        initUI();
        robots = environment.getRobots();
        //boot the robots
        robotsPoolExecutor =new ScheduledThreadPoolExecutor(100);
        bootRobots();
    }



    private void bootRobots() {
        for (Robot robot : robots) {
            robotsPoolExecutor.submit(robot);
        }
    }

    private void initUI() {
        setTitle("Robot Simulation");
        setSize(WINDOW_WITH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        setUpTimer();
    }

    private void setUpTimer() {
        new Timer(10, e -> {
            environment.update(panel.getWidth(), panel.getHeight());
            int activeThreads = Thread.activeCount();
            System.out.println("activeThreads: " + activeThreads);
            panel.repaint();
        }).start();
    }

    class SimulationPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawCenter(g);
            drawAxes(g);
            for (Robot robot : environment.getRobots()) {
                robot.draw(g);
            }
            for (Circle circle : environment.getCircleList()) {
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                circle.draw(centerX,centerY,g);
            }
        }

        private void drawCenter(Graphics g) {
            int pointSize = 5;
            g.setColor(Color.BLACK);
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            g.fillOval(centerX - (pointSize / 2), centerY - (pointSize / 2), pointSize, pointSize); // 绘制中心点

        }

        private void drawAxes(Graphics g) {

            //网格刻度大小
            int radius = 50;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 画中心线
            g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
            g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            // 设置网格和坐标刻度
            g2d.setColor(Color.LIGHT_GRAY);
            for (int i = radius; i < getWidth() / 2; i += radius) {
                g2d.drawLine(getWidth() / 2 + i, 0, getWidth() / 2 + i, getHeight()); // x正轴网格
                g2d.drawLine(getWidth() / 2 - i, 0, getWidth() / 2 - i, getHeight()); // x负轴网格
                g2d.drawLine(0, getHeight() / 2 + i, getWidth(), getHeight() / 2 + i); // y正轴网格
                g2d.drawLine(0, getHeight() / 2 - i, getWidth(), getHeight() / 2 - i); // y负轴网格
            }

            // 设置坐标轴数字
            g2d.setColor(Color.BLACK);
            for (int i = radius; i < getWidth() / 2; i += radius) {
                g2d.drawString(String.valueOf(i), getWidth() / 2 + i - 3, getHeight() / 2 + 15); // x正轴数字
                g2d.drawString(String.valueOf(-i), getWidth() / 2 - i - 10, getHeight() / 2 + 15); // x负轴数字
                g2d.drawString(String.valueOf(-i), getWidth() / 2 + 5, getHeight() / 2 + i + 3); // y正轴数字
                g2d.drawString(String.valueOf(i), getWidth() / 2 + 5, getHeight() / 2 - i + 3); // y负轴数字
            }

        }
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new RobotSimulation().setVisible(true));
    }
}
