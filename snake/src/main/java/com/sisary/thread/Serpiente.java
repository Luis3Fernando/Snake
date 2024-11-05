/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.thread;

import com.sisary.global.Global;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Luis Fernando
 */
public class Serpiente extends Thread {

    private static final int GRID_COLUMNS = 30;
    private static final int GRID_ROWS = 18;
    private static final int INITIAL_SPEED = 100;
    private static final int SPEED_INCREMENT = 5;
    private static final int MIN_SPEED = 20;

    Random random = null;
    JPanel head = new JPanel();
    int headIndex;
    int speed = INITIAL_SPEED;
    boolean growing = false;
    private boolean isReset = false;
    int start = 0;

    public Serpiente() {
        random = new Random();
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (Global.nextIndex != -1) {
                    moveSnake();
                    isReset = false;
                } else if (Global.banderaLeft) {
                    Global.banderaLeft = false;
                    moveLeft();
                    isReset = false;
                } else if (Global.banderaRight) {
                    Global.banderaRight = false;
                    moveRight();
                    isReset = false;
                } else if (Global.banderaUp) {
                    Global.banderaUp = false;
                    moveUp();
                    isReset = false;
                } else if (Global.banderaDown) {
                    Global.banderaDown = false;
                    moveDown();
                    isReset = false;
                } else if (!isReset) {
                    reset();
                }
                sleep(speed);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Serpiente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void moveSnake() {
        if (headIndex == Global.nextIndex) {
            return;
        }

        int siguienteFila = Global.nextIndex / GRID_COLUMNS;
        int siguienteColumna = Global.nextIndex % GRID_COLUMNS;
        int filaActual = headIndex / GRID_COLUMNS;
        int columnaActual = headIndex % GRID_COLUMNS;

        if (siguienteFila > filaActual && siguienteColumna > columnaActual) {
            headIndex += (GRID_COLUMNS + 1);
        } else if (siguienteFila > filaActual && siguienteColumna < columnaActual) {
            headIndex += (GRID_COLUMNS - 1);
        } else if (siguienteFila < filaActual && siguienteColumna > columnaActual) {
            headIndex -= (GRID_COLUMNS - 1);
        } else if (siguienteFila < filaActual && siguienteColumna < columnaActual) {
            headIndex -= (GRID_COLUMNS + 1);
        } else if (siguienteFila > filaActual) {
            headIndex += GRID_COLUMNS;
        } else if (siguienteFila < filaActual) {
            headIndex -= GRID_COLUMNS;
        } else if (siguienteColumna > columnaActual) {
            headIndex += 1;
        } else if (siguienteColumna < columnaActual) {
            headIndex -= 1;
        }

        head = Global.gridList.get(headIndex);
        Global.snakeBody.add(0, head);

        if (Global.snackIndex == headIndex) {
            Global.snackConsumed = true;
            Global.score++;
            Global.playSound("/audio/comer.wav", false);
            growing = true;
            speed = Math.max(speed - SPEED_INCREMENT, MIN_SPEED);
            Global.LabelVelocidad.setText("Velocidad: " + speed);
            
            if(Global.score >= 400){
                Global.showGameOverDialog();
            }
        }

        for (int i = Global.snakeBody.size() - 1; i >= 0; i--) {
            if (i == 0) {
                mostrarPanel(Global.snakeBody.get(i), new Color(0x4aaf9f));
            } else {
                mostrarPanel(Global.snakeBody.get(i), new Color(0x2c776c));
            }
        }

        List<JPanel> panelesParaEliminar = new ArrayList<>();

        if (!growing && Global.snakeBody.size() > 1) {
            JPanel tail = Global.snakeBody.remove(Global.snakeBody.size() - 1);
            if (!Global.snakeBody.contains(tail)) {
                tail.removeAll();
                tail.revalidate();
                tail.repaint();
            } else {
                panelesParaEliminar.add(tail);
            }
        } else {
            growing = false;
        }

        if (!growing && !panelesParaEliminar.isEmpty()) {
            for (JPanel panel : panelesParaEliminar) {
                if (!Global.snakeBody.contains(panel)) {
                    panel.removeAll();
                    panel.revalidate();
                    panel.repaint();
                }
            }
            panelesParaEliminar.clear();
        }
    }

    public void mostrarPanel(JPanel padre, Color color) {
        JPanel hijo = new JPanel();

        padre.removeAll();
        padre.setLayout(null);
        int panelWidth = padre.getWidth();
        int panelHeight = padre.getHeight();
        int eatSize = 30;

        int centerX = (panelWidth - eatSize) / 2;
        int centerY = (panelHeight - eatSize) / 2;

        hijo.setBackground(color);
        hijo.setBounds(centerX, centerY, eatSize, eatSize);
        padre.add(hijo);

        padre.revalidate();
        padre.repaint();
    }

    public void reset() {
        for (JPanel segment : Global.snakeBody) {
            segment.removeAll();
            segment.revalidate();
            segment.repaint();
        }
        Global.snakeBody.clear();

        head = Global.getMiddleElement();
        headIndex = Global.gridList.indexOf(head);
        Global.snakeBody.add(head);

        head.removeAll();
        head.setLayout(null);
        int panelWidth = head.getWidth();
        int panelHeight = head.getHeight();
        int eatSize = 30;

        int centerX = (panelWidth - eatSize) / 2;
        int centerY = (panelHeight - eatSize) / 2;

        JPanel iniciar = new JPanel();
        iniciar.setBackground(new Color(0x4aaf9f));
        iniciar.setBounds(centerX, centerY, eatSize, eatSize);
        head.add(iniciar);

        head.revalidate();
        head.repaint();

        if (start != 0) {
            Global.fall = Global.fall + 1;
            Global.LabelFaults.setText("Faults: " + Global.fall);
            Global.playSound("/audio/chocar.wav", false);
            speed = INITIAL_SPEED;
            Global.LabelVelocidad.setText("Velocidad: " + speed);
        }
        start += 1;
        isReset = true;
    }

    private void moveLeft() {
        boolean bandera = false;
        try {
            while (headIndex % GRID_COLUMNS != 0) {
                if (Global.nextIndex != -1 || Global.banderaRight || Global.banderaUp || Global.banderaDown) {
                    bandera = true;
                    break;
                } else {
                    headIndex -= 1;
                    updateSnake();
                    Thread.sleep(speed);

                }

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Serpiente.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void moveRight() {
        boolean bandera = false;
        try {
            while (headIndex % GRID_COLUMNS != GRID_COLUMNS - 1) {
                if (Global.nextIndex != -1 || Global.banderaLeft || Global.banderaUp || Global.banderaDown) {
                    bandera = true;
                    break;
                } else {
                    headIndex += 1;
                    updateSnake();
                    Thread.sleep(speed);

                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Serpiente.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void moveUp() {
        boolean bandera = false;
        try {
            while (headIndex >= GRID_COLUMNS) {
                if (Global.nextIndex != -1 || Global.banderaRight || Global.banderaLeft || Global.banderaDown) {
                    bandera = true;
                    break;
                } else {
                    headIndex -= GRID_COLUMNS;
                    updateSnake();
                    Thread.sleep(speed);
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Serpiente.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void moveDown() {
        boolean bandera = false;
        try {
            while (headIndex < GRID_COLUMNS * (GRID_ROWS - 1)) {
                if (Global.nextIndex != -1 || Global.banderaRight || Global.banderaUp || Global.banderaLeft) {
                    bandera = true;
                    break;
                } else {
                    headIndex += GRID_COLUMNS;
                    updateSnake();
                    Thread.sleep(speed);

                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Serpiente.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateSnake() {
        head = Global.gridList.get(headIndex);
        Global.snakeBody.add(0, head);

        for (int i = Global.snakeBody.size() - 1; i >= 0; i--) {
            if (i == 0) {
                mostrarPanel(Global.snakeBody.get(i), new Color(0x4aaf9f));
            } else {
                mostrarPanel(Global.snakeBody.get(i), new Color(0x2c776c));
            }
        }

        List<JPanel> panelesParaEliminar = new ArrayList<>();

        if (!growing && Global.snakeBody.size() > 1) {
            JPanel tail = Global.snakeBody.remove(Global.snakeBody.size() - 1);
            if (!Global.snakeBody.contains(tail)) {
                tail.removeAll();
                tail.revalidate();
                tail.repaint();
            } else {
                panelesParaEliminar.add(tail);
            }
        } else {
            growing = false;
        }

        if (!growing && !panelesParaEliminar.isEmpty()) {
            for (JPanel panel : panelesParaEliminar) {
                if (!Global.snakeBody.contains(panel)) {
                    panel.removeAll();
                    panel.revalidate();
                    panel.repaint();
                }
            }
            panelesParaEliminar.clear();
        }
    }
}
