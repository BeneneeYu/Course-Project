package Block;
import java.io.Serializable;
public class OBlock extends Block implements Serializable {
    public OBlock() {
        this.cells = new boolean[][]{
                { false,true,true, false}, {false, true, true, false}, {false, false, false, false}, {false, false, false, false}
        };
    }
    public boolean[][] nextRotatedCells() {
        return cells;
    }
    public void rotate() {
        cells = nextRotatedCells();
    }
    public boolean[][] getCells() {
        return cells;
    }
}