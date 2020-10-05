package com.br.ilegra.filenalyze.application;

import com.br.ilegra.filenalyze.utility.Dialog;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class que exibe e esconde a tela de splash sincronizado pelo Pela class Main
 *
 * @author Jose Maia
 *
 */
public class FAPreloader extends Preloader {

    private Stage preloaderStage;
    ProgressBar myProg = new ProgressBar();

    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;

        VBox loading = new VBox(20);

        myProg.setPrefWidth(350);
        loading.setMaxWidth(Region.USE_PREF_SIZE);
        loading.setMaxHeight(Region.USE_PREF_SIZE);
        loading.getChildren().add(myProg);
        loading.getChildren().add(new Label(Dialog.LOADING_DATA));

        BorderPane root = new BorderPane(loading);
        Scene scene = new Scene(root);

        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {

        myProg.setProgress(pn.getProgress());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == Type.BEFORE_START) {
            preloaderStage.hide();
        }
    }
}
