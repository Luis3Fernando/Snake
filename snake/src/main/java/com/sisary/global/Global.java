/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.global;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.BufferedInputStream;

/**
 *
 * @author Luis Fernando
 */
public class Global {

    public static boolean collision = true;
    public static List<JPanel> gridList = new ArrayList<>();
    public static int nextIndex = -1;
    public static int snackIndex = -1;
    public static boolean snackConsumed = false;
    public static int score = 0;
    public static int fall = 0;
    public static List<JPanel> snakeBody = new ArrayList<>();

    public static JLabel LabelVelocidad = new JLabel();
    public static JLabel LabelScore = new JLabel();
    public static JLabel LabelFaults = new JLabel();

    public static boolean banderaRight = false;
    public static boolean banderaLeft = false;
    public static boolean banderaUp = false;
    public static boolean banderaDown = false;

    public static int puntoX = 0;
    public static int puntoY = 0;

    public static JPanel getMiddleElement() {
        int sizeWidth = 1036 / 30;
        int sizeHeight = 629 / 18;

        int col = 490 / sizeWidth;
        int row = 290 / sizeHeight;

        int index = row * 30 + col;

        if (index >= 0 && index < gridList.size()) {
            return gridList.get(index);
        }
        return null;
    }

    public static void playSound(String resourcePath, boolean loop) {
        try (InputStream audioSrc = Global.class.getResourceAsStream(resourcePath); BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc)) {

            if (audioSrc == null) {
                System.err.println("Could not find resource: " + resourcePath);
                return;
            }

            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void showGameOverDialog() {
        int option = JOptionPane.showOptionDialog(
                null,
                "Game Over",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Aceptar"},
                "Aceptar"
        );

        if (option == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }
}
