import java.io.*;
import Block.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
public class Game  implements Serializable{
    private static final long serialVersionUID = 7401066204744542318L;
    public Game(){};
    public Game(String username,String archivelocation){
        this();
        this.username = username;
        this.usernum = archivelocation;

    }
    public boolean[][] cells;//The map of the game.
    public int rows = 15;
    public int columns = 10;
    int score = 0;
    public int time = 0;
    public int posRow = 0;
    public int posColumn = (int) (this.columns / 2) - 1;//So thar the block can appear in the middle of the top.
    public int dropRow;
    public int dropColumn;
    int changeWeaponTime = 3;
    int getIWeaponTime = 3;
    Block currentBlock;//Initialize the current block in advance.
    Block nextBlock;//Initialize the next block in advance.
    String username;
    String usernum;
    //To get a random block.
    private Block getRandomBlock() {
        Block randomBlock;
        int type = (int) (7 * Math.random());
        switch (type) {
            case (0):
                randomBlock = new LBlock();
                break;
            case (1):
                randomBlock = new ZBlock();
                break;
            case (2):
                randomBlock = new SBlock();
                break;
            case (3):
                randomBlock = new OBlock();
                break;
            case (4):
                randomBlock = new TBlock();
                break;
            case (5):
                randomBlock = new JBlock();
                break;
            case (6):
                randomBlock = new IBlock();
                break;
            default:
                randomBlock = new IBlock();
        }
        return randomBlock;
    }

    public void nextBlock() {
        posRow = 0;
        posColumn = (int) (this.columns / 2) - 1;
        nextBlock = getRandomBlock();
    }



    //Initialize the map,let the map become a blank first.
    public void initial(int height, int width) {
        this.posRow = 6;
        this.posColumn = 6;
        this.rows = height;
        this.columns = width;
        this.cells = new boolean[rows][columns];
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                cells[i][j] = false;
            }
        }
    }

    public void initiateBlock() {
        currentBlock = getRandomBlock();
    }

    //To judge if the game is over.
    public boolean isOver() {
        return collide(posRow, posColumn, currentBlock.getCells());
    }


    public boolean collide(int nextPosRow, int nextPosColumn, boolean[][] nextBlockCells) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int posX = nextPosColumn + j;
                int posY = nextPosRow + i;
                if (((posY >= this.cells.length) || (posX <= -1) || (posX >= this.cells[0].length)) && nextBlockCells[i][j]) {
                    return true;
                }
                if (posY < this.cells.length && posX > -1 && posX < this.cells[0].length && nextBlockCells[i][j] && (this.cells[posY][posX])) {
                    return true;
                }
            }
        }
        return false;
    }

    //To judge if the block will have reach the bottom of the map.
    public boolean reachTheBottom() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int posY = posRow + i;
                if ((posY >= this.cells.length - 1) && (this.currentBlock.getCells()[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    //To judge if there is anything under the current block which may will hinder the block.
    public boolean somethingUnderBlock() {
        return collide(posRow + 1, posColumn, currentBlock.getCells());
    }

    //After the block can not move anymore,we should load the current block in the map.
    public void loadBlock() {
        int bottom = 1;
        if (reachTheBottom() || somethingUnderBlock()) {
            for (int n = 0; n < this.currentBlock.getCells().length; n++)
                for (int m = 0; m < this.currentBlock.getCells()[n].length; m++) {
                    if (this.currentBlock.getCells()[n][m]) {
                        bottom = n + 1;
                    }
                }
            for (int n = 0; n < bottom; n++) {
                for (int m = 0; m < 4; m++) {
                    int posXX = posColumn + m;
                    int posYY = posRow + n;
                    if (this.currentBlock.getCells()[n][m]) {
                        this.cells[posYY][posXX] = this.currentBlock.getCells()[n][m];
                    }
                }
            }
            Main.newi = true;

        }
    }

    private boolean full(int row) {
        for (int a = 0; a < this.cells[row].length; a++) {
            if (!this.cells[row][a]) {
                return false;
            }
        }
        return true;
    }

    //After a row is full,eliminate it.
    public void eliminate() {
        int scoreCombo = 0;
        int rowFlag = 0;
        int scoreStep = 10;
        while (rowFlag < this.cells.length) {
            if (full(rowFlag)) {
                eliminateOneRow(rowFlag);
                scoreCombo++;
                rowFlag--;
                for (int width = this.cells[0].length - 1; width > 0; width--) {
                    this.cells[0][width] = false;
                }
            }
            rowFlag++;
        }
        if (scoreCombo > 1) {
            this.changeWeaponTime++;
        }
        if (scoreCombo > 2) {
            this.getIWeaponTime++;
        }
        for (int i = 0; i < scoreCombo - 1; i++) {
            scoreStep = scoreStep * 2;
        }
        this.score = this.score + scoreStep * scoreCombo;
        new operation().playLowerVolumeMusic("./Resource/Music/SEB_platinum.wav",0.04);
    }

    private void eliminateOneRow(int row) {
        for (int flagone = row; flagone > 0; flagone--) {
            for (int flagtwo = this.cells[flagone].length - 1; flagtwo > -1; flagtwo--) {
                this.cells[flagone][flagtwo] = this.cells[flagone - 1][flagtwo];
            }
        }
    }

    //The several following methods concerning about the movement of block.
    public void goLeft() {
        {
            if (!collide(this.posRow, this.posColumn - 1, this.currentBlock.getCells())) {
                this.posColumn -= 1;
            }
        }
    }

    public void goRight() {
        {
            if (!collide(this.posRow, this.posColumn + 1, this.currentBlock.getCells())) {
                this.posColumn += 1;
            }
        }
    }

    public void goDown() {
        {
            if (!collide(this.posRow + 1, this.posColumn, this.currentBlock.getCells())) {
                this.posRow += 1;
            }
        }
    }

    public void goBottom() {
        while (!collide(this.posRow + 1, this.posColumn, this.currentBlock.getCells())) {
            this.posRow += 1;
        }
    }

    //To judge where should the shadow appear,we assume the block can move down consistently,once it cannot go down,shadow should appear there.
    public void shadowBottom() {
        this.dropRow = this.posRow;
        this.dropColumn = this.posColumn;
        while (!collide(this.dropRow + 1, this.dropColumn, this.currentBlock.getCells())) {
            this.dropRow += 1;
        }
    }

    //The two methods to use properties.
    public void changeWeapon() {
        if (changeWeaponTime > 0) {
            this.currentBlock = this.nextBlock;
            nextBlock();
            changeWeaponTime--;
        }
    }

    public void getIWeapon() {
        if (getIWeaponTime > 0) {
            this.nextBlock = new IBlock();
            getIWeaponTime--;
        }
    }


    //This method is designed to initialize the game.
    public void getGame() {
        this.score = 0;
        this.time = 0;
        this.getIWeaponTime = 3;
        this.changeWeaponTime = 3;
        this.initial(15, 10);
        this.initiateBlock();
        this.nextBlock();
    }

    //Make these operations a group to make it clear;
    public void eliminateGroup() {
        loadBlock();
        eliminate();
        this.currentBlock = this.nextBlock;
        nextBlock();
    }

    //Print the new map.
    public void paintMap(GridPane pane) {
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j]) {
                    Rectangle rectangle = new Rectangle();
                    rectangle.setWidth(29.1);
                    rectangle.setHeight(36.86);
                    rectangle.setStroke(Color.BLACK);
                    rectangle.setFill(Color.WHITE);
                    pane.add(rectangle, j, i);
                }
            }
        }
    }
}