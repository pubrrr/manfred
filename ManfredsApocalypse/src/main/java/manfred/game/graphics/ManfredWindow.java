package manfred.game.graphics;

import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
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