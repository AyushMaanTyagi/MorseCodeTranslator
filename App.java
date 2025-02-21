import javax.swing.*;

public class App {
    public static void main(String[] args) {
//invoke later ensures that the GUI is created and updated in a thread safe manner

    SwingUtilities.invokeLater(()->new Morse().setVisible(true));
    }
}
