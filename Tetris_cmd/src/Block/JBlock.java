package Block;
import java.io.Serializable;
public class JBlock extends Block implements Serializable {
    public JBlock() {
        this.cells = new boolean[][]{
                { true, false,false, false}, {true, true, true, false}, {false, false, false, false}, {false, false, false, false}
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