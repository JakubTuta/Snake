import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 700;
    static final int UNITS = 20;
    static final int UNIT_SIZE = SCREEN_HEIGHT / UNITS;
    static final int DELAY = 30;
    int bodyParts = 6;
    List<Pair<Integer, Integer>> coords = new ArrayList<>();
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    boolean gay = false;
    boolean przechodzenie = true;
    Timer timer;
    Random random;

    GamePanel() {
        for (int i = 0; i < bodyParts; i++) {
            coords.add(new Pair<>(bodyParts - i, UNITS / 2));
        }

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (!running) {
            gameOver(g);
            return;
        }

        g.setColor(Color.red);
        g.fillOval(appleX * UNIT_SIZE, appleY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

        g.setColor(Color.green);
        g.fillRect(coords.get(0).getX() * UNIT_SIZE, coords.get(0).getY() * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        for (int i = 1; i < bodyParts; i++) {
            if (gay)
                g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            else
                g.setColor(new Color(45, 180, 0));
            g.fillRect(coords.get(i).getX() * UNIT_SIZE, coords.get(i).getY() * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    public void newApple() {
        boolean good;
        do {
            good = false;
            appleX = random.nextInt(UNITS);
            appleY = random.nextInt(UNITS);
            for (int i = 0; i < bodyParts; i++) {
                if (appleX == coords.get(i).getX() && appleY == coords.get(i).getY()) {
                    good = true;
                    break;
                }
            }
        } while (good);
    }

    public void move() {
        for (int i = bodyParts - 1; i > 0; i--) {
            coords.get(i).setX(coords.get(i - 1).getX());
            coords.get(i).setY(coords.get(i - 1).getY());
        }

        switch (direction) {
            case 'U' -> coords.get(0).setY(coords.get(0).getY() - 1);
            case 'D' -> coords.get(0).setY(coords.get(0).getY() + 1);
            case 'L' -> coords.get(0).setX(coords.get(0).getX() - 1);
            case 'R' -> coords.get(0).setX(coords.get(0).getX() + 1);
        }
    }

    public void checkCollisions() {
        // cialo
        for (int i = 2; i < bodyParts; i++) {
            if ((coords.get(0).getX() == coords.get(i).getX()) && (coords.get(0).getY() == coords.get(i).getY())) {
                running = false;
                break;
            }
        }

        // jablko
        if ((coords.get(0).getX() == appleX) && (coords.get(0).getY() == appleY)) {
            newApple();
            coords.add(new Pair<>(coords.get(bodyParts-1).getX(), coords.get(bodyParts-1).getY()));
            bodyParts++;
            applesEaten++;
        }

        if (!przechodzenie) {
            // lewo
            if (coords.get(0).getX() < 0)
                running = false;
                // prawo
            else if (coords.get(0).getX() >= UNITS)
                running = false;
                // gora
            else if (coords.get(0).getY() < 0)
                running = false;
                // dol
            else if (coords.get(0).getY() >= UNITS)
                running = false;
        } else {
            // lewo
            if (coords.get(0).getX() < 0)
                coords.get(0).setX(UNITS);
                // prawo
            else if (coords.get(0).getX() >= UNITS)
                coords.get(0).setX(0);
                // gora
            else if (coords.get(0).getY() < 0)
                coords.get(0).setY(UNITS);
                // dol
            else if (coords.get(0).getY() >= UNITS)
                coords.get(0).setY(0);
        }

        if (!running)
            timer.stop();
    }

    public void gameOver(Graphics g) {
        // Game over
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            repaint();
            move();
            checkCollisions();
        }
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> {
                    if (direction != 'R')
                        direction = 'L';
                }
                case KeyEvent.VK_D -> {
                    if (direction != 'L')
                        direction = 'R';
                }
                case KeyEvent.VK_S -> {
                    if (direction != 'U')
                        direction = 'D';
                }
                case KeyEvent.VK_W -> {
                    if (direction != 'D')
                        direction = 'U';
                }
            }
        }
    }
}