import java.util.concurrent.*;
import java.util.List;
public class RobotScheduler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private List<Robot> robots;
    public RobotScheduler(Environment environment) {
        this.environment = environment;
        robots = environment.getRobots();
    }
    public void start(Robot robot) {
        final Runnable checker = new Runnable() {
            public void run() { 
                if(isNotActive()){
                    for (Robot robot : robots) {
                        robot.setActive(true);
                    }
                }
            }
        };
        final ScheduledFuture<?> checkerHandle = scheduler.scheduleAtFixedRate(checker, 10, 10, TimeUnit.SECONDS);
    }

    //检测所有机器人是否为非激活状态
    private boolean isNotActive(){
        for (Robot robot : robots) {
            if(robot.isActive()){
                return false;
            }
        }
        return true;
    }
}