package sample.controller;

import java.util.*;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import sample.GreenhouseControls;

public class Controller {
    @FXML
    Label FXMLlight;
    @FXML
    Label FXMLwater;
    @FXML
    Label FXMLtemperature;

    public GreenhouseControls gc2;
    public void initialize(){
        new Thread(()-> {
             gc2 = new GreenhouseControls();
            Event[] eventList = {
                    //gc2.new ThermostatNight(0),
                    gc2.new LightOn(400),
                    gc2.new LightOff(800),
                    //gc2.new WaterOn(600),
                    //gc2.new WaterOff(800),
                    //gc2.new ThermostatDay(1400)
            };
            gc2.addEvent(gc2.new Restart(4000, eventList));
            try {
                gc2.runGui();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        FXMLlight.setText(String.valueOf(gc2.getLight()));
                        //FXMLtemperature.setText(String.valueOf(gc2.getWater()));
                        //FXMLwater.setText(String.valueOf(gc2.getThermostat()));
                    }
                });
            }
        };
        Timer tm = new Timer();
        tm.schedule(task, 1, 1);
    }
    private List<Event> eventList = new ArrayList<Event>();

    public void addEvent(Event c) {
        eventList.add(c);
    }

    public void run() throws InterruptedException {
        while (eventList.size() > 0)
            for (Event e : new ArrayList<Event>(eventList))
                if (e.ready()) {
                    Thread.sleep(2000);
                    System.out.println(e);
                    e.action();
                    eventList.remove(e);
                }
    }
    String str;
    public void runGui() throws InterruptedException {
        while (eventList.size() > 0)
            for (Event e : new ArrayList<Event>(eventList))
                if (e.ready()) {
                    str = String.valueOf(e);
                    e.action();
                    eventList.remove(e);
                }
    }
}