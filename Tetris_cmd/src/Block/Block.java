package Block;
import java.io.Serializable;
public class Block implements Serializable {
    public boolean cells[][];
    // Dim the rows of cells.
    public static final int rows = 4;
    // Dim the columns of cells.
    public static final int columns = 4;
    public boolean isNew = false;
     public boolean[][] nextRotatedCells(){return  null;};
    public void rotate() {
        cells = nextRotatedCells();
    }
    public boolean[][] getCells() {
        return cells;
    }
    public boolean[][] nextRotatedCellsForReal3cross3() {
        boolean nextCells[][] = new boolean[rows][columns];
        nextCells[1][0] = cells[2][1];
        nextCells[2][1] = cells[1][2];
        nextCells[1][2] = cells[0][1];
        nextCells[0][1] = cells[1][0];
        nextCells[0][0] = cells[2][0];
        nextCells[2][0] = cells[2][2];
        nextCells[2][2] = cells[0][2];
        nextCells[0][2] = cells[0][0];
        nextCells[1][1] = cells[1][1];
        nextCells[0][3] = cells[0][3];
        nextCells[1][3] = cells[0][3];
        nextCells[2][3] = cells[0][3];
        nextCells[3][3] = cells[0][3];
        return nextCells;
    }
}
