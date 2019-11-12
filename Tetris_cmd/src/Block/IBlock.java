package Block;
import java.io.Serializable;
public class IBlock extends Block implements Serializable {
    public IBlock() {
        this.cells = new boolean[][]{
                {false,false,false,false},{true,true,true,true},{false,false,false,false},{false,false,false,false}
        };
    }
    public boolean[][] nextRotatedCells() {
        boolean nextCells[][] = new boolean[rows][columns];
        nextCells [0][1] = cells[2][0];
        nextCells [0][2] = cells[1][0];
        nextCells [1][0] = cells[3][1];
        nextCells [1][1] = cells[2][1];
        nextCells [1][2] = cells[1][1];
        nextCells [1][3] = cells[0][1];
        nextCells [2][0] = cells[3][2];
        nextCells [2][1] = cells[2][2];
        nextCells [2][2] = cells[1][2];
        nextCells [2][3] = cells[0][2];
        nextCells [3][1] = cells[2][3];
        nextCells [3][2] = cells[1][3];
        return nextCells;
    }
    public void rotate() {
        cells = nextRotatedCells();
    }
    public boolean[][] getCells(){
        return cells;
    }
}