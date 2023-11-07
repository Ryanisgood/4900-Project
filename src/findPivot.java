import java.util.ArrayList;

// Determine the pivot points of each circle
public class findPivot {
    public void findCloseRobots(ArrayList<Robot> robots,Robot pivot){
        double minDistance1 = Integer.MAX_VALUE;
        double minDistance2 = Integer.MIN_VALUE;
        double currentPositiveX=0;
        double currentNegativeX=0;


        //Find the two closest distances of p2
        for (int i=0;i< robots.size();i++) {
            Robot rb= robots.get(i);
            if(rb.getY()<0.0){
                //Calculate distance between each point and pivot
                double yDistance= rb.getY()-pivot.getY();
                double distance= Math.sqrt(Math.pow(rb.getX(),2)+Math.pow(yDistance,2));
                double x=rb.getX();
                // Positive side
                if(x>0.0){
                    if (distance<minDistance1){
                        minDistance1 = distance;
                        currentPositiveX=x;
                    }
                }
                // Negative side
                if(x<0.0){
                    if (distance>minDistance2){
                        minDistance2 = distance;
                        currentNegativeX = x;

                    }
                }
            }

        }

        //Set the robots on the two closest distance as the pivots
        for(Robot robot: robots){
            if (robot.getX()==currentPositiveX){
                robot.setPivot(true);
            }else if (robot.getX()==currentNegativeX){
                robot.setPivot(true);
            }
        }

    }



    public void findPivot(Circle eachCircle, double radius) {
        ArrayList<Robot> robots =new ArrayList<>(eachCircle.showRobots());
        //Condition1: less than 4 robots in a circle

        if (robots.size() < 4) {
            for (Robot r : robots) {
                r.setPivot(true);
            }
        }

        //More than four robots on a circle :
        else {
            //Count the number of robots
            int count = 0;
            Robot p1=null, p2=null;
            //Find p1 and p2
            for (Robot r : robots) {
                //check if there is the robot on the p1 and p2
                // p1 is located in the top of the circle
                if (r.getX() == 0.0 && r.getY() == radius) {
                    p1 = r;

                    count++;
                }
                //p2 is located in the lowest point of the circle
                if (r.getX() == 0.0 && r.getY() == -radius) {
                    p2 = r;
                    count++;
                }

                //Condition 1: only one
                if (count == 1) {
                    p1.setPivot(true);
                    findCloseRobots(robots,p2);
                }

                //Condition2: two robots on the Y-axis, p1 already set to true
                else if(count==2){
                    p1.setPivot(true);
                    p2.setPivot(true);
                }

                //Condition3: none of robots
                else if(count==0){
                    //Close to p1
                    findCloseRobots(robots,p1);

                    //Close to p2
                    findCloseRobots(robots,p2);

                }


            }

        }

    }


}
