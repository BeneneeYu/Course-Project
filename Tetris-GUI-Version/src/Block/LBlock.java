package Block;
import java.io.Serializable;
public class LBlock extends Block implements Serializable {
    public LBlock() {
        this.cells = new boolean[][]{
                {false, false, true, false}, {true , true, true, false}, {false, false, false, false    }, {false, false, false, false}
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
