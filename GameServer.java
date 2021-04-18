import java.net.Socket;
import java.io.*;

public class GameServer extends Thread {
    private Socket socket1, socket2;

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

        /* setup the input and output streams to/from clients, and read from clients */
        try {
            outToClient1 = new DataOutputStream(socket1.getOutputStream());
            outToClient2 = new DataOutputStream(socket2.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inFromClient1 = new DataInputStream(new BufferedInputStream(socket1.getInputStream()));
            inFromClient2 = new DataInputStream(new BufferedInputStream(socket2.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outToClient1.writeUTF("1");
            outToClient2.writeUTF("2");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(activeGame) {
            try {
                player1Move = inFromClient1.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(player1Move.equals("Quit")) {
                break;
            }
            try {
                outToClient2.writeUTF(player1Move);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                player2Move = inFromClient2.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(player2Move.equals("Quit")) {
                break;
            }
            try {
                outToClient1.writeUTF(player2Move);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
