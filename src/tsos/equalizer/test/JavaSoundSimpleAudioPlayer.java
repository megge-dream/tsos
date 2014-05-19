package tsos.equalizer.test;

import tsos.equalizer.spi.EqualizerInputStream;
import tsos.equalizer.core.IIRControls;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
public class JavaSoundSimpleAudioPlayer {
    /**
     * Plays audio from given file names.
     */
    public static void main(String[] args) {
        // Check for given sound file names.
        if (args.length < 1) {
            System.out.println("Play usage:");
            System.out.println("\tjava Play <sound file names>*");
            System.exit(0);
        }

        // Process arguments.
        for (int i = 0; i < args.length; i++)
            playAudioFile(args[i]);

        // Must exit explicitly since audio creates non-daemon threads.
        System.exit(0);
    } // main

    /**
     * Play audio from the given file name.
     */
    public static void playAudioFile(String fileName) {
        try {
            AudioInputStream audioInputStream;
            try {
                URL url = new URL(fileName);
                audioInputStream = AudioSystem.getAudioInputStream(url);
            } catch (MalformedURLException e) {
                File soundFile = new File(fileName);
                audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            }

            // Create a stream from the given file.
            // Throws IOException or UnsupportedAudioFileException
            playAudioStream(audioInputStream);
        } catch (Exception e) {
            System.out.println("Problem with file " + fileName + ":");
            e.printStackTrace();
        }
    } // playAudioFile

    /**
     * Plays audio from the given audio input stream.
     */
    public static void playAudioStream(AudioInputStream audioInputStream) {
        // Audio format provides information like sample rate, size, channels.
        AudioFormat audioFormat = audioInputStream.getFormat();

        // Convert compressed audio data to uncompressed PCM format.
        if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            AudioFormat newFormat = new AudioFormat(audioFormat.getSampleRate(),
                    audioFormat.getSampleSizeInBits(),
                    audioFormat.getChannels(),
                    true,
                    false);
            System.out.println("Converting audio format to " + newFormat);
            AudioInputStream newStream = AudioSystem.getAudioInputStream(newFormat, audioInputStream);
            audioFormat = newFormat;
            audioInputStream = newStream;
        }

        // Open a data line to play our type of sampled audio.
        // Use SourceDataLine for play and TargetDataLine for record.
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Play.playAudioStream does not handle this type of audio on this system.");
            return;
        }

        try {
            // Create a SourceDataLine for play back (throws LineUnavailableException).
            SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine(info);
            System.out.println("SourceDataLine class=" + dataLine.getClass());

            // The line acquires system resources (throws LineAvailableException).
            dataLine.open(audioFormat);

            // Adjust the volume on the output line.
            if (dataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) dataLine.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue((volume.getMaximum() - volume.getMinimum()) * 1f + volume.getMinimum());
            }

            // Allows the line to move data in and out to a port.
            dataLine.start();

            EqualizerInputStream eq = new EqualizerInputStream(audioInputStream, 10);

            IIRControls con = eq.getControls();
            con.setBandDbValue(0, 0, -12f);
            con.setBandDbValue(1, 0, -12f);
            con.setBandDbValue(2, 0, 12f);
            con.setBandDbValue(3, 0, -12f);
            con.setBandDbValue(4, 0, 12f);
            con.setBandDbValue(5, 0, 12f);
            con.setBandDbValue(6, 0, 4f);
            con.setBandDbValue(7, 0, -12f);
            con.setBandDbValue(8, 0, 0f);
            con.setBandDbValue(9, 0, -12f);


            // Create a buffer for moving data from the audio stream to the line.
            int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
            byte[] buffer = new byte[bufferSize];

            // Move the data until done or there is an error.
            try {
                int bytesRead = 0;
                while (bytesRead >= 0) {
                    bytesRead = eq.read(buffer, 0, buffer.length);
                    if (bytesRead >= 0)
                        dataLine.write(buffer, 0, bytesRead);
                } // while
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Play.playAudioStream draining line.");
            // Continues data line I/O until its buffer is drained.
            dataLine.drain();

            System.out.println("Play.playAudioStream closing line.");
            // Closes the data line, freeing any resources such as the audio device.
            dataLine.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    } // playAudioStream
}
