import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Launcher {

    static int frameWidth = 800;
    static int frameHeight = 600;
    static int worldSize = 20;
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Renderer");
        Renderer panel = new Renderer(frameWidth, frameHeight, worldSize);
        frame.add(panel);
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // escape key closes the window
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exit");
        frame.getRootPane().getActionMap().put("exit", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
        });
        frame.setVisible(true);
        
        
    }
}