public class Robot {
    private boolean group;
    private int codeNum;//编号
    private double x_axis;
    private double y_axis;
    private boolean pivot;
    private boolean detectedMoreThanOne;
    public Robot(boolean group,int codeNum,double x, double y, boolean p, boolean detected){
        //boolean true => gathering; false=>formation
        this.group=group;
        this.codeNum=codeNum;
        this.x_axis=x;
        this.y_axis=y;
        this.pivot=p;
        this.detectedMoreThanOne=detected;

    }
    //Get coordinate of the x-axis
    public double getX(){
        return this.x_axis;
    }

    //Get coordinate of the y-axis
    public double getY(){
        return this.y_axis;
    }

    public void setX_axis(double x){
        this.x_axis=x;
    }

    public void setY_axis(double y){
        this.y_axis=y;
    }


    public void isPivot(){
        this.pivot=true;
    }

    public void isNotPivot(){
        this.pivot=false;
    }

    public void isMoreThanOne() {
        this.detectedMoreThanOne=true;
    }

    public void isNotMoreThanOne() {
        this.detectedMoreThanOne=false;
    }
}
