package tsos.equalizer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.Font;

import tsos.equalizer.spi.EqualizerInputStream;
import tsos.equalizer.core.IIRControls;

import javax.sound.sampled.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by warprobot on 04.05.14.
 */
public class gui extends JFrame{
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JLabel controlsLabel;
    private JSlider slider3;
    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider4;
    private JSlider slider5;
    private JSlider slider6;
    private JSlider slider7;
    private JSlider slider8;
    private JSlider slider9;
    private JSlider slider10;
    private JButton effect1Button;
    private JButton effect2Button;
    private JPanel rootPanel;
    private JButton effect3Button;
    private JButton effect4Button;
    public int effect = 0;
    public int isPause = 0;
    public float bandDbValue[];
    public static gui ourGUI = new gui();


    public gui() {
        super("EQ form");
        int effect = 0;
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bandDbValue = new float[10];


        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playMusic();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ourGUI.getIsPause() == 0)
                    ourGUI.setIsPause(1);
                else
                    ourGUI.setIsPause(0);
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        effect1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ourGUI.getEff() == 1)
                    ourGUI.setEff(0);
                else
                    ourGUI.setEff(1);
            }
        });

        effect2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ourGUI.getEff() == 2)
                    ourGUI.setEff(0);
                else
                    ourGUI.setEff(2);
            }
        });

        effect3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ourGUI.getEff() == 3)
                    ourGUI.setEff(0);
                else
                    ourGUI.setEff(3);
            }
        });

        effect4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ourGUI.getEff() == 4)
                    ourGUI.setEff(0);
                else
                    ourGUI.setEff(4);
            }
        });

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bandDbValue[0] = slider1.getValue();
            }
        });
        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bandDbValue[1] = slider2.getValue();
            }
        });
        slider3.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bandDbValue[2] = slider3.getValue();
            }
        });
        slider4.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bandDbValue[3] = slider4.getValue();
            }
        });
        slider5.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bandDbValue[4] = slider5.getValue();
            }
        });
        slider6.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bandDbValue[5] = slider6.getValue();
            }
        });
        slider7.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bandDbValue[6] = slider7.getValue();
            }
        });
        slider8.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bandDbValue[7] = slider8.getValue();
            }
        });
        slider9.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bandDbValue[8] = slider9.getValue();
            }
        });
        slider10.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bandDbValue[9] = slider10.getValue();
            }
        });


        setVisible(true);
    }


    public final void playMusic() {
        SwingWorker stringDraw = new MusicWorker();
        stringDraw.execute();
    }


    /** Работа с потоками. **/
    private class MusicWorker extends SwingWorker<Object, Object> {
        private int effect;
        public final Object doInBackground() {
            String args = "/home/megge/kurs/src/tsos/equalizer/test/test.wav";
            playAudioFile(args);
            return null;
        }
    }

    public int getEff(){
        return effect;
    }

    public  void setEff(int eff){
        effect = eff;
    }

    public int getIsPause(){
        return isPause;
    }

    public  void setIsPause(int eff){
        isPause = eff;
    }

    public float[] getBandDbValue(){
        return bandDbValue;
    }



    public static void main(String[] args) {
        // Check for given sound file names.
        if (args.length < 1) {
            System.out.println("Play usage:");
            System.out.println("\tjava Play <sound file names>*");
            System.exit(0);
        }
       // gui ourGUI = new gui();
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
            float bandDbVal[] = ourGUI.getBandDbValue();
            con.setBandDbValue(0, 0, bandDbVal[0]);
            con.setBandDbValue(1, 0, bandDbVal[1]);
            con.setBandDbValue(2, 0, bandDbVal[2]);
            con.setBandDbValue(3, 0, bandDbVal[3]);
            con.setBandDbValue(4, 0, bandDbVal[4]);
            con.setBandDbValue(5, 0, bandDbVal[5]);
            con.setBandDbValue(6, 0, bandDbVal[6]);
            con.setBandDbValue(7, 0, bandDbVal[7]);
            con.setBandDbValue(8, 0, bandDbVal[8]);
            con.setBandDbValue(9, 0, bandDbVal[9]);


            // Create a buffer for moving data from the audio stream to the line.
            int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
            byte[] buffer = new byte[bufferSize];

            int delay = (int) (500);
            delay = (int) (1);
            byte[][] oldBuffer = new byte[delay+1][bufferSize];

            // Move the data until done or there is an error.
            try {
                int bytesRead = 0;

                //vibrato
                float iAndDirForVibrato[] = new float[2];
                iAndDirForVibrato[0] = 0.1f;
                iAndDirForVibrato[1] = 1.0f;
                //vibrato ends

                float currentBands[] = new float[10];
                for (int i = 0; i < 10; i++) {
                    currentBands[i] = bandDbVal[i];
                }

                int startDist = 0;
                byte count=0;
                while (bytesRead >= 0) {
                    while (ourGUI.getIsPause() == 1) {
                        dataLine.stop();
                    }
                    if (ourGUI.getIsPause() == 0) {
                        dataLine.start();
                    }
                    bytesRead = eq.read(buffer, 0, buffer.length);
                    if (ourGUI.getEff() == 0) {
                        con.setPreampValue(0, 1.0f);
                        if (startDist == 1)
                            for (int i=0; i < 10; i++) {
                                bandDbVal[i] = currentBands[i];
                            }
                        startDist = 0;
                    }
                    if (ourGUI.getEff() == 1) {
                        iAndDirForVibrato = con.vibrato(con, iAndDirForVibrato[0], iAndDirForVibrato[1]);
                    }
                    if (ourGUI.getEff() == 2) {
                        if (startDist == 0){
                            for (int i=0; i < 10; i++) {
                                currentBands[i] = bandDbVal[i];
                            }
                        }
                        startDist = 1;
                        bandDbVal = con.distortion(con, bandDbVal);
                    }
                    con.setBandDbValue(0, 0, bandDbVal[0]);
                    con.setBandDbValue(1, 0, bandDbVal[1]);
                    con.setBandDbValue(2, 0, bandDbVal[2]);
                    con.setBandDbValue(3, 0, bandDbVal[3]);
                    con.setBandDbValue(4, 0, bandDbVal[4]);
                    con.setBandDbValue(5, 0, bandDbVal[5]);
                    con.setBandDbValue(6, 0, bandDbVal[6]);
                    con.setBandDbValue(7, 0, bandDbVal[7]);
                    con.setBandDbValue(8, 0, bandDbVal[8]);
                    con.setBandDbValue(9, 0, bandDbVal[9]);

                    if (bytesRead >= 0)
                        if (ourGUI.getEff() != 4 && ourGUI.getEff() != 3) {
                            dataLine.write(buffer, 0, bytesRead);
                        } else
                            if (ourGUI.getEff() == 4) {
                                // начинается пиздец
                                if (count >= 0 && count < delay) {
                                    oldBuffer[count] = buffer;
                                }
                                if (count >= delay) {
                                    oldBuffer[delay] = buffer;
                                    buffer = con.eho(oldBuffer[0], buffer);
                                    for (int k = 0; k < delay - 1; k++) {
                                        oldBuffer[k] = oldBuffer[k + 1];
                                    }
                                    dataLine.write(buffer, 0, bytesRead);
                                }
                                count++;
                            } else {
                                // начинается пиздец
                                if (count >= 0 && count < delay) {
                                    oldBuffer[count] = buffer;
                                }
                                if (count >= delay) {
                                    oldBuffer[delay] = buffer;
                                    buffer = con.reverberation(oldBuffer[0], buffer);
                                    for (int k = 0; k < delay - 1; k++) {
                                        oldBuffer[k] = oldBuffer[k + 1];
                                    }
                                    dataLine.write(buffer, 0, bytesRead);
                                }
                                count++;
                            }
                    //count++;
                    System.out.print(count);
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
