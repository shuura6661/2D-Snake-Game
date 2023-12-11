import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class main {
    public static void main(String[] args) {
        JFrame object = new JFrame();
        gamepy gp = new gamepy();

        object.setBounds(10,10,905,700);
        object.setBackground(Color.DARK_GRAY);
        object.setResizable(false);
        object.setVisible(true);
        object.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        object.add(gp);
    }

    public static void playSound(String soundFileName) {
        try {
            // Open an audio input stream.
            File soundFile = new File(soundFileName); //you could also get the sound file with an URL
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);              
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}