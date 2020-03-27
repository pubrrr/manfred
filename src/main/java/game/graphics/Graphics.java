package game.graphics;

import javax.swing.*;

public class Graphics {
    private JFrame window;

    public void init(){
        window = new JFrame("Manfreds Apokalüpse");
        window.setUndecorated(true);
        window.setVisible(true);
        window.setResizable(true);
        window.setSize(1200, 800);
        window.add(new GraphicsPanel());
    }

    public void paint() {
        window.repaint();
    }
}
