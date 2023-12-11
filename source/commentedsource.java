// Importing necessary classes for GUI components and event handling
import java.awt.*;                    // Provides classes for creating and managing graphical user interface components
import java.awt.event.ActionEvent;    // Represents an action event that occurs when a component is activated
import java.awt.event.ActionListener; // Listens for action events, such as button clicks
import java.awt.event.KeyEvent;       // Represents a key event that occurs when a key is pressed, released, or typed
import java.awt.event.KeyListener;    // Listens for key events, such as key presses and releases

// Importing classes for file I/O operations
import java.io.FileWriter;         // Used for writing data to a file
import java.io.IOException;        // Used for handling input/output exceptions
import java.text.SimpleDateFormat; // Used for formatting dates
import java.util.Date;             // Used for representing dates and times
import java.util.Random;           // Used for generating random numbers
import javax.swing.*;              // Provides classes for creating and managing graphical user interface components
import java.io.BufferedReader;     // Used for reading text from a character-input stream
import java.io.FileReader;         // Used for reading data from a file

class gamepy extends JPanel implements KeyListener, ActionListener {
    // Array to store the x-coordinate of each segment of the snake
    private int[] snakexlength = new int[750];

    // Array to store the y-coordinate of each segment of the snake
    private int[] snakeylength = new int[750];

    private int snakelength = 3;     // Stores the length of the snake
    private boolean left = false;    // Indicates if the snake is moving left
    private boolean right = false;   // Indicates if the snake is moving right
    private boolean up = false;      // Indicates if the snake is moving up
    private boolean down = false;    // Indicates if the snake is moving down
    private int score = 0;           // Stores the current score
    private int level = 0;           // Stores the current level
    private int highestScore = 0;    // Stores the highest score achieved

    private int[] enemyxpos = {25, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625, 650, 675, 700, 725, 750}; // Stores the x-coordinate of each enemy
    private int[] enemyypos = {75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525};                                                  // Stores the y-coordinate of each enemy

    private Random random = new Random();  // Used for generating random numbers
    private int xpos = random.nextInt(29); // Stores the x-coordinate of the randomly positioned enemy
    private int ypos = random.nextInt(19); // Stores the y-coordinate of the randomly positioned enemy

    private Timer timer;              // Used for game timing
    private int speed = 100;          // Delay between each game update
    private ImageIcon enemy;          // Image of the enemy
    private ImageIcon rightimage;     // Image of the snake's head facing right
    private ImageIcon upimage;        // Image of the snake's head facing up
    private ImageIcon downimage;      // Image of the snake's head facing down
    private ImageIcon leftimage;      // Image of the snake's head facing left
    private ImageIcon snakeimage;     // Image of the snake's body
    private ImageIcon topimage;       // Image for the game's title
    private int move = 0;             // Indicates the current move of the snake

    private boolean gameStarted = false;             // Indicates if the game has started
    private String playerName = "";                  // Stores the name of the player
    private String lastRecordedPlayerName = "";      // Stores the name of the player with the last recorded score
    private int lastRecordedScore = -1;              // Stores the last recorded score. This variable is initialized with a value of -1 to indicate that no score has been recorded yet.
    private String lastRecordedDate = "";            // Stores the date of the last recorded score

