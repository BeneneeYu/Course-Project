import java.io.*;
import Block.*;
import java.util.StringTokenizer;
import java.util.Scanner;
public class Game implements Serializable {
    private boolean[][] cells;//The map of the game.
    private int rows;
    private int columns;
    int score = 0;
    private int posRow = 0;
    private int posColumn = (int) (this.columns / 2) - 1;//So thar the block can appear in the middle of the top.
    private int dropRow;
    private int dropColumn;
    int changeWeaponTime = 3;
    int getIWeaponTime = 3;
    Block currentBlock;//Initialize the current block in advance.
    Block nextBlock;//Initialize the next block in advance.

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

    public Game() {
    }

    //Initialize the map,let the map become a blank first.
    public void initial(int height, int width) {
        posRow = 0;
        posColumn = 5;
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
        posRow = 0;
        posColumn = (int) (this.columns / 2) - 1;
        currentBlock = getRandomBlock();
    }

    //To judge if the game is over.
    public boolean isOver() {
//        if (currentBlock.isNew) {
            return collide(posRow, posColumn, currentBlock.getCells());
//        }
//        return false;
    }

    //The method to get the whole game as a string.
    public String print() {
        StringBuilder sBuffer = new StringBuilder("Game\n" + "下一个图形\n");
        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 4; n++) {
                if (this.nextBlock.getCells()[m][n]) {
                    sBuffer.append("* ");
                } else {
                    sBuffer.append("  ");
                }
            }
            sBuffer.append("\n");
        }
        for (int i = 0; i < this.rows; i++) {
            sBuffer.append("| ");
            for (int j = 0; j < this.columns; j++) {
                if (((i >= posRow) && (i < posRow + 4)) && ((j >= posColumn) && (j < posColumn + 4))) {
                    if (currentBlock.getCells()[i - posRow][j - posColumn]) {
                        sBuffer.append("* ");
                    } else {
                        if (this.cells[i][j]) {
                            sBuffer.append("* ");
                        } else {
                            if (((i >= dropRow) && (i < dropRow + 4)) && ((j >= dropColumn) && (j < dropColumn + 4))) {
                                if (currentBlock.getCells()[i - dropRow][j - dropColumn]) {
                                    sBuffer.append("+ ");
                                } else sBuffer.append("  ");
                            } else
                                sBuffer.append("  ");
                        }
                    }
                } else if (((i >= dropRow) && (i < dropRow + 4)) && ((j >= dropColumn) && (j < dropColumn + 4))) {
                    if (currentBlock.getCells()[i - dropRow][j - dropColumn]) {
                        sBuffer.append("+ ");
                    } else {
                        if (this.cells[i][j]) {
                            sBuffer.append("* ");
                        } else {
                            sBuffer.append("  ");
                        }
                    }
                } else if (!this.cells[i][j]) {
                    sBuffer.append("  ");
                } else
                    sBuffer.append("* ");
            }
            sBuffer.append("|\n");
        }
        sBuffer.append(" -");
        for (int q = 0; q < columns * 2; q++) {
            sBuffer.append("-");
        }
        sBuffer.append("\n" + Words.showScore + this.score + "分\n" + Words.changeWeaponTime + this.changeWeaponTime + "\n" + Words.getIWeaponTime + this.getIWeaponTime + "\n" + Words.console);
        return sBuffer.toString();
    }

    //To distinguish the command and execute it.
    public void executeCommand(char command) {
        this.currentBlock.isNew = false;
        switch (command) {
            case ('a'):
                goLeft();
                break;
            case ('d'):
                goRight();
                break;
            case ('w'):
                if (!this.collide(this.posRow + 1, this.posColumn, this.currentBlock.nextRotatedCells())) {
                    currentBlock.rotate();
                    break;
                }
            case ('s'):
                break;
            case ('x'):
                goBottom();
                break;
            case ('q'):
                System.out.println("是否存档？（输入y表示存储，输入其它表示不存储。）");
                char cd = getFirst();
                if(cd == 'y'){
                    System.out.println(Words.askName);
                    String username = this.getAll();
                    System.out.println("输入存档位置数字。");
                    int num = (int)(getFirst() - '0');
                    outGame(this,username,num);
                    System.out.println("好的，您的游戏已存档成功，即将退出游戏，祝您生活愉快！");
                }
                System.exit(0);
            case ('1'):
                changeWeapon();
                break;
            case ('2'):
                getIWeapon();
                break;
            default:
                System.out.println(command + Words.commandError);
        }
        if (!reachTheBottom() && !somethingUnderBlock()) {
            goDown();
        } else {
            this.loadBlock();
            this.eliminate();
            this.currentBlock = this.nextBlock;
            nextBlock();
        }
    }

    //To judge if the current block will collide with the wall or the already existing blocks.
    private boolean collide(int nextPosRow, int nextPosColumn, boolean[][] nextBlockCells) {
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
    private boolean reachTheBottom() {
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
    private boolean somethingUnderBlock() {
        return collide(posRow + 1, posColumn, currentBlock.getCells());
    }
    //After the block can not move anymore,we should load the current block in the map.
    private void loadBlock() {
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
    private void eliminate() {
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
        if(scoreCombo > 1){
            this.changeWeaponTime++;
        }
        if(scoreCombo > 2){
            this.getIWeaponTime++;
        }
        for(int i = 0;i < scoreCombo - 1;i++){
            scoreStep = scoreStep * 2;
        }
        this.score = this.score + scoreStep * scoreCombo;
    }
    private void eliminateOneRow(int row) {
        for (int flagone = row; flagone > 0; flagone--) {
            for (int flagtwo = this.cells[flagone].length - 1; flagtwo > -1; flagtwo--) {
                this.cells[flagone][flagtwo] = this.cells[flagone - 1][flagtwo];
            }
        }
    }
    //The several following methods concerning about the movement of block.
    private void goLeft() {
        {
            if (!collide(this.posRow, this.posColumn - 1, this.currentBlock.getCells())) {
                this.posColumn -= 1;
            }
        }
    }
    private void goRight() {
        {
            if (!collide(this.posRow, this.posColumn + 1, this.currentBlock.getCells())) {
                this.posColumn += 1;
            }
        }
    }
    private void goDown() {
        {
            if (!collide(this.posRow + 1, this.posColumn, this.currentBlock.getCells())) {
                this.posRow += 1;
            }
        }
    }
    private void goBottom() {
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

    //The two methods to get the past user's name and score.
    public int getNum(String a) {
        int Num = 0;
        if (a != null && !"".equals(a) && !"null".equals(a)) {
            StringTokenizer b = new StringTokenizer(a);
            b.nextToken();
            String c = b.nextToken();
            if (c != null && !"".equals(c) && !"null".equals(c)) {
                Num = Integer.parseInt(c);
                return Num;
            }
            return Num;
        }
        return Num;
    }
    public String getName(String name) {
        if (name != null && !"".equals(name) && !"null".equals(name)) {
            StringTokenizer part = new StringTokenizer(name);
            String Name = part.nextToken();
            if (Name != null && !"".equals(Name)) {
                return Name;
            } else {
                return null;
            }
        }
        return null;
    }

    //The method to insert the current user's score and name and show the ranking afterwards.
    public void printRanking(String Name)throws IOException {
        System.out.println(Words.showFinalScore + this.score);
        FileWriter wt = new FileWriter("./myfile.txt", true);
        BufferedReader in = new BufferedReader(new FileReader("./myfile.txt"));
        BufferedWriter out = new BufferedWriter(wt);
        out.write(Name + "\t" + this.score + "\n");
        out.flush();
        out.close();
        String[] nameAndScore = new String[256];
        for (int i = 0; i < 256; i++) {
            String tempStr = in.readLine();
            if (tempStr != null) {
                if (!"".equals(tempStr)) {
                    nameAndScore[i] = tempStr;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        for(int i=0;i < nameAndScore.length-1;i++) {//冒泡趟数
            for (int j = 0; j < nameAndScore.length - i - 1; j++) {
                //如果顺序不对，则交换两个元素
                if (this.getNum(nameAndScore[j + 1]) < this.getNum(nameAndScore[j])) {
                    String temp = "";//定义一个临时变量
                    temp = nameAndScore[j];
                    nameAndScore[j] = nameAndScore[j + 1];
                    nameAndScore[j + 1] = temp;
                }
            }
        }
        System.out.println(Words.rank);
        for (int m = nameAndScore.length - 1; m > nameAndScore.length - 11; m--) {
            System.out.println("第" + (nameAndScore.length-m) + "名" + this.getName(nameAndScore[m]) +"的分数是"  + this.getNum(nameAndScore[m]));
        }
    }
    //Scan the first letter.
    public  char getFirst(){
        Scanner scanner = new Scanner(System.in);
        String string = scanner.next();
        return  string.charAt(0);
    }
    //Scan the whole String.
    public  String getAll(){
        Scanner scanner = new Scanner(System.in);
        return  scanner.next();
    }
    //Output the instance of Game.
    private static void outGame(Game game,String username,int num) {
        FileOutputStream fos = null;
        //声明对象处理流
        ObjectOutputStream oos = null;
        try {
            File file = new File(game.getClass().getName() + "_" + username + num + ".dat");
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭资源
                fos.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Input the instance of game.
    public static Game getGame(String classname,String username,int num) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Game s = null;
        try {
            File file = new File(classname + "_" + username + num + ".dat");
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            s = (Game)ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s ;
    }
    //This method is designed to initialize the game.
    public void getGame() {
        this.getIWeaponTime = 3;
        this.changeWeaponTime = 3;
        this.initial(15, 10);
        this.initiateBlock();
        this.nextBlock();
    }
}