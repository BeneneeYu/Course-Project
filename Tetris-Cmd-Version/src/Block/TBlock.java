package Block;
import java.io.Serializable;
public class TBlock extends Block implements Serializable {
    public TBlock() {
        this.cells = new boolean[][]{
                {false, true, false, false}, {true, true, true, false}, {false, false, false, false}, {false, false, false, false}
        };
    }
    public boolean[][] nextRotatedCells() {
        return super.nextRotatedCellsForReal3cross3();
    }
    public void rotate() {
        cells = nextRotatedCells();
    }
    public boolean[][] getCells() {
        return cells;
    }
}
