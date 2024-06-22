package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public Rectangle hitBox; // Tambahkan hit box

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        int hitBoxWidth = gp.tileSize - 20; // Mengurangi lebar hit box
        int hitBoxHeight = gp.tileSize - 20; // Mengurangi tinggi hit box
        hitBox = new Rectangle(0, 0, hitBoxWidth, hitBoxHeight); // Hit box lebih kecil dari tile

        setDefaultValues();
        getPlayerImage();
    }



    public void setDefaultValues() {
        worldX = gp.tileSize * 6;  // posisi awal pemain di dunia
        worldY = gp.tileSize * 4;  // posisi awal pemain di dunia
        speed = 2;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResource("/res/player/belakang/belakang.png"));
            up2 = ImageIO.read(getClass().getResource("/res/player/belakang/belakang1.png"));
            up3 = ImageIO.read(getClass().getResource("/res/player/belakang/belakang2.png"));
            down1 = ImageIO.read(getClass().getResource("/res/player/depan/depan.png"));
            down2 = ImageIO.read(getClass().getResource("/res/player/depan/depan1.png"));
            down3 = ImageIO.read(getClass().getResource("/res/player/depan/depan2.png"));
            left1 = ImageIO.read(getClass().getResource("/res/player/kiri/kiri.png"));
            left2 = ImageIO.read(getClass().getResource("/res/player/kiri/kiri1.png"));
            left3 = ImageIO.read(getClass().getResource("/res/player/kiri/kiri2.png"));
            right1 = ImageIO.read(getClass().getResource("/res/player/kanan/kanan.png"));
            right2 = ImageIO.read(getClass().getResource("/res/player/kanan/kanan1.png"));
            right3 = ImageIO.read(getClass().getResource("/res/player/kanan/kanan2.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            double newX = worldX;
            double newY = worldY;
            if (keyH.upPressed) {
                direction = "up";
                newY -= speed;
            }
            if (keyH.downPressed) {
                direction = "down";
                newY += speed;
            }
            if (keyH.leftPressed) {
                direction = "left";
                newX -= speed;
            }
            if (keyH.rightPressed) {
                direction = "right";
                newX += speed;
            }

            // Update hit box posisi baru dan pastikan tetap terpusat
            int hitBoxX = (int) newX + (gp.tileSize - hitBox.width) / 2;
            int hitBoxY = (int) newY + (gp.tileSize - hitBox.height) / 2;
            Rectangle newHitBox = new Rectangle(hitBoxX, hitBoxY, hitBox.width, hitBox.height);

            // Konversi koordinat dunia ke koordinat tile
            int col = (int) (newHitBox.getX() / gp.tileSize);
            int row = (int) (newHitBox.getY() / gp.tileSize);

            if (!gp.getTileM().isCollisionTile(col, row +1)) {
                worldX = newX;
                worldY = newY;
                hitBox.setLocation(hitBoxX, hitBoxY); // Update posisi hit box
            }

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : (spriteNum == 2) ? 3 : 1;
                spriteCounter = 0;
            }
        }
    }





    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case "up":
                image = (spriteNum == 1) ? up1 : (spriteNum == 2) ? up2 : up3;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : (spriteNum == 2) ? down2 : down3;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : (spriteNum == 2) ? left2 : left3;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : (spriteNum == 2) ? right2 : right3;
                break;
        }

        int playerScreenX = screenX;
        int playerScreenY = screenY;

        if (gp.player.worldX < gp.screenWidth / 2) {
            playerScreenX = (int) gp.player.worldX;
        } else if (gp.player.worldX > gp.maxWorldCol * gp.tileSize - gp.screenWidth / 2) {
            playerScreenX = (int) (gp.screenWidth - (gp.maxWorldCol * gp.tileSize - gp.player.worldX));
        }

        if (gp.player.worldY < gp.screenHeight / 2) {
            playerScreenY = (int) gp.player.worldY;
        } else if (gp.player.worldY > gp.maxWorldRow * gp.tileSize - gp.screenHeight / 2) {
            playerScreenY = (int) (gp.screenHeight - (gp.maxWorldRow * gp.tileSize - gp.player.worldY));
        }

        g2.drawImage(image, playerScreenX, playerScreenY, gp.tileSize, gp.tileSize, null);

        // Gambar hit box untuk debugging
        g2.setColor(Color.RED);
        int hitBoxDrawX = playerScreenX + (gp.tileSize - hitBox.width) / 2;
        int hitBoxDrawY = playerScreenY + (gp.tileSize - hitBox.height) / 2;
        g2.drawRect(hitBoxDrawX, hitBoxDrawY, hitBox.width, hitBox.height);
    }



}
