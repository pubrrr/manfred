package manfred.game.graphics;

import javax.swing.*;

public class ManfredWindow extends JFrame {
    public ManfredWindow(GamePanel panel) {
        super("Manfreds Apokalüpse");

        add(panel);

        setUndecorated(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
}