
import java.io.*; 
import java.util.*;

/**
 *
 * @author Adnan Ahmed
 */
class EightPuzzleProblem implements GenericProblem<EightPuzzleBoard, EightPuzzleAction>
{
    private EightPuzzleBoard initialBoard;
    private EightPuzzleBoard goalBoard;

    EightPuzzleProblem(EightPuzzleBoard initialBoard, EightPuzzleBoard goalBoard) {
        this.initialBoard = initialBoard;
        this.goalBoard = goalBoard;
    }

    public int setHvalue(EightPuzzleBoard state)  {
        int hValue = 0;
        hValue = state.calculateHvalue(this.goalBoard.getBoard());
        return hValue;
    }

    public int setHvalueH2(EightPuzzleBoard state)  {
        int hValue = 0;
        hValue = state.calculateHvalue(this.goalBoard.getBoard());
        return hValue;
    }

    @Override //Returns the initial state of the agent
    public EightPuzzleBoard getInitialState() {
        return this.initialBoard;
    }

    @Override //Returns the description of the possible actions available to the agent
    public List<EightPuzzleAction> getActions(EightPuzzleBoard state) {
        List<EightPuzzleAction> actionsList = new ArrayList<EightPuzzleAction>();
        int[][] currentBoard = state.getBoard();
        int row = 0, col = 0;

        //finding 0
        for(int i=0; i<currentBoard.length; i++) {
            for(int j=0; j<currentBoard[i].length; j++) {
                if (currentBoard[i][j]==0) { row = i; col = j; }
            }
        }

        if (col!=0) { actionsList.add(new EightPuzzleAction(Actions.LEFT)); }
        if (col!=2) { actionsList.add(new EightPuzzleAction(Actions.RIGHT)); }
        if (row!=0) { actionsList.add(new EightPuzzleAction(Actions.UP)); }
        if (row!=2) { actionsList.add(new EightPuzzleAction(Actions.DOWN)); }

        return actionsList;
    }

    //Returns the State that is a result of an action
    @Override //This is a transition model. e.g. Successor
    public EightPuzzleBoard getResult(EightPuzzleBoard state, EightPuzzleAction action) {
        state.move(action);
        return state;
    }

    @Override //Determines whether a given state is a goal state
    public boolean goalTest(EightPuzzleBoard state) {
        if (Arrays.deepEquals(state.getBoard(),this.goalBoard.getBoard())) {
            return true;
        }
        return false;
    }

    //Returns the <b>step cost</b> of taking action in state to reach state
    @Override //<code>stateDelta</code> denoted by c(s, a, s')
    public double getStepCosts(EightPuzzleBoard state1, EightPuzzleAction action, EightPuzzleBoard state2) {
        return 1.0;
    }
}
