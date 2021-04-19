/*************************************************************************
 * Course: CIS457-10
 * File:   Semester Project
 * Name:   Charlie Dorn, Kendra Haan, Wesley Luna, Justin Von Kulajta Winn
 * Date:   4/19/2021
 *
 * Description: The game server class for the Connect4 game. Facilitates
 * the connection between two game clients, player 1 and player 2.
 **************************************************************************/
import java.net.Socket;
import java.io.*;

public class GameServer extends Thread {
    /* Create a socket for each client */
    private Socket socket1, socket2;

    /* Initialize both client sockets */
    public GameServer(Socket p1, Socket p2) {
        this.socket1 = p1;
        this.socket2 = p2;
    }

    public void run() {
        String player1Move = null;
        String player2Move = null;
        DataInputStream inFromClient1 = null;
        DataInputStream inFromClient2 = null;
        DataOutputStream  outToClient1 = null;
        DataOutputStream  outToClient2 = null;
        boolean activeGame = true;

        /* setup the output streams to clients */
        try {
            outToClient1 = new DataOutputStream(socket1.getOutputStream());
            outToClient2 = new DataOutputStream(socket2.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* Setup the input streams from clients */
        try {
            inFromClient1 = new DataInputStream(new BufferedInputStream(socket1.getInputStream()));
            inFromClient2 = new DataInputStream(new BufferedInputStream(socket2.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* write to each client letting them know who they are */
        try {
            outToClient1.writeUTF("1");
            outToClient2.writeUTF("2");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Keep on repeating as long as the game is still active */
        while(activeGame) {
            /* First we read from player 1 to get their move */
            try {
                player1Move = inFromClient1.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /* If player 1 entered "Quit", we break */
            if(player1Move.equals("Quit")) {
                break;
            }
            /* Then we write player 1's move to player 2 to update their game */
            try {
                outToClient2.writeUTF(player1Move);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /* Next, we read from player 2 to get their move */
            try {
                player2Move = inFromClient2.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /* If player 2 entered "Quit", we break */
            if(player2Move.equals("Quit")) {
                break;
            }
            /* Then we write player 2's move to player 1 to update their game */
            try {
                outToClient1.writeUTF(player2Move);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /* If we're here, we must have received "Quit"!... we gotta close sockets! */
        try {
            socket1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
