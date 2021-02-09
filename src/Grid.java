//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.Serial;

public class Grid extends JPanel implements ActionListener, MouseListener, KeyListener {
    @Serial
    private static final long serialVersionUID = 1L;
    public static final Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WIDTH = (int) SCREEN.getWidth(), HEIGHT = (int) SCREEN.getHeight();
    private final boolean[][] out;
    private final double[][] real;
    private final double[][] img;
    private double X_CENTER = 0.0D;
    private double Y_CENTER = 0.0D;
    private final JButton picture;
    private long SCALE = (long) WIDTH/4;
    private final int cycle_power = 12;
    private final int cycles = (int)Math.pow(2.0D, cycle_power);
    private int count;
    private int numCalc;
    private final int[] colormap;
    private final BufferedImage screen;
    private final Timer time;
    private final JTextField xPos;
    private final JTextField yPos;
    private final JTextField scale;
    private final Driver frame;

    public Grid(Driver frame) {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        System.out.println(WIDTH + "  " + HEIGHT);
        this.frame = frame;
        this.real = new double[WIDTH][HEIGHT];
        this.img = new double[WIDTH][HEIGHT];
        this.out = new boolean[WIDTH][HEIGHT];
        Font f = new Font("Arial", Font.PLAIN, WIDTH / 80);
        this.xPos = new JTextField(6);
        this.xPos.addActionListener(this);
        this.xPos.setText("" + this.X_CENTER);
        this.xPos.setFont(f);
        this.yPos = new JTextField(6);
        this.yPos.addActionListener(this);
        this.yPos.setText("" + this.Y_CENTER);
        this.yPos.setFont(f);
        this.scale = new JTextField(6);
        this.scale.addActionListener(this);
        this.scale.setText("" + this.SCALE);
        this.scale.setFont(f);
        this.picture = new JButton("Pic");
        this.picture.addActionListener(this);
        this.setNumCalc(0);
        this.add(this.xPos);
        this.add(this.yPos);
        this.add(this.scale);
        this.add(this.picture);

        for(int x = 0; x < WIDTH; ++x) {
            for(int y = 0; y < HEIGHT; ++y) {
                this.real[x][y] = 0.0D;
                this.img[x][y] = 0.0D;
                this.out[x][y] = false;
            }
        }

        frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        this.screen = new BufferedImage(WIDTH, HEIGHT, 1);
        this.count = 0;
        this.time = new Timer(1, this);
        this.time.start();
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.colormap = this.getColor();
    }

    public int[] getColor() {
        int[] cm = new int[256];

        for(int i = 0; i < 256; ++i) {
            int r;
            int g;
            int b;
            if (i < 85) {
                r = 0;
                b = 85 - i;
                g = i;
            } else if (i < 170) {
                r = i - 85;
                b = 0;
                g = 170 - i;
            } else {
                g = 0;
                b = i - 170;
                r = 256 - i;
            }

            cm[i] = 2 * ((r << 16) + (g << 8) + b) + 5460819;
        }

        return cm;
    }

    public boolean iterate(int x, int y) {
        double c_real = this.X_CENTER - (double)(WIDTH - 2 * x) / (double)(2L * this.SCALE);
        double c_img = this.Y_CENTER - (double)(HEIGHT - 2 * y) / (double)(2L * this.SCALE);
        double temp = 2.0D * this.real[x][y] * this.img[x][y] + c_img;
        double square_real = this.real[x][y] * this.real[x][y];
        double square_img = this.img[x][y] * this.img[x][y];
        this.real[x][y] = square_real - square_img + c_real;
        this.img[x][y] = temp;
        if (square_real + square_img < 4.0D) {
            return false;
        } else {
            this.out[x][y] = true;
            this.setNumCalc(this.getNumCalc() + 1);
            return true;
        }
    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        Graphics2D page2D = (Graphics2D)page;
        int rgb = this.colormap[this.count % 256];

        for(int x = 0; x < WIDTH; ++x) {
            for(int y = 0; y < HEIGHT; ++y) {
                if (!this.out[x][y] && this.iterate(x, y)) {
                    this.screen.setRGB(x, y, rgb);
                }
            }
        }

        page2D.drawImage(this.screen, null, null);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == this.time) {
            if (this.count < this.cycles) {
                this.repaint();
                ++this.count;
            } else {
                this.time.stop();
                this.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        } else if (o != this.picture) {
            this.X_CENTER = Double.parseDouble(this.xPos.getText());
            this.Y_CENTER = Double.parseDouble(this.yPos.getText());
            this.SCALE = Long.parseLong(this.scale.getText());
            this.count = 0;

            for(int x = 0; x < WIDTH; ++x) {
                for(int y = 0; y < HEIGHT; ++y) {
                    this.real[x][y] = 0.0D;
                    this.img[x][y] = 0.0D;
                    this.out[x][y] = false;
                    this.screen.setRGB(x, y, 0);
                }
            }

            this.frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            this.time.start();
        }

    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        this.xPos.setText("" + (this.X_CENTER - (double)(WIDTH - 2 * x) / (double)(2L * this.SCALE)));
        this.yPos.setText("" + (this.Y_CENTER - (double)(HEIGHT - 2 * y) / (double)(2L * this.SCALE)));
        this.scale.setText("" + this.SCALE * 10L);
        this.X_CENTER = Double.parseDouble(this.xPos.getText());
        this.Y_CENTER = Double.parseDouble(this.yPos.getText());
        this.SCALE *= 10L;
        this.count = 0;

        for(int x1 = 0; x1 < WIDTH; ++x1) {
            for(int y1 = 0; y1 < HEIGHT; ++y1) {
                this.real[x1][y1] = 0.0D;
                this.img[x1][y1] = 0.0D;
                this.out[x1][y1] = false;
                this.screen.setRGB(x1, y1, 0);
            }
        }

        this.frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        this.time.start();
        System.out.println(this.X_CENTER - (double)(WIDTH - 2 * x) / (double)(2L * this.SCALE));
        System.out.println(this.Y_CENTER - (double)(HEIGHT - 2 * y) / (double)(2L * this.SCALE));
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        int x;
        int y;
        if (e.getKeyCode() == 521) {
            this.scale.setText("" + this.SCALE * 10L);
            this.X_CENTER = Double.parseDouble(this.xPos.getText());
            this.Y_CENTER = Double.parseDouble(this.yPos.getText());
            this.SCALE *= 10L;
            this.count = 0;

            for(x = 0; x < WIDTH; ++x) {
                for(y = 0; y < HEIGHT; ++y) {
                    this.real[x][y] = 0.0D;
                    this.img[x][y] = 0.0D;
                    this.out[x][y] = false;
                    this.screen.setRGB(x, y, 0);
                }
            }

            this.time.start();
        } else if (e.getKeyCode() == 45) {
            this.scale.setText("" + (double)this.SCALE * 0.1D);
            this.X_CENTER = Double.parseDouble(this.xPos.getText());
            this.Y_CENTER = Double.parseDouble(this.yPos.getText());
            this.SCALE = (long)((double)this.SCALE * 0.1D);
            this.count = 0;

            for(x = 0; x < WIDTH; ++x) {
                for(y = 0; y < HEIGHT; ++y) {
                    this.real[x][y] = 0.0D;
                    this.img[x][y] = 0.0D;
                    this.out[x][y] = false;
                    this.screen.setRGB(x, y, 0);
                }
            }

            this.time.start();
        }

    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public int getNumCalc() {
        return this.numCalc;
    }

    public void setNumCalc(int numCalc) {
        this.numCalc = numCalc;
    }
}
