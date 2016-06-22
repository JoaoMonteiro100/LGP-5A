package com.lgp5.patient.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;


public class Game extends JPanel {
    @FXML
    private Button myButton;


    public Game(){
    }

    @FXML
    private void initialize() {
        final Game game = new Game();
        try {
            game.start();
            Stage stage;
            launchSettingsView();
            //get a handle to the stage
            stage = (Stage) myButton.getScene().getWindow();
            //close current window
            stage.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myButton.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
<<<<<<< HEAD
                 System.out.println("cenas");
=======
                System.out.println("cenas");
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
                try {
                    game.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void launchSettingsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/settingsView.fxml"));
            Parent parent = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("BrainStream - Settings");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int x = 0;
    private static int y = 0;
    private int xa = 1;
    private static int ya = 1;
    private static int bx = 100;
    private static int ax = 0;
    private int by = getHeight() - 350;
    private static int mouseX;
    private int choice1;
    private static int dead = 0;
    private static int choice2;
    private static int speed;
    private static int choice3;
    JFrame frame = new JFrame("Arinerron's Ping-Pong!");

    private void moveBall() {
        if (x + xa < 0)
            xa = 1;
        if (x + xa > getWidth() - 30)
            xa = -1;
        if (y + ya < 0)
            ya = 1;
        if (y + ya > getHeight() - 30) {
            ya = -1;
            gameOver();
        }

        x = x + xa;
        y = y + ya;
    }

    private void gameOver() {
        dead = 1;
        Object[] options = {
                "Re-play!",
                "Close game!"
        };
        choice1 = JOptionPane.showOptionDialog(null, "You died! :( Re-play?", "Goodbye... :(",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (choice1 == 0) {
            dead = 0;
            Object[] options2 = {
                    "Easy",
                    "Hard"
            };
            choice2 = JOptionPane.showOptionDialog(null, "Please select the difficulty! (Again...) :)", "Waiting for answer...",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options2, options2[0]);

            if (choice2 == 0) {
                speed = 5;
            }
            if (choice2 == 1) {
                speed = 1;
            }
        }
        if (choice1 == 1) {
            setVisible(false);
<<<<<<< HEAD
           System.exit(0);
          //  frame.dispose();
=======
            System.exit(0);
            //  frame.dispose();
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017
        }
        x = 0;
        y = 0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillOval(x, y, 30, 30);
        g.fillRect(mouseX - 100, 350, 80, 10);

    }

    public static void SPACE() {
        Object[] options5 = {
                "Easy",
                "Hard",
                "Exit Program",
                "Restart",
                "Continue"
        };
        choice3 = JOptionPane.showOptionDialog(null, "Please select the difficulty! :)", "Waiting for answer...",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options5, options5[0]);

        if (choice3 == 0) {
            speed = 5;
        }
        if (choice3 == 1) {
            speed = 1;
        }
        if (choice3 == 2) {
            System.exit(0);
        }
        if (choice3 == 3) {
            x = 0;
            y = 0;
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            bx = -3;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            bx = 3;
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            SPACE();
        }
    }
    public void paint(Graphics2D g) {
        g.fillRect(0, 330, 600, 600);
    }

    public  void start() throws InterruptedException {
        Object[] options = {
                "Easy",
                "Hard"
        };
        choice2 = JOptionPane.showOptionDialog(null, "Please select the difficulty! :)", "Waiting for answer...",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (choice2 == 0) {
            speed = 5;
        }
        if (choice2 == 1) {
            speed = 1;
        }

//        JFrame frame = new JFrame("Arinerron's Ping-Pong!");
        Game game = new Game();
        frame.add(game);
        frame.setSize(500, 500);
        frame.setVisible(true);
<<<<<<< HEAD
      //  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
=======
        //  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
>>>>>>> a5bc84d3c04cf674a96b7eaf146218b33ae20017



        while (true) {
            if (dead == 0) {
                game.moveBall();
                mouseX = MouseInfo.getPointerInfo().getLocation().x;
                ax = ax + bx;
                game.repaint();
                Thread.sleep(speed);
                if (y > 325) {
                    if (mouseX - 100 - 40 < x) {
                        if (mouseX - 100 + 40 > x) {
                            if (y < 350) {
                                ya = -1;
                            }
                        }
                    }
                }
            }
        }
    }
}