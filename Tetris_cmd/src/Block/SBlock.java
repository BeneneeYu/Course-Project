package Block;
import java.io.Serializable;
public class SBlock extends Block implements Serializable {
    public SBlock() {
        this.cells = new boolean[][]{
                {false,true,true,false},{true,true,false,false},{false,false,false,false},{false,false,false,false}
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

