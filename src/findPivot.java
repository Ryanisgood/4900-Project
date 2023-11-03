// Determine the pivot points
public class findPivot {
    public void findPivot(Robot eachCircle[], double radius) {
        //Condition1: less than 4 robots in a circle
        if (eachCircle.length < 4) {
            for (Robot r : eachCircle) {
                r.setPivot(true);
            }
        }

        //More than four robots on a circle :
        else {
            //Count the number of robots
            int count = 0;
            Robot p1=null, p2=null;
            //Find p1 and p2
            for (Robot r : eachCircle) {
                //check if there is the robot on the p1 and p2
                // p1 is located in the top of the circle
                if (r.getX() == 0.0 && r.getY() == radius) {
                    p1 = r;
                    r.setPivot(true);
                    count++;
                }
                //p2 is located in the lowest point of the circle
                if (r.getX() == 0.0 && r.getY() == -radius) {
                    p2 = r;
                    count++;
                }

                //Condition 1: only one
                if (count == 1) {
                    double minDistance1 = Integer.MAX_VALUE;
                    double minDistance2 = Integer.MIN_VALUE;

                    //Find the two closest distances of p2
                    for (int i=0;i< eachCircle.length;i++) {
                        Robot rb =eachCircle[i];
                        if(rb.getY()<0.0){
                            if (rb!=p1){
                                double distance= rb.getX();
                                // Positive side
                                if(rb.getX()>0.0){
                                    if (distance<minDistance1){
                                        minDistance1 = distance;
                                    }
                                }
                                // Negative side
                                if(rb.getX()<0.0){
                                    if (distance>minDistance2){
                                        minDistance2 = distance;
                                    }
                                }
                            }
                        }

                    }

                    //Set the robots on the two closest distance as the pivots
                    for(Robot robot: eachCircle){
                        if (robot.getX()==minDistance1){
                            robot.setPivot(true);
                        }else if (robot.getX()==minDistance2){
                            robot.setPivot(true);
                        }
                    }
                }

                //Condition2: two robots on the Y-axis, p1 already set to true
                else if(count==2){
                    p2.setPivot(true);
                }

                //Condition3: none of robots
                else if(count==0){
                    //用atan2
                    //Close to p1

                }


            }

        }

    }
}
