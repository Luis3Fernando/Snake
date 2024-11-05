/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.thread;

import com.sisary.global.Global;
import java.awt.Color;
import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Luis Fernando
 */
public class Snack extends Thread {

    private static final int GRID_COLUMNS = 30;
    private static final int GRID_ROWS = 18;
    private JPanel snackPanel;
    private int snackIndex;
    private Random random;
    private long currentTime;

    public Snack() {
        random = new Random();
        generateNewSnack();
        currentTime = System.currentTimeMillis() / 1000;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (Global.snackConsumed || secondsElapsed(currentTime, 5)) {
                    generateNewSnack();
                    Global.snackConsumed = false;
                    currentTime = System.currentTimeMillis() / 1000;
                    Global.LabelScore.setText("Score: " + Global.score);
                }

                sleep(100);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Snack.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void generateNewSnack() {
        if (snackPanel != null && Global.snackConsumed == false) {
            snackPanel.removeAll();
            snackPanel.revalidate();
            snackPanel.repaint();
        }
        do {
            snackIndex = random.nextInt(GRID_COLUMNS * GRID_ROWS);
        } while (SnackInBody(snackIndex));

        Global.snackIndex = snackIndex;
        snackPanel = Global.gridList.get(snackIndex);
        mostrarBotana(snackPanel);
    }

    private boolean SnackInBody(int index) {
        for (JPanel panel : Global.snakeBody) {
            if (Global.gridList.indexOf(panel) == index) {
                return true;
            }
        }
        return false;
    }

    public void mostrarBotana(JPanel panel) {
        JPanel hijo = new JPanel();

        panel.removeAll();
        panel.setLayout(null);
        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();
        int snackSize = 20;

        int centerX = (panelWidth - snackSize) / 2;
        int centerY = (panelHeight - snackSize) / 2;

        hijo.setBackground(new Color(0xfefefe));
        hijo.setBounds(centerX, centerY, snackSize, snackSize);
        panel.add(hijo);

        panel.revalidate();
        panel.repaint();
    }

    private boolean secondsElapsed(long startTime, int seconds) {
        return (System.currentTimeMillis() / 1000 - startTime) >= seconds;
    }
}
