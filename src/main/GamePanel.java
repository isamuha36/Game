package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalSize = 20; // 20x20 tile
    final int scale = 3; // 3x scale

    public final int tileSize = originalSize * scale; // 60x60 pixels
    public final int maxScreenCol = 24; // 24 tiles
    public final int maxScreenRow = 12; // 12 tiles
    public final int screenWidth = tileSize * maxScreenCol; // 1440 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 720 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
//    public final int worldWidth = tileSize*maxWorldCol;
//    public final int worldHeight = tileSize*maxWorldRow;

    // FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    public Player player = new Player(this, keyH);

    // Set player's default position
//    int playerX = 100;
//    int playerY = 100;
//    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; // 16.6666666667 milliseconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

        public void update () {
            player.update();
        }

        @Override
        public void paintComponent (Graphics g){
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            tileM.draw(g2);

            player.draw(g2);

            g2.dispose();
        }

    public TileManager getTileM() {
        return tileM;
    }
}
