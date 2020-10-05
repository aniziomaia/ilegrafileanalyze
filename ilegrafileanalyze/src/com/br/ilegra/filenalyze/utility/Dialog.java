package com.br.ilegra.filenalyze.utility;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Classe responssavel pelas mensagens
 *
 * @author Jose Maia
 *
 */
public class Dialog {

    private static String pathBaseIcons = "com/br/ilegra/filenalyze/icons/";

    public static String ERROR = "Erro";
    public static String SUCCESS = "Sucesso";
    public static String WARNING = "Aviso";
    public static String INFORMATION = "Informação";
    public static String LOADING_DATA = "Carregando os Dados...";

    /**
     * *******************************************TITULOS***************************************** *
     */
    public static String TITLE_WINDOW_SNMP_MAIN = "FILE ANALYZE";

    /**
     * *******************************************MENSAGENS***************************************** *
     */
    public static String MSG_ERROR_FILE_PROPERTIES = "Ocorreu um erro durante a leitura do arquivo de propriedades.";
    public static String MSG_ERROR_FLEX_DIR_MONITOR = "Ocorreu um erro na tentativa de monitorar o diretório ";

    public static void showMsg(AlertType type, String msg, String tytle) {
        Alert alert = new Alert(type);
        Image image = null;

        if (Dialog.ERROR.equals(tytle)) {
            image = new Image(pathBaseIcons + "erro.png");
        } else if (Dialog.WARNING.equals(tytle)) {
            image = new Image(pathBaseIcons + "aviso.png");
        } else if (Dialog.INFORMATION.equals(tytle)) {
            image = new Image(pathBaseIcons + "sucesso.png");
        } else if (Dialog.SUCCESS.equals(tytle)) {
            image = new Image(pathBaseIcons + "sucesso.png");
        }

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(image); // To add an icon

        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);

        alert.setTitle(tytle);
        alert.setContentText(msg);
        //alert.show();
        FadeTransition fade = setAnimation(alert.getDialogPane());
        fade.play();
        alert.showAndWait();

    }

    private static FadeTransition setAnimation(Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1), node);
        fade.setFromValue(0.2);
        fade.setToValue(3);
        //fade.setCycleCount(Timeline.INDEFINITE);
        fade.setAutoReverse(true);
        return fade;
    }
}
