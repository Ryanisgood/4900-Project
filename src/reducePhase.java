import java.util.ArrayList;

public class reducePhase {
    public void reduceNonPivot(boolean mP,ArrayList< Circle> circles){
        ArrayList<Robot> afterReduce=new ArrayList<>();
        //no multiplicity points 没想好是直接传进来还是在这个方法里读取

        int lastId= circles.size()-1;
        //are more than four robots on S
        if(mP==false&&circles.get(lastId).robotsSizeOnCircle()>4){
            //deal with each circle
            for(int i=0; i< circles.size();i++){
                if(i==0) {
                    //k==1 move towards to O
                }else{
                    //k>1 move to inner
                    Circle currentCircle= circles.get(i);
                    //
                    Circle innerCircle = circles.get(i+1);

                    for(Robot r: currentCircle.showRobots()){
                        //move non-pivot
                        if(r.getPivot()==false){

                        }
                    }
                }

            }


        }

    }

}
