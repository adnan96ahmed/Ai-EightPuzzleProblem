
import java.io.*; 

enum Actions{
    LEFT,RIGHT,UP,DOWN;
}

/**
 *
 * @author Adnan Ahmed
 */
class EightPuzzleAction implements GenericAction
{
    private Actions action;

    EightPuzzleAction(Actions action) {
        this.action = action;
    }

    //return action type
    public Actions getAction() {
        return this.action;
    }

    @Override //print out the action instance
    public void printAction() {
        switch(this.action) {
           case LEFT:
               System.out.print("LEFT");
               break;
           case RIGHT:
               System.out.print("RIGHT");
               break;
           case UP:
               System.out.print("UP");
               break;
           case DOWN:
               System.out.print("DOWN");
               break;
           default:
               System.out.print("INVALID ACTION");
       }
    }
}
