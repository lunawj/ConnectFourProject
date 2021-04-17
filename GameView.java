import javax.swing.*;

public class GameView {
    /* Main method for client side. Sets up GUI for the 
       Connect4 game and creates an instance of the controller
       used to run the clients side */
    //public static void main(final String[] args) {
    public GameController controller;

    public GameView(int player) {
        /* Create the JFrame, make it exit when close is pressed */
        JFrame frame = new JFrame("ConnectFour!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controller = new GameController(player);

        /* Add controller to JFrame created for program */
        frame.getContentPane().add(controller);
        frame.setResizable(true);

        /* Pack to good dimensions for game */
        frame.pack();

        /* Set the frame to visible so it can be interacted with */
        frame.setVisible(true);
    }
}
