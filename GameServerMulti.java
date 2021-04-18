import java.net.*;
import java.io.*;
import java.lang.*;

public class GameServerMulti {
    /* Handles multi-threading the server */
    public static void main(String[] args) throws IOException {
        ServerSocket server = null;
        GameServer game;
        Socket p1, p2;
        int port = Integer.parseInt(args[0]);
        /* Create our serverSocket at the specified port */
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
        while (true) {
            // wait for two players to join the game
            System.out.println("Waiting for players to connect");
            p1 = server.accept();
            System.out.println("Player 1 connected " + p1.getInetAddress());
            p2 = server.accept();
            System.out.println("Player 2 connected " + p2.getInetAddress());
            System.out.println("New game has begun. Good luck!");
            /* Create GameServer instance and spawn it in it's own thread */
            game = new GameServer(p1, p2);
            Thread t = new Thread(game);
            t.start();
        }
    }
}

