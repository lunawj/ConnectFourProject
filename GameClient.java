/*************************************************************************
 * Course: CIS457-10
 * File:   Semester Project
 * Name:   Charlie Dorn, Kendra Haan, Wesley Luna, Justin Von Kulajta Winn
 * Date:   4/19/2021
 *
 * Description: The game client for the Connect4 game. Initializes the game
 * view, sends player moves to the sever and reads opponent moves from the
 * server.
 **************************************************************************/
import java.net.*;
import java.io.*;
import java.lang.*;

public class GameClient {
    /** Keeps track of which player we are (1 or 2) */
    private static int player;

    public static void main(String args[]) throws IOException {
        /* instance of the game */
        GameView game;
        String gameInfo = null;
        /* the socket we use to communicate to the server */
        Socket gameSocket = new Socket(args[0], Integer.parseInt(args[1]));
        /* activeGame is set to false upon end of game */
        boolean activeGame = true;
        int move;

        /* Setup input and output streams */
        DataOutputStream outToServer = new DataOutputStream(gameSocket.getOutputStream());
        DataInputStream inFromServer = new DataInputStream(
                new BufferedInputStream(gameSocket.getInputStream()));

        /* Read from the server to figure out who we are */
        try {
            gameInfo = inFromServer.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* parse data from server and set player value */
        player = gameInfo.equals("1") ? 1 : 2;
        System.out.println("I am player " + player);
        /* instantiate instance of GameView */
        game = new GameView(player);

        /* We repeat as long as game is still active */
        while(activeGame) {
            /* If it's our turn, and a new move has been made... */
            if(game.controller.getCurrentTurn() == player && game.controller.newMove()) {
                /* We get the move that was made from the game controller */
                move = game.controller.getMove();
                System.out.println("Place chip in col: " + move);
                /* We communicate the move to the server */
                outToServer.writeUTF(Integer.toString(move));
                /* We switch turns (now its the other persons turn */
                game.controller.switchTurn(player);
                /* Make sure we don't double count the move */
                game.controller.clearNewMove();
            }
            /* If it's not our turn... */
            else if (game.controller.getCurrentTurn() != player){
                System.out.println("Waiting for other player to move");
                /* We wait for input from the server containing the other 
                   players move */
                try {
                    gameInfo = inFromServer.readUTF();
                /* IOException caused when other client has ended... so we end here */ 
                } catch (IOException e) {   // end client on IO exception
                    break;
                }
                /* When other player exits, "Quit" will be relayed from quitting client
                   to server to us */
                if(gameInfo.equals("Quit")) {
                    break;
                }
                /* We need to make the other clients move to reflect it on our game */
                game.controller.setChipFromServer(Integer.parseInt(gameInfo));
            }
            /* If the game is finished or the window was closed, the game is not active */
            if(game.controller.gameFinished() || game.windowClosed()) {
                activeGame = false;
            }
        }
        /* Once the game is no longer active, we send "Quit" to the server,
           We close our socket, print to the console, and exit */
        outToServer.writeUTF("Quit");
        gameSocket.close();
        System.out.println("Player has closed the game");
        System.exit(0);
    }

}
