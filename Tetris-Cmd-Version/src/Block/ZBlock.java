package Block;
import java.io.Serializable;
public class ZBlock extends Block implements Serializable {
    public ZBlock() {
        this.cells = new boolean[][]{
                {true,true,false,false},{false,true,true,false},{false,false,false,false},{false,false,false,false}
        };
    }
    public boolean[][] nextRotatedCells() {
return super.nextRotatedCellsForReal3cross3();
    }
    public void rotate() {
        cells = nextRotatedCells();
    }
    public boolean[][] getCells(){
        return cells;
    }
}