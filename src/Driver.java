//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javax.swing.JFrame;

public class Driver extends JFrame {
    public static void main(String[] args) {
        new Driver("Mandelbrot");
    }

    public Driver(String title) {
        super(title);
        this.getContentPane().add(new Grid(this));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }
}
