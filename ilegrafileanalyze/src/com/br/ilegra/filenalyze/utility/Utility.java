
package com.br.ilegra.filenalyze.utility;

import java.io.File;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;

/**
 * 
 * @author Jose Maia
 */
public class Utility {
    
    /**
     * metodo que faz o aguardo de um processamento de acordo com o tempo
     * definido
     *
     * @param mileseconds
     */
    public static void setTimeToSleepByThread(int mileseconds) {

        try {

            Thread.sleep(mileseconds);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void changeStyleScreenInit(Pane pane) {
        //configura as cores para amarelo
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pane.setStyle("-fx-background-color: aliceblue");
            }
        });
    }
    
     public static void changeStyleScreenSuccess(Pane pane) {
        //configura as cores para verde
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pane.setStyle("-fx-background-color: lightgreen");
            }
        });
    }

    public static void changeStyleScreenWarning(Pane pane) {
        //configura as cores para amarelo
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pane.setStyle("-fx-background-color: yellow");
            }
        });
    }
    
    public static void changeStyleScreenFail(Pane pane) {
        //configura as cores para vermelho
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pane.setStyle("-fx-background-color: red");
            }
        });
    }
    
    public static void setFocusField(TextInputControl field) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                field.requestFocus();
            }
        });
    }
    
    public static void setTheTextLog(TextArea taLog, String log) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                taLog.setText(taLog.getText() + log);
                taLog.appendText("");
            }
        });
    }
    
    public static void setMessage(Label lbMessage, String msg, Alert.AlertType type) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbMessage.setText(msg);
            }
        });

        if (type == Alert.AlertType.NONE) {
            lbMessage.setStyle("-fx-text-fill: red");
        } else {
            lbMessage.setStyle("-fx-text-fill: black");
        }
    }
    
    /**
     * @param fields 
     */
    public static void cleanFields(TextInputControl ...fields) {

        for (int i = 0; i < fields.length; i++) {
            TextInputControl field = fields[i];
            
            Platform.runLater(new Runnable() {
            @Override
            public void run() {
                field.setText("");
                field.positionCaret(0);
                field.requestFocus();
            }
            });
        }
    }
    
    /**
     * verifica se as DLLs do framework jnotify foram colocadas no diretorio de
     * instalacao do java
     *
     * @return
     */
    public static boolean checkJnotifyDLL(TextArea taLog, Label lbMessage) {

        String jnotifydll = "\\jnotify.dll";
        String jnotify_64bit = "\\jnotify_64bit.dll";

        String pathJvmInstalled = System.getProperty("sun.boot.library.path");

        File fileJnotifydll = new File(pathJvmInstalled + jnotifydll);
        File fileJnotify_64bit = new File(pathJvmInstalled + jnotify_64bit);

        Utility.setTheTextLog(taLog, "jnotify.dll--> " + pathJvmInstalled + jnotifydll + "\n");
        Utility.setTheTextLog(taLog, "jnotify_64bit.dll--> " + pathJvmInstalled + jnotify_64bit + "\n");

        if (!fileJnotifydll.exists() || !fileJnotify_64bit.exists()) {

            String texto = "As DLLs [jnotify.dll e jnotify_64bit.dll]  não foram encontradas: " + "\n" + 
                    "Copie as DLLs abaixo para o diretório:" + pathJvmInstalled + "\n";
            Utility.setMessage(lbMessage, texto, Alert.AlertType.NONE);

            return false;
        } else {

            Utility.setTheTextLog(taLog, "As DLLs foram encontradas." + "\n");
            return true;
        }
    }
}
