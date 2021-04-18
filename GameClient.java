import java.net.*;
import java.io.*;
import java.lang.*;

public class GameClient {

    private static int player;

    public static void main(String args[]) throws IOException {
        GameView game;
        String gameInfo = null;
        Socket gameSocket = new Socket(args[0], Integer.parseInt(args[1]));
        boolean activeGame = true;
        int move;

        DataOutputStream outToServer = new DataOutputStream(gameSocket.getOutputStream());
        DataInputStream inFromServer = new DataInputStream(
                new BufferedInputStream(gameSocket.getInputStream()));

        try {
            gameInfo = inFromServer.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player = gameInfo.equals("1") ? 1 : 2;
        System.out.println("I am player " + player);
        game = new GameView(player);

        while(activeGame) {
            if(game.controller.getCurrentTurn() == player && game.controller.newMove()) {
                move = game.controller.getMove();
                System.out.println("Place chip in col: " + move);
                outToServer.writeUTF(Integer.toString(move));
                game.controller.switchTurn(player);
                game.controller.clearNewMove();
            }
            else if (game.controller.getCurrentTurn() != player){
                System.out.println("Waiting for other player to move");
                try {
                    gameInfo = inFromServer.readUTF();
                } catch (IOException e) {   // end client on IO exception
                    break;
                }
                // handle condition when other player exits game
                if(gameInfo.equals("Quit")) {
                    break;
                }
                game.controller.setChipFromServer(Integer.parseInt(gameInfo));
            }
            if(game.controller.gameFinished() || game.windowClosed()) {
                activeGame = false;
            }
        }
        outToServer.writeUTF("Quit");
        gameSocket.close();
        System.out.println("Player has closed the game");
        System.exit(0);
    }

}
