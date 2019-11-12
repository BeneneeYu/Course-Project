import java.io.*;
import java.lang.*;
public class Main implements Serializable {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        a:
        while (true) {
            game.getGame();
            System.out.println(Words.startInterface);
            char startcommand = game.getFirst();
            switch (startcommand) {
                case ('s'):
                    System.out.println(Words.askName);
                    String userName = game.getAll();
                    while (true) {
                        game.shadowBottom();
                        System.out.println(game.print());
                        char command = game.getFirst();
                        game.executeCommand(command);
                        if (game.isOver()) {
                            game.printRanking(userName);
                            break;
                        }
                    }
                    while (true) {
                        System.out.println(Words.aNewGame);
                        char reStartCommand = game.getFirst();
                        switch (reStartCommand) {
                            case ('y'):
                                game.score = 0;
                                continue a;
                            case ('n'):
                                System.exit(0);
                            default:
                                System.out.println(Words.commandError);
                                continue;
                        }
                    }
                case ('q'):
                    System.out.println(Words.quitGame);
                    System.exit(0);
                case ('l'):
                    System.out.println(Words.askName);
                    String username = game.getAll();
                    System.out.println("输入存档位置数字。");
                    int num = (int) (game.getFirst() - '0');
                    Game gamee = Game.getGame("Game", username, num);
                    System.out.println("读取成功，享受游戏吧！");
                    while (true) {
                        gamee.shadowBottom();
                        System.out.println(gamee.print());
                        gamee.executeCommand(gamee.getFirst());
                        if (gamee.isOver()) {
                            gamee.printRanking(username);
                            break;
                        }
                    }
                    while (true) {
                        System.out.println(Words.aNewGame);
                        char reStartCommand = game.getFirst();
                        switch (reStartCommand) {
                            case ('y'):
                                game.score = 0;
                                continue a;
                            case ('n'):
                                System.exit(0);
                            default:
                                System.out.println(Words.commandError);
                                continue;
                        }
                    }
            }
        }
    }
}

