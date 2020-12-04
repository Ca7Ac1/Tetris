package Game;

import javax.swing.JFrame;

import java.awt.EventQueue;

public class Main {
    
    public Main() {
        JFrame frame = new JFrame("Tetris");

        frame.add(new Board());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Main();
            }
        });
    }
}
