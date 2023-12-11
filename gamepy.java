import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

class gamepy extends JPanel implements KeyListener, ActionListener {
    private int[] snakexlength = new int[750];
    private int[] snakeylength = new int[750];

    private int snakelength = 3;
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private int score = 0;
    private int level = 0;
    private int highestScore = 0;

    private int[] enemyxpos = {25,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625,650,675,700,725,750};
    private int[] enemyypos = {75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525};

    private Random random = new Random();
    private int xpos = random.nextInt(29);
    private int ypos = random.nextInt(19);

    private Timer timer;
    private int delay = 100;
    private ImageIcon enemy;
    private ImageIcon rightimage;
    private ImageIcon upimage;
    private ImageIcon downimage;
    private ImageIcon leftimage;
    private ImageIcon snakeimage;
    private int move = 0;
    private ImageIcon topimage;

    private boolean gameStarted = false;
    private String playerName = "";
    private String lastRecordedPlayerName = "";
    private int lastRecordedScore = -1;
    private String lastRecordedDate = "";

    private Clip soundtrack;

    boolean isGameOver = false; // Add this line at the beginning of your class

    public gamepy() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        if (!gameStarted) {
            g.setColor(Color.white);
            g.setFont(new Font("arial", Font.BOLD, 20));
            g.drawString("Press Space to Start!", 350, 300);
            return;
        }

        if (move == 0) {
            snakexlength[2] = 50;
            snakexlength[1] = 75;
            snakexlength[0] = 100;
            snakeylength[2] = 100;
            snakeylength[1] = 100;
            snakeylength[0] = 100;
        }

        // Draw title image border
        g.setColor(Color.white);
        g.drawRect(24, 10, 851, 55);

        // Draw the title image
        topimage = new ImageIcon("resource\\img\\snakebanner.png");
        topimage.paintIcon(this, g, 25, 11);

        // Draw border for gameplay
        g.setColor(Color.white);
        g.drawRect(24, 74, 851, 576);

        // Draw background for the gameplay
        g.setColor(Color.black);
        g.fillRect(25, 75, 850, 575);

