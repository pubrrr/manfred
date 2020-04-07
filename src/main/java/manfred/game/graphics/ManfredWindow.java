package manfred.game.graphics;

import javax.swing.*;

public class ManfredWindow extends JFrame {
    public ManfredWindow() {
        super("Manfreds Apokalüpse");

        setUndecorated(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }
}