    /**
     * Constructor for the gamepy class.
     * Initializes the game and starts the timer.
     */
    public gamepy() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(speed, this);
        timer.start();
    }

    /**
     * This method is responsible for painting the game graphics on the screen.
     * It is called automatically by the system whenever the screen needs to be updated.
     * It draws various elements such as the game title, gameplay area, snake, apple, and score.
     * If the game is not started, it displays a message to prompt the user to start the game.
     * If the game is over, it displays a game over message and options to replay or exit.
     * 
     * @param g The Graphics object used for drawing on the screen.
     */
    
    public void paint(Graphics g) {
        // Check if the game has not started
        if (!gameStarted) {
            // Set the color to white
            g.setColor(Color.white);
            // Set the font to Arial with bold style and size 20
            g.setFont(new Font("arial", Font.BOLD, 20));
            // Display the message "Press Space to Start!" at coordinates (350, 300)
            g.drawString("Press Space to Start!", 350, 300);
            // Exit the method and return to the caller
            return;
        }

        // Initialize the initial position of the snake
        if (move == 0) {
            // Set the x-coordinates of the snake's body segments
            snakexlength[2] = 50;  // Index 2 represents the tail of the snake
            snakexlength[1] = 75;  // Index 1 represents the body segment of the snake
            snakexlength[0] = 100; // Index 0 represents the head of the snake

            // Set the y-coordinates of the snake's body segments
            snakeylength[2] = 100; // Index 2 represents the tail of the snake
            snakeylength[1] = 100; // Index 1 represents the body segment of the snake
            snakeylength[0] = 100; // Index 0 represents the head of the snake
        }

        /**
         * This code block is responsible for drawing various elements on the screen.
         * It includes drawing a title image border, a title image, a gameplay border, a gameplay background,
         * score record, snake body's length record, level indicator, and highest score record.
         * The elements are drawn using the Graphics object 'g' and various colors and fonts.
         */

        // Draw title image border
        g.setColor(Color.white);
        g.drawRect(24, 10, 851, 55);

        // Draw the title image
        topimage = new ImageIcon("BannerArtboard 1.png");
        topimage.paintIcon(this, g, 25, 11);

        // Draw border for gameplay
        g.setColor(Color.black);
        g.drawRect(24, 74, 851, 577);

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

        /**
         * This loop iterates over the snake's length.
         */
        for (int a = 0; a < snakelength; a++) {
            /**
             * Checks if the value of 'a' is equal to 0.
             * This condition represents the head of the snake.
             */
            if (a == 0) { //the head of the snake
                /**
                 * This code block is responsible for drawing the snake's head image based on its direction.
                 * It checks the direction flags (right, left, up, down) and paints the corresponding image icon on the screen.
                 * The image icons are loaded from the files "head_right.png", "head_left.png", "head_up.png", and "head_down.png".
                 * The position of the snake's head is determined by the arrays snakexlength and snakeylength.
                 * 
                 * @param g The graphics object used for painting.
                 * @param a The index of the snake's head in the arrays snakexlength and snakeylength.
                 */
                if (right) {
                    rightimage = new ImageIcon("head_right.png");
                    rightimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (left) {
                    leftimage = new ImageIcon("head_left.png");
                    leftimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (up) {
                    upimage = new ImageIcon("head_up.png");
                    upimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (down) {
                    downimage = new ImageIcon("head_down.png");
                    downimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                } 
            } else if (a == snakelength - 1) { // tail of the snake
                if (right) {
                    snakeimage = new ImageIcon("tail_left.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (left) {
                    snakeimage = new ImageIcon("tail_right.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (up) {
                    snakeimage = new ImageIcon("tail_down.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (down) {
                    snakeimage = new ImageIcon("tail_up.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
            } else { // body of the snake
                if (right || left) {
                    snakeimage = new ImageIcon("body_horizontal.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                if (up || down) {
                    snakeimage = new ImageIcon("body_vertical.png");
                    snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
                }
                // Add conditions for bottomleft, bottomright, topleft, topright based on your game logic
            }
        }

        /**
         * Creates a new ImageIcon object with the specified image file.
         * 
         * @param imagePath the path to the image file
         */
        enemy = new ImageIcon("apple.png");

        /**
         * Checks if the snake's head collides with an enemy.
         * If there is a collision, it updates the score, highest score, speed, level, and snake length.
         * It also generates new random positions for the enemy.
         */
        // Checks if the enemy's x position matches the snake's head x position
        // and if the enemy's y position matches the snake's head y position
        if (enemyxpos[xpos] == snakexlength[0] && enemyypos[ypos] == snakeylength[0]) {
            // Increases the score by 1
            score++;
            // Checks if the current score is higher than the highest score
            if (score > highestScore) {
                // Updates the highest score with the current score
                highestScore = score;
            }
            // Checks if the score is divisible by 2
            if (score % 2 == 0) {
                // Decreases the speed by 10% (increases the game speed)
                speed = (int)(speed * 0.9);
                // Increases the level by 1
                level++;
                // Updates the timer delay with the new speed
                timer.setDelay(speed);
            }
            // Increases the length of the snake by 1
            snakelength++;
            // Generates new random positions for the enemy
            xpos = random.nextInt(29);
            ypos = random.nextInt(19);
        }

        /**
         * Paints the enemy icon on the specified graphics object at the given x and y positions.
         *
         * @param g The graphics object to paint on.
         * @param enemy The enemy icon to paint.
         * @param xpos The x positions of the enemy.
         * @param ypos The y positions of the enemy.
         */
        enemy.paintIcon(this, g, enemyxpos[xpos], enemyypos[ypos]);

        /**
         * This loop checks if the snake's head collides with any part of its body.
         * If a collision is detected, the game over state is triggered.
         * The game over state disables all movement directions, sets the game speed to 100,
         * displays a "GAME OVER!" message on the screen, and prompts the player to either replay or exit the game.
         * The score is also recorded and the level is reset to 0.
         */
        for (int b = 1; b < snakelength; b++) {
            // Check if the snake's head collides with any part of its body
            if (snakexlength[b] == snakexlength[0] && snakeylength[b] == snakeylength[0]) {
                
                // Disable all movement directions
                right = false;
                left = false;
                up = false;
                down = false;
                
                // Set the game speed to 100 miliseconds
                speed = 100;
                /**
                 * Sets the delay of the timer.
                 * 
                 * @param speed the delay in milliseconds
                 */
                timer.setDelay(speed);
                
                // Set the color to white and set the font to bold with size 50
                g.setColor(Color.white);
                g.setFont(new Font("arial", Font.BOLD, 50));
                
                // Display "GAME OVER!" message on the screen at position (300, 300)
                g.drawString("GAME OVER!", 300, 300);
                
                // Set the font to bold with size 20
                g.setFont(new Font("arial", Font.BOLD, 20));
                
                // Display "Press SPACE to Re-Play" message on the screen at position (350, 340)
                g.drawString("Press SPACE to Re-Play", 350, 340);
                
                // Display "Press E to Exit" message on the screen at position (350, 370)
                g.drawString("Press E to Exit", 350, 370);
                
                // Record the score
                recordScore();
                
                // Repaint the screen
                repaint();
                
                // Reset the level to 0
                level = 0;
            }
        }
        /**
         * Disposes of this graphics context and releases any system resources that it is using.
         * The graphics context cannot be used after being disposed.
         */
        g.dispose();
    }

    /**
     * This method is responsible for recording the score in the "scores.txt" file.
     * It reads the current record number from the file, checks if the current score is higher than the previous recorded score,
     * and if so, appends the new score to the file along with the player name, score, level, date, and time.
     * The method also updates the last recorded player name, score, and date.
     * If an error occurs while reading or writing the file, it prints the stack trace.
     */
    private int recordNo = 0;

    private void recordScore() {
        try {
            // Read the current record number from the scores.txt file
            // Create a BufferedReader to read the "scores.txt" file
            BufferedReader reader = new BufferedReader(new FileReader("scores.txt"));
            String line;

            // Read each line of the file
            while ((line = reader.readLine()) != null) {
                // Split the line into an array of strings using whitespace as the delimiter
                String[] parts = line.split("\\s+");
                if (parts.length > 0) {
                    String recordNoStr = parts[0];
                    // Check if the current line contains a valid record number
                    if (recordNoStr.matches("\\d+")) {
                        // Convert the record number from string to integer
                        int currentRecordNo = Integer.parseInt(recordNoStr);
                        // Check if the current record number is greater than the previous record number
                        if (currentRecordNo > recordNo) {
                            // Update the record number with the current record number
                            recordNo = currentRecordNo;
                        }
                    }
                }
            }
            /**
             * Closes the reader.
             */
            reader.close();

            // Check if the current score, player name, or date is different from the last recorded score
            if (!playerName.equals(lastRecordedPlayerName) || score != lastRecordedScore || !getCurrentDate().equals(lastRecordedDate)) {
                /**
                 * This line of code initializes a FileWriter object and assigns it to the variable 'writer'.
                 */
                FileWriter writer = null;
                try {
                    // Append the new score to the scores.txt file
                    writer = new FileWriter("scores.txt", true); // Create a FileWriter object to write to the "scores.txt" file in append mode
                    String currentTime = getCurrentTime(); // Get the current time in the format "HH:mm:ss"
                    // Format the record string with the current record number, player name, score, level, current date, and current time
                    String record = String.format("%-4d %-20s %-8d %-8d %s %-4s\n", ++recordNo, playerName, score, level, getCurrentDate(), currentTime);
                    writer.write(record); // Write the record string to the file
                    writer.close(); // Close the FileWriter object to release system resources
                    
                    // Update the last recorded player name, score, and date
                    lastRecordedPlayerName = playerName;
                    lastRecordedScore = score;
                    lastRecordedDate = getCurrentDate();
                // Catch any IOException that occurs during the FileWriter closing process
                } catch (IOException e) {
                    e.printStackTrace();
                // This code block is responsible for closing the FileWriter object 'writer' and handling any IOException that occurs during the closing process.
                } finally {
                    if (writer != null) {
                        try {
                            writer.close(); // Close the FileWriter object to release system resources
                        } catch (IOException e) {
                            e.printStackTrace(); // Print the stack trace if an IOException occurs during the closing process
                        }
                    }
                }
            }
        // Catch any IOException that occurs during the FileWriter closing process
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if an IOException occurs during the closing process
        }
    }

    /**
     * Returns the current time in the format "HH:mm:ss".
     * @return The current time as a string.
     */
    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    /**
     * Returns the current date in the format "yyyy-MM-dd".
     * @return The current date as a string.
     */
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * This method is called when an action event occurs, such as a button click.
     * It handles the movement of the snake based on the direction flags (right, left, up, down).
     * The snake's body segments are updated accordingly, and the game screen is repainted.
     * 
     * @param arg0 The action event that triggered the method
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        timer.start(); // Start the timer for the game
        if (right) { // If the snake is moving right
            for (int b = snakelength - 1; b >= 0; b--) {
                snakeylength[b + 1] = snakeylength[b]; // Shift the y-coordinate of each body segment one position down
            }
            for (int b = snakelength; b >= 0; b--) {
                if (b == 0) {
                    snakexlength[b] = snakexlength[b] + 25; // Move the head of the snake 25 units to the right
                } else {
                    snakexlength[b] = snakexlength[b - 1]; // Move each body segment to the position of the previous segment
                }
                if (snakexlength[b] > 850) { // If the head of the snake goes beyond the right boundary
                    snakexlength[b] = 25; // Wrap around to the left boundary
                }
            }
            repaint(); // Repaint the game screen to update the snake's position
        }
        // Move the snake to the left
        if (left) {
            // Shift the y-coordinate of each body segment one position down
            for (int b = snakelength - 1; b >= 0; b--) {
                snakeylength[b + 1] = snakeylength[b];
            }
            // Move each body segment to the position of the previous segment
            for (int b = snakelength; b >= 0; b--) {
                if (b == 0) {
                    snakexlength[b] = snakexlength[b] - 25; // Move the head of the snake 25 units to the left
                } else {
                    snakexlength[b] = snakexlength[b - 1];
                }
                if (snakexlength[b] < 25) { // If the head of the snake goes beyond the left boundary
                    snakexlength[b] = 850; // Wrap around to the right boundary
                }
            }
            repaint(); // Repaint the game screen to update the snake's position
        }
        // Move the snake upwards
        if (up) {
            // Shift the x-coordinate of each body segment one position to the right
            for (int b = snakelength - 1; b >= 0; b--) {
                snakexlength[b + 1] = snakexlength[b];
            }
            // Move each body segment to the position of the previous segment
            for (int b = snakelength; b >= 0; b--) {
                if (b == 0) {
                    snakeylength[b] = snakeylength[b] - 25; // Move the head of the snake 25 units upwards
                } else {
                    snakeylength[b] = snakeylength[b - 1];
                }
                if (snakeylength[b] < 75) { // If the head of the snake goes beyond the top boundary
                    snakeylength[b] = 625; // Wrap around to the bottom boundary
                }
            }
            repaint(); // Repaint the game screen to update the snake's position
        }
        // Move the snake downwards
        if (down) {
            // Shift the x-coordinate of each body segment one position to the right
            for (int b = snakelength - 1; b >= 0; b--) {
                snakexlength[b + 1] = snakexlength[b];
            }
            // Move each body segment to the position of the previous segment
            for (int b = snakelength; b >= 0; b--) {
                if (b == 0) {
                    snakeylength[b] = snakeylength[b] + 25; // Move the head of the snake 25 units downwards
                } else {
                    snakeylength[b] = snakeylength[b - 1];
                }
                if (snakeylength[b] > 625) { // If the head of the snake goes beyond the bottom boundary
                    snakeylength[b] = 75; // Wrap around to the top boundary
                }
            }
            repaint(); // Repaint the game screen to update the snake's position
        }
    }

    /**
     * Handles the key pressed event.
     * If the space key is pressed and the game has not started, prompts the user to enter their name and starts the game.
     * If the space key is pressed and the game has already started, resets the game.
     * If the E key is pressed, exits the game.
     * If any other arrow key is pressed and the game has started, updates the movement direction of the snake.
     * 
     * @param e The KeyEvent object representing the key press event.
     */
    @Override
    // Handle the key pressed event
    public void keyPressed(KeyEvent e) {
        // If the space key is pressed
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // If the game has not started
            if (!gameStarted) {
                // Prompt the user to enter their name
                playerName = JOptionPane.showInputDialog("Enter your name:");
                // Start the game
                gameStarted = true;
                // Hide the dialog box
                JOptionPane.getRootFrame().dispose();
                // Repaint the game screen
                repaint();
            } else {
                // Reset the game
                move = 0;
                score = 0;
                snakelength = 3;
            }
        } 
        // If the E key is pressed
        else if (e.getKeyCode() == KeyEvent.VK_E) {
            // Exit the game
            System.exit(0);
        } 
        // If any other arrow key is pressed
        else {
            // If the game has started
            if (gameStarted) {
                // If the right arrow key is pressed
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    move++;
                    right = true;
                    // If the snake is not moving left
                    if (!left) {
                        right = true;
                    } else {
                        right = false;
                        left = true;
                    }
                    up = false;
                    down = false;
                } 
                // If the left arrow key is pressed
                else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    move++;
                    left = true;
                    // If the snake is not moving right
                    if (!right) {
                        left = true;
                    } else {
                        left = false;
                        right = true;
                    }
                    up = false;
                    down = false;
                } 
                // If the up arrow key is pressed
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    move++;
                    up = true;
                    // If the snake is not moving down
                    if (!down) {
                        up = true;
                    } else {
                        up = false;
                        down = true;
                    }
                    right = false;
                    left = false;
                } 
                // If the down arrow key is pressed
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    move++;
                    down = true;
                    // If the snake is not moving up
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

    // This method is called when a key is released
    @Override
    public void keyReleased(KeyEvent arg0) {
        // No action is performed in this method
    }

    // This method is called when a key is typed
    @Override
    public void keyTyped(KeyEvent arg0) {
        // No action is performed in this method
    }
}