import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Player implements KeyListener {
    private int xPos, yPos;
    private boolean upPressed, downPressed, leftPressed, rightPressed;

    private BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    private int playerSpeed;
    private int spriteNum;
    private String direction;
    private int spriteCounter;
    private boolean isColliding;
    private CollisionManager cm;
    private int temp;
    private Rectangle hitBox;
    public Player()  {
        cm = new CollisionManager();
        isColliding = false;
        spriteCounter = 0;
        spriteNum = 1;
        direction = "down";
        playerSpeed = 2;
        xPos = 100;
        yPos = 100;
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        hitBox = new Rectangle(xPos, yPos , 23, 24);
        try {
            getPlayerImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    public int getPlayerSpeed() {
        return playerSpeed;
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
        yPos += a;
    }
    public void changeXPos(double a) {
        xPos += a;
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
        if (temp == KeyEvent.VK_W || temp == KeyEvent.VK_UP) {
            upPressed = true;
            direction = "up";
        } else if (temp == KeyEvent.VK_A || temp == KeyEvent.VK_LEFT) {
            leftPressed = true;
            direction = "left";
        } else if (temp == KeyEvent.VK_S || temp == KeyEvent.VK_DOWN) {
            downPressed = true;
            direction = "down";
        } else  {
            rightPressed = true;
            direction = "right";
        }
    }

    public void update() {
        if (upPressed || downPressed || rightPressed || leftPressed) {
            isColliding = false;
            cm.checkCollision(this);
            if (!isColliding) {
                if (upPressed) {
                    changeYPos(-1 * getPlayerSpeed());
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (leftPressed) {
                    changeXPos(-1 * getPlayerSpeed());
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (downPressed) {
                    changeYPos(getPlayerSpeed());
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    changeXPos(getPlayerSpeed());
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

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
        hitBox.setLocation(xPos + 13, yPos + 24);
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
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getImage();
        g2.drawImage(image, getXPos(), getYPos(), 48, 48, null);
    }


}
