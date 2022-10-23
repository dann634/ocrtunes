package com.example.ocrtune;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class AudioPlayer {

    private Long currentFrame;
    private Clip clip;
    private String status;
    private AudioInputStream audioInputStream;
    private String filePath;

    public AudioPlayer() {
        this.currentFrame = 0L;
        filePath = "src/main/resources/music/" + HomeController.getCurrentSong().getName() + ".wav";
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Error: Unsupported File Format");
        } catch (IOException e) {
            System.err.println("Error: Music Couldn't be played");
        } catch (LineUnavailableException e) {
            System.err.print("Error: Music couldn't be played");
        }
    }

    public void play() {
        if(this.currentFrame != 0L) {
            resume();
        } else {
            clip.start();
        }
    }

    public void pause() {
        this.currentFrame = this.clip.getLongFramePosition();
        clip.stop();
    }

    private void resume() {
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(this.currentFrame);
        this.play();
    }

    public void stop() {
        this.currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    public void jump(long location) {
        if(location > 0 && location < clip.getMicrosecondLength()) {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = location;
            clip.setMicrosecondPosition(location);
            this.play();
        }
    }

    public void resetAudioStream() {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void setVolume(double gain) {
        if(gain > 0 && gain < 1) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }


}
