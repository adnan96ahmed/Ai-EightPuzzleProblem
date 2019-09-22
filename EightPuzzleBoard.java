
import java.io.*; 
import java.util.*;

/**
 *
 * @author Adnan Ahmed
 */
class EightPuzzleBoard implements GenericState<EightPuzzleBoard, EightPuzzleAction>
{
    private int[][] board = new int[3][3];
    private int hValue = 0;
    private int fValue = 0;

    EightPuzzleBoard(int[][] board, int hValue, int fValue) {
        this.board = board;
        this.hValue = hValue;
        this.fValue = fValue;
    }

    public void setFvalue(int fValue) {
        this.fValue = fValue;
    }

    public int getHvalue() {
        return this.hValue;
    }

    public int getFvalue() {
        return this.fValue;
    }

    //helper to retrieve 2D array for comparison
    public int[][] getBoard() {
        return this.board;
    }

    //helper to retrieve 2D array for comparison
    public int[][] copyBoard(int[][] newBoard) {
        for(int i=0; i<this.board.length; i++) {
            for(int j=0; j<this.board[i].length; j++) {
                newBoard[i][j] = this.board[i][j];
            }
        }
        return newBoard;
    }

    //function to calculate Hvalue - misplaced tiles
    public int calculateHvalue(int[][] goalBoard) {
        int hValue = 0;
        for(int i=0; i<this.board.length; i++) {
            for(int j=0; j<this.board[i].length; j++) {
                if ((this.board[i][j] != 0) && (goalBoard[i][j] != this.board[i][j])) {
                    hValue = hValue+1;
                }
            }
        }
        this.hValue = hValue;
        return this.hValue;
    }

    //function to calculate Hvalue - manhatten distance
    public int calculateHvalueH2(int[][] goalBoard) {
        int checkValue = 0;
        int hValue = 0;
        for(int i=0; i<this.board.length; i++) {
            for(int j=0; j<this.board[i].length; j++) {
                if (this.board[i][j] != 0) {
                    checkValue = this.board[i][j];
                    for(int k=0; k<goalBoard.length; k++) {
                        for(int l=0; l<goalBoard[k].length; l++) {
                            if (goalBoard[k][l] != 0 && checkValue == goalBoard[k][l]) {
                                hValue = hValue + (Math.abs(i-k)) + (Math.abs(j-l));
                            }
                        }
                    }
                }
            }
        }
        this.hValue = hValue;
        return this.hValue;
    }

    @Override //return the state
    public EightPuzzleBoard getState() {
        return this;
    }

    @Override //print out the current state
    public void printState() {
        for(int i=0; i<this.board.length; i++) {
            for(int j=0; j<this.board[i].length; j++) {
                System.out.print(this.board[i][j]+" ");
                if (j==2) { System.out.println(); }
            }
        }
    }

    @Override //move from action a
    public void move(EightPuzzleAction a) {
        int row = 0, col = 0, temp = 0;

        //finding 0
        for(int i=0; i<this.board.length; i++) {
            for(int j=0; j<this.board[i].length; j++) {
                if (this.board[i][j]==0) { row = i; col = j; }
            }
        }

        switch(a.getAction()) {
            case LEFT:
                temp = this.board[row][col-1];
                this.board[row][col-1] = 0;
                this.board[row][col] = temp;
                break;
           case RIGHT:
                temp = this.board[row][col+1];
                this.board[row][col+1] = 0;
                this.board[row][col] = temp;                
                break;
           case UP:
                temp = this.board[row-1][col];
                this.board[row-1][col] = 0;
                this.board[row][col] = temp;                
                break;
           case DOWN:
                temp = this.board[row+1][col];
                this.board[row+1][col] = 0;
                this.board[row][col] = temp;                
                break;
           default:
                System.out.println("INVALID ACTION");
        }
    }
}