        // Score record
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN ,12));
        g.drawString(String.format("%-8s: %s", "Score", score), 700, 30);

        // Record of snake body's length
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN ,12));
        g.drawString(String.format("%-7s: %s", "Length", snakelength), 700, 50);
        
        // Level indicator
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN ,12));
        g.drawString(String.format("%-16s: %s", "Level", level), 778, 30);
        
        // Highest Score
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN ,12));
        g.drawString(String.format("%-11s: %s", "Best Score", highestScore), 778, 50);

        // rightimage = new ImageIcon("head_right.png");
        // rightimage.paintIcon(this, g, snakexlength[0], snakeylength[0]);

        for (int a = 0; a < snakelength; a++) {
            if (a == 0) { // head of the snake
                if (right) {
                    rightimage = new ImageIcon("resource\\img\\graphics v3\\head_right.png");
                    rightimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (left) {
                    leftimage = new ImageIcon("resource\\img\\graphics v3\\head_left.png");
                    leftimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (up) {
                    upimage = new ImageIcon("resource\\img\\graphics v3\\head_up.png");
                    upimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (down) {
                    downimage = new ImageIcon("resource\\img\\graphics v3\\head_down.png");
                    downimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                } 
            } else if (a == snakelength - 1) { // tail of the snake
                if (right) {
                    snakeimage = new ImageIcon("resource\\img\\graphics v4\\tail_left.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (left) {
                    snakeimage = new ImageIcon("resource\\img\\graphics v4\\tail_right.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (up) {
                    snakeimage = new ImageIcon("resource\\img\\graphics v4\\tail_down.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (down) {
                    snakeimage = new ImageIcon("resource\\img\\graphics v4\\tail_up.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
            } else { // body of the snake
                if (right || left) {
                    snakeimage = new ImageIcon("resource\\img\\graphics v3\\body_horizontal.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (up || down) {
                    snakeimage = new ImageIcon("resource\\img\\graphics v3\\body_vertical.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                // Add conditions for bottomleft, bottomright, topleft, topright based on your game logic
            }
        }

        enemy = new ImageIcon("resource\\img\\apple.png");

        if (enemyxpos[xpos] == snakexlength[0] && enemyypos[ypos] == snakeylength[0]) {
            main.playSound("resource\\wav\\eat2.wav");
            score++;
            if (score > highestScore) {
                highestScore = score;
            }
            if (score % 10 == 0) {
                delay = (int)(delay * 0.9);
                level++;
                timer.setDelay(delay);
            }
            snakelength++;
            xpos = random.nextInt(29);
            ypos = random.nextInt(19);
        }

        enemy.paintIcon(this, g, enemyxpos[xpos], enemyypos[ypos]);

        for (int b = 1; b < snakelength; b++) {
            if (snakexlength[b] == snakexlength[0] && snakeylength[b] == snakeylength[0]) {
                right = left = up = down = false;
        
                if (!isGameOver) { // Add this line
                    main.playSound("resource\\wav\\gameover2.wav");
                    isGameOver = true; // And this line
                    soundtrack.stop();
                }
        
                delay = 100;
                timer.setDelay(delay);
        
                g.setColor(Color.white);
                g.setFont(new Font("arial", Font.BOLD, 50));
                g.drawString("GAME OVER!", 300, 300);
                g.setFont(new Font("arial", Font.BOLD, 20));
                g.drawString("Press SPACE to Re-Play", 350, 340);
                g.drawString("Press E to Exit", 350, 370);
        
                recordScore();
                repaint();
                level = 0;
            }
        }
        g.dispose();
    }
    public void playSoundtrack(String filename) {
        try {
            // If a soundtrack is already playing, stop it
            if (soundtrack != null && soundtrack.isRunning()) {
                soundtrack.stop();
                soundtrack.close();
            }
    
            // Start the new soundtrack
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filename));
            soundtrack = AudioSystem.getClip();
            soundtrack.open(audioIn);
            soundtrack.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void setGameMode() {
        String[] gameModes = {"Easy Mode", "Intermediate", "Hard Mode"};
        int selectedMode = JOptionPane.showOptionDialog(null, "Choose a game mode", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, gameModes, gameModes[0]);
        switch (selectedMode) {
            case 0: // Easy Mode
                delay *= 1; // No speed change
                break;
            case 1: // Intermediate
                delay *= 0.8; // Increase speed by 20%
                break;
            case 2: // Hard Mode
                delay *= 0.6; // Increase speed by 30%
                break;
        }
        timer.setDelay(delay);
    }

    private int recordNo = 0;

    private void recordScore() {
        try {
            // Read the current record number from the scores.txt file
            BufferedReader reader = new BufferedReader(new FileReader("scores.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length > 0) {
                    String recordNoStr = parts[0];
                    if (recordNoStr.matches("\\d+")) {
                        int currentRecordNo = Integer.parseInt(recordNoStr);
                        if (currentRecordNo > recordNo) {
                            recordNo = currentRecordNo;
                        }
                    }
                }
            }
            reader.close();

            if (!playerName.equals(lastRecordedPlayerName) || score != lastRecordedScore || !getCurrentDate().equals(lastRecordedDate)) {
                FileWriter writer = null;
                try {
                    writer = new FileWriter("scores.txt", true);
                    String currentTime = getCurrentTime();
                    String record = String.format("%-4d %-20s %-8d %-8d %s %-4s\n", ++recordNo, playerName, score, level, getCurrentDate(), currentTime);
                    writer.write(record);
                    writer.close();
                    lastRecordedPlayerName = playerName;
                    lastRecordedScore = score;
                    lastRecordedDate = getCurrentDate();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        timer.start();
        if (right) {
            for (int b = snakelength - 1; b >= 0; b--) {
                snakeylength[b + 1] = snakeylength[b];
            }
            for (int b = snakelength; b >= 0; b--) {
                if (b == 0) {
                    snakexlength[b] = snakexlength[b] + 25;
                } else {
                    snakexlength[b] = snakexlength[b - 1];
                }
                if (snakexlength[b] > 850) {
                    snakexlength[b] = 25;
                }
            }
            repaint();
        }
        if (left) {
            for (int b = snakelength - 1; b >= 0; b--) {
                snakeylength[b + 1] = snakeylength[b];
            }
            for (int b = snakelength; b >= 0; b--) {
                if (b == 0) {
                    snakexlength[b] = snakexlength[b] - 25;
                } else {
                    snakexlength[b] = snakexlength[b - 1];
                }
                if (snakexlength[b] < 25) {
                    snakexlength[b] = 850;
                }
            }
            repaint();
        }
        if (up) {
            for (int b = snakelength - 1; b >= 0; b--) {
                snakexlength[b + 1] = snakexlength[b];
            }
            for (int b = snakelength; b >= 0; b--) {
                if (b == 0) {
                    snakeylength[b] = snakeylength[b] - 25;
                } else {
                    snakeylength[b] = snakeylength[b - 1];
                }
                if (snakeylength[b] < 75) {
                    snakeylength[b] = 625;
                }
            }
            repaint();
        }
        if (down) {
            for (int b = snakelength - 1; b >= 0; b--) {
                snakexlength[b + 1] = snakexlength[b];
            }
            for (int b = snakelength; b >= 0; b--) {
                if (b == 0) {
                    snakeylength[b] = snakeylength[b] + 25;
                } else {
                    snakeylength[b] = snakeylength[b - 1];
                }
                if (snakeylength[b] > 625) {
                    snakeylength[b] = 75;
                }
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameStarted) {
                playerName = JOptionPane.showInputDialog("Enter your name:");
                setGameMode();
                playSoundtrack("resource\\wav\\soundtrack1.wav");
                gameStarted = true;
                // Hide the dialog box
                JOptionPane.getRootFrame().dispose();
                repaint();
            } else {
                move = 0;
                score = 0;
                snakelength = 3;
                isGameOver = false;
                playSoundtrack("resource\\wav\\soundtrack1.wav");
            }
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            System.exit(0); // Exit the game
        } else {
            if (gameStarted) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    move++;
                    right = true;
                    if (!left) {
                        right = true;
                    } else {
                        right = false;
                        left = true;
                    }
                    up = false;
                    down = false;
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    move++;
                    left = true;
                    if (!right) {
                        left = true;
                    } else {
                        left = false;
                        right = true;
                    }
                    up = false;
                    down = false;
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    move++;
                    up = true;
                    if (!down) {
                        up = true;
                    } else {
                        up = false;
                        down = true;
                    }
                    right = false;
                    left = false;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    move++;
                    down = true;
                    if (!up) {
                        down = true;
                    } else {
                        down = false;
                        up = true;
                    }
                    right = false;
                    left = false;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
}