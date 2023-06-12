import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Player implements KeyListener, ActionListener {
    public static int worldXPos, worldYPos;
    public static final int screenX = Panel.SCREEN_LENGTH / 2 - (Panel.TILE_SIZE), screenY = Panel.SCREEN_WIDTH / 2 - (Panel.TILE_SIZE);
    private boolean upPressed, downPressed, leftPressed, rightPressed;

    private BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    private int playerSpeed;
    private int spriteNum;
    private String direction;
    private int spriteCounter;
    private boolean isColliding;
    private boolean hasAxe;
    private CollisionManager cm;
    private int temp;
    private Rectangle hitBox;
    private boolean hasShovel;
    private Timer time;
    private int seconds;
    private boolean showStats;
    private int logs;
    private int score;
    private boolean gotItem;
    private int mostRecent;
    private boolean gameEnd;
    private boolean gameBegun;
    public Player()  {
        gameBegun = false;
        gameEnd = false;
        mostRecent = 0;
        gotItem = false;
        score = 0;
        seconds = 100;
        showStats = false;
        time = new Timer(1000, null);
        time.addActionListener(this);
        time.start();
        hasShovel = false;
        hasAxe = false;
        cm = new CollisionManager();
        isColliding = false;
        spriteCounter = 0;
        spriteNum = 1;
        direction = "down";
        playerSpeed = 2;
        worldXPos = 1152;
        worldYPos = 1152;
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        hitBox = new Rectangle(worldXPos, worldYPos, 23, 24);
        try {
            getPlayerImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getXPos() {
        return worldXPos;
    }

    public double getYPos() {
        return worldYPos;
    }
    public int getPlayerSpeed() {
        return playerSpeed;
    }
    public boolean getGameEnd() {
        return gameEnd;
    }

    public String getDirection() {
        return direction;
    }
    public boolean getUpPressed() {
        return upPressed;
    }

    public boolean getDownPressed() {
        return downPressed;
    }
    public boolean getRightPressed() {
        return rightPressed;
    }
    public boolean getLeftPressed() {
        return leftPressed;
    }

    public void changeYPos(double a) {
        worldYPos += a;
    }
    public void changeXPos(double a) {
        worldXPos += a;
    }
    public boolean isGameEnd() {
        return gameEnd;
    }
    public int getScore() {
        return score;
    }
    public int getLogs() {
        return logs;
    }

    public void getPlayerImage() throws IOException {
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaFront1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaFront2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaFront3.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaBack1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaBack2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaBack3.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaLeft1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaLeft2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaLeft3.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaRight1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaRight2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/Sprites/KoalaRight3.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setIsColliding(boolean b) {
        isColliding = b;
    }

    public BufferedImage getImage() {
        if (spriteNum == 1) {
            if (direction.equals("down")) {
                return down1;
            }
            if (direction.equals("up")) {
                return up1;
            }
            if (direction.equals("left")) {
                return left1;
            }
            if (direction.equals("right")) {
                return right1;
            }
        } else if (spriteNum == 2){
            if (direction.equals("down")) {
                return down2;
            }
            if (direction.equals("up")) {
                return up2;
            }
            if (direction.equals("left")) {
                return left2;
            }
            if (direction.equals("right")) {
                return right2;
            }
        } else {
            if (direction.equals("down")) {
                return down3;
            }
            if (direction.equals("up")) {
                return up3;
            }
            if (direction.equals("left")) {
                return left3;
            }
            if (direction.equals("right")) {
                return right3;
            }
        }
        return up1;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int temp = e.getKeyCode();
        if (temp == KeyEvent.VK_I){
            gameBegun = !gameBegun;
        }
        if (temp == KeyEvent.VK_ENTER) {
            gameBegun = true;
        }
        if (temp == KeyEvent.VK_W || temp == KeyEvent.VK_UP && !showStats && gameBegun) {
            upPressed = true;
            direction = "up";
        } else if (temp == KeyEvent.VK_A || temp == KeyEvent.VK_LEFT&& !showStats) {
            leftPressed = true;
            direction = "left";
        } else if (temp == KeyEvent.VK_S || temp == KeyEvent.VK_DOWN&& !showStats) {
            downPressed = true;
            direction = "down";
        } else if (temp == KeyEvent.VK_D || temp == KeyEvent.VK_RIGHT&& !showStats) {
            rightPressed = true;
            direction = "right";
        }
        if (temp == KeyEvent.VK_G) {
            showStats = true;
        }
        if (temp == KeyEvent.VK_Q && hasAxe) {
            try {
                chop();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (temp == KeyEvent.VK_E && hasShovel) {
            try {
                shovel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public void shovel() throws IOException{
            int row = (worldXPos + (Panel.TILE_SIZE / 2))/ Panel.TILE_SIZE;
            int col = (worldYPos + (Panel.TILE_SIZE / 2))/ Panel.TILE_SIZE;
            if (TileManager.tiles[row][col].getName().equals("suspiciousSand")) {
                TileManager.tiles[row][col] = new Tile("hole",ImageIO.read(getClass().getResourceAsStream("/Terrain/Hole.png")), false, 0, 0);
                gotItem = true;
                int rand = (int) (Math.random() * 5) + 1;
                if (rand == 1) {
                    mostRecent = 1;
                    score+= 800;
                } else {
                    mostRecent = 2;
                    score+= 100;
                }
            }
    }


    public void chop() throws IOException {
            int row = (worldXPos + (Panel.TILE_SIZE / 2))/ Panel.TILE_SIZE;
            int col = (worldYPos + (Panel.TILE_SIZE / 2))/ Panel.TILE_SIZE;
            if (direction.equals("down")) {
                col += 1;
            } else if (direction.equals("right")) {
                row += 1;
            } else if (direction.equals("left")) {
                row -= 1;
            } else if (direction.equals("up")) {
                col -= 1;
            }
            if (TileManager.tiles[row][col].getName().equals("palmTree")) {
                TileManager.tiles[row][col] = new Tile("cutTree",ImageIO.read(getClass().getResourceAsStream("/Terrain/Cut_Tree.png")), true, 0, 0);
                mostRecent = 3;
                if (logs < 99) {
                    logs++;
                }
            } else if (TileManager.tiles[row][col].getName().equals("cutTree")) {
                TileManager.tiles[row][col] = new Tile("sand1", ImageIO.read(getClass().getResourceAsStream("/Terrain/Sand.png")), false, 0, 0);
                mostRecent = 3;
            }
    }

    public void update() throws IOException {
        boolean moving;
        int row = (worldXPos + (Panel.TILE_SIZE / 2))/ Panel.TILE_SIZE;
        int col = (worldYPos + (Panel.TILE_SIZE / 2))/ Panel.TILE_SIZE;
        if (TileManager.tiles[row][col].getName().equals("shovel")){
            TileManager.tiles[row][col] = new Tile("sand1", ImageIO.read(getClass().getResourceAsStream("/Terrain/Sand.png")), false, 0, 0);
            hasShovel = true;
        }
        if (TileManager.tiles[row][col].getName().equals("axe")) {
            TileManager.tiles[row][col] = new Tile("sand1", ImageIO.read(getClass().getResourceAsStream("/Terrain/Sand.png")), false, 0, 0);
            hasAxe = true;
        }
        if (TileManager.tiles[row][col].getName().equals("hole")) {
            playerSpeed = 1;
        } else {
            playerSpeed = 2;
        }


        if (upPressed || downPressed || rightPressed || leftPressed && gameBegun && !gameEnd) {
            isColliding = false;
            cm.checkCollision(this);
            if (!isColliding) {
                if (upPressed && !leftPressed && !downPressed && !rightPressed&& !showStats) {
                        changeYPos(-1 * getPlayerSpeed());
                        moving = true;
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (!upPressed && leftPressed && !downPressed && !rightPressed&& !showStats) {
                        changeXPos(-1 * getPlayerSpeed());
                        moving = true;
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (!upPressed && !leftPressed && downPressed && !rightPressed && !showStats) {
                        changeYPos(getPlayerSpeed());
                        moving = true;
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (!upPressed && !leftPressed && !downPressed && rightPressed&& !showStats) {
                        changeXPos(getPlayerSpeed());
                        moving = true;
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    moving = false;
                }
                if (moving) {
                    spriteCounter++;
                    if (spriteCounter > 15) {
                        if (spriteNum == 1) {
                            spriteNum = 2;
                        } else if (spriteNum == 2) {
                            spriteNum = 3;
                        } else {
                            spriteNum = 1;
                        }
                        spriteCounter = 0;
                    }
                }
            }
        }

        hitBox.setLocation (screenX + 13,  screenY + 24);
    }
    @Override
    public void keyReleased(KeyEvent e) {
        temp = e.getKeyCode();
        if (temp == KeyEvent.VK_W || temp == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (temp == KeyEvent.VK_A || temp == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (temp == KeyEvent.VK_S || temp == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (temp == KeyEvent.VK_D || temp == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (temp == KeyEvent.VK_G) {
            showStats = false;
        }
    }

    public void draw(Graphics2D g2) throws IOException {
        if (!gameBegun) {
            g2.drawImage(ImageIO.read(getClass().getResourceAsStream("/Text/TitleScreen.png")), 0, 0, 768,750, null);
        } else if (!gameEnd) {
            BufferedImage image = getImage();
            g2.drawImage(image, screenX, screenY, 48, 48, null);
            if (showStats) {
                BufferedImage s = ImageIO.read(getClass().getResourceAsStream("/Text/Score.png"));
                BufferedImage t = ImageIO.read(getClass().getResourceAsStream("/Text/Time.png"));
                g2.drawImage(s, screenX - 25, screenY + Panel.TILE_SIZE + 10, 58, 20, null);
                g2.drawImage(t, screenX - 25, screenY + Panel.TILE_SIZE + 40, 48, 20, null);
                int temp = seconds;
                int offset = 0;
                for (int i = (seconds + "").length(); i > 0; i--) {
                    if ((seconds + "").length() != 1) {
                        temp /= (Math.pow(10, i - 1));
                        BufferedImage bi = getNumber(temp);
                        offset += 19;
                        temp = (int) (seconds % (Math.pow(10, i - 1)));
                        g2.drawImage(bi, screenX + 10 + offset, screenY + Panel.TILE_SIZE + 38, 24, 24, null);
                    } else {
                        BufferedImage bi = getNumber(temp);
                        g2.drawImage(bi, screenX + 29 + offset, screenY + Panel.TILE_SIZE + 38, 24, 24, null);
                    }
                }
                int temp2 = score;
                int offset2 = 0;
                for (int i = (score + "").length(); i > 0; i--) {
                    if ((score + "").length() != 1) {
                        temp2 /= (Math.pow(10, i - 1));
                        BufferedImage bi = getNumber(temp2);
                        offset2 += 19;
                        temp2 = (int) (score % (Math.pow(10, i - 1)));
                        g2.drawImage(bi, screenX + 16 + offset2, screenY + Panel.TILE_SIZE + 8, 24, 24, null);
                    } else {
                        if ((score + "").length() == 1) {
                            BufferedImage bi = getNumber(temp2);
                            g2.drawImage(bi, screenX + 35 + offset2, screenY + Panel.TILE_SIZE + 8, 24, 24, null);
                        }
                    }
                }
                int temp3 = logs;
                int offset3 = 0;
                for (int i = (logs + "").length(); i > 0; i--) {
                    if ((logs + "").length() != 1) {
                        g2.drawImage(ImageIO.read(getClass().getResourceAsStream("/Text/Logs.png")), 550, 694, 77, 30, null);
                        temp3 /= (Math.pow(10, i - 1));
                        BufferedImage bi = getNumber(temp3);
                        offset3 += 19;
                        temp3 = (int) (logs % (Math.pow(10, i - 1)));
                        g2.drawImage(bi, 600 + offset3, 694, 30, 30, null);
                    } else {
                        g2.drawImage(ImageIO.read(getClass().getResourceAsStream("/Text/Logs.png")), 560, 694, 77, 30, null);
                        if ((logs + "").length() == 1) {
                            BufferedImage bi = getNumber(temp3);
                            g2.drawImage(bi, 640 + offset3, 694, 30, 30, null);
                        }
                    }
                    g2.drawImage(ImageIO.read(getClass().getResourceAsStream("/Text/Forward_Slash.png")), 670, 694, 30, 30, null);
                    g2.drawImage(ImageIO.read(getClass().getResourceAsStream("/Text/Number_One.png")), 695, 694, 30, 30, null);
                    g2.drawImage(ImageIO.read(getClass().getResourceAsStream("/Text/Number_Six.png")), 719, 694, 30, 30, null);
                }
            }


            if (gotItem) {
                BufferedImage bi;
                int offset = 0;
                if (mostRecent == 1) {
                    bi = ImageIO.read(getClass().getResourceAsStream("/Sprites/Diamond.png"));
                    offset = -3;
                } else if (mostRecent == 2) {
                    bi = ImageIO.read(getClass().getResourceAsStream("/Sprites/Amethyst.png"));
                } else {
                    bi = ImageIO.read(getClass().getResourceAsStream("/Sprites/Axe.png"));
                }
                g2.drawImage(bi, screenX + offset, screenY - Panel.TILE_SIZE / 2 - 25, 48, 48, null);
            }
        }
    }
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(seconds == 0) {
            gameEnd = true;
        }
        if (o instanceof Timer && seconds > 0 && gameBegun) {
            seconds--;
        }
    }

    public BufferedImage getNumber(int temp) throws IOException {
        BufferedImage bi;
        if (temp == 0) {
            bi = ImageIO.read(getClass().getResourceAsStream("/Text/Number_Zero.png"));
        } else if (temp == 1) {
            bi = ImageIO.read(getClass().getResourceAsStream("/Text/Number_One.png"));
        } else if (temp == 2) {
            bi = ImageIO.read(getClass().getResourceAsStream("/Text/Number_Two.png"));
        } else if (temp == 3) {
            bi = ImageIO.read(getClass().getResourceAsStream("/Text/Number_Three.png"));
        } else if (temp == 4) {
            bi = ImageIO.read(getClass().getResourceAsStream("/Text/Number_Four.png"));
        } else if (temp == 5) {
            bi = ImageIO.read(getClass().getResourceAsStream("/Text/Number_Five.png"));
        } else if (temp == 6) {
            bi = ImageIO.read(getClass().getResourceAsStream("/Text/Number_Six.png"));
        } else if (temp == 7) {
            bi = ImageIO.read(getClass().getResourceAsStream("/Text/Number_Seven.png"));
        } else if (temp == 8) {
            bi = ImageIO.read(getClass().getResourceAsStream("/Text/Number_Eight.png"));
        } else {
            bi = ImageIO.read(getClass().getResourceAsStream("/Text/Number_Nine.png"));
        }
        return bi;
    }

}
