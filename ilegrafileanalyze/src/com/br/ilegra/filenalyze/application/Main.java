package com.br.ilegra.filenalyze.application;

import com.br.ilegra.filenalyze.resource.AppProperties;
import com.br.ilegra.filenalyze.utility.Dialog;
import com.sun.javafx.application.LauncherImpl;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Classe que inicia a aplicacao de acordo com o posto definido
 * lanca a tela de splash
 *
 * @author Jose Maia
 *
 */
@SuppressWarnings("restriction")
public class Main extends Application {

    private static Stage stage;
    FadeTransition fade;

    @Override
    public void start(Stage primaryStage) {
        this.showSplashScreean(primaryStage);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, FAPreloader.class, args);
    }

    public static Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        Main.stage = stage;
    }

    
    @Override
    public void stop() throws Exception {
        super.stop();

        
    }

    /**
     * fecha a janela com o comando esc
     *
     * @param primaryStage
     */
    public void closeWindowKeyCommand(Stage primaryStage) {
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                
                stage.close();
            }
        });
    }

    /**
     * exibe a tela de splash duarante a chamada a tela inicial
     *
     * @param primaryStage
     */
    private void showSplashScreean(Stage primaryStage) {

        try {

            Image applicationIcon = new Image("com/br/ilegra/filenalyze/icons/logo.png");
            primaryStage.getIcons().add(applicationIcon);

            // Carrega a tela
            FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/com/br/ilegra/filenalyze/view/splash.fxml"));
            StackPane splashPane = splashLoader.load();

            // Cria a Janela do Splash
            // Define como transparente para que n�o apare�a decora��o de janela (maximizar, minimizar)
            Stage splashStage = new Stage(StageStyle.TRANSPARENT);
            final Scene scene = new Scene(splashPane);
            scene.setFill(Color.TRANSPARENT); // Define que a cor do painel root seja transparente para que d� o efeito de sombra

            splashStage.setScene(scene);

            // Cria o servi�o para rodar alguma tarefa em background enquanto o splash � mostrado (no caso somente um delay de 10s)
            Service<Boolean> splashService = new Service<Boolean>() {

                // Mostra o splash quando o servi�o for iniciado
                @Override
                public void start() {
                    super.start();
                    splashStage.show(); // mostra a janela
                }

                // Delay de 1 segundos 
                @Override
                protected Task<Boolean> createTask() {
                    return new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                            Thread.sleep(5000);
                            return true;
                        }
                    };
                }

                // Quando a tarefa for finalizada fecha o splash e mostra a tela principal
                @Override
                protected void succeeded() {
                    splashStage.close();  // Fecha o splash
                    try {
                        showMainScreean(primaryStage); // Chama a tela principal
                        fade.play();
                    } catch (Exception ex) {
                    }
                }
            };

            splashService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exibe a tela inicial de acordo com a tela setada no arquivo de
     * propriedade
     */
    private void showMainScreean(Stage primaryStage) {
        try {

            String path = "";
            Region root = null;

            String version = AppProperties.getProperties(AppProperties.PROPERTIES_SOFTWARE_VERSION);
            primaryStage.setTitle(Dialog.TITLE_WINDOW_SNMP_MAIN + ": V " + version );

            path = "/com/br/ilegra/filenalyze/view/file_analyze_view_full.fxml";
            root = (BorderPane) FXMLLoader.load(getClass().getResource(path));
            setStyleAndShowWindow(primaryStage, root, false, false);

        } catch (Exception e) {
            e.printStackTrace();
            Dialog.showMsg(AlertType.ERROR, Dialog.MSG_ERROR_FILE_PROPERTIES, Dialog.ERROR);
        }
    }

    /**
     * monta o formato da tela e o estilo de exibicao
     *
     * @param primaryStage
     * @param root
     * @param applyTheme
     * @param showMaximized
     */
    private void setStyleAndShowWindow(Stage primaryStage, Region root, boolean showMaximized, boolean isUndecorate) {

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/br/ilegra/filenalyze/view/application.css").toExternalForm());

        primaryStage.setScene(scene);
        this.setStage(primaryStage);
        primaryStage.setMaximized(showMaximized);

        if (isUndecorate) {
            //esconde os botoes de maximizar, minimizar e fechar
            primaryStage.initStyle(StageStyle.UNDECORATED);

            //colocar a chanela na parte inferior da tela
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

            double x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 0.0;
            double y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.8;

            System.out.println("bounds.getWidth(): " + bounds.getWidth());
            System.out.println("bounds.getHeight(): " + bounds.getHeight());
            System.out.println("scene.getHeight(): " + scene.getHeight());
            System.out.println("scene.getHeight(): " + scene.getHeight());
            System.out.println("bounds.getHeight() - scene.getHeight(): " + (bounds.getHeight() - scene.getHeight()));
            System.out.println("(bounds.getHeight() - scene.getHeight()) * 0.8: " + (bounds.getHeight() - scene.getHeight()) * 0.8);
            System.out.println("Y: " + y);
            
            y = y + 45;
            
            System.out.println("Y: " + y);
            
            stage.setX(x);
            stage.setY(y);
            stage.setWidth(bounds.getWidth());

        } else {
            this.closeWindowKeyCommand(primaryStage);
        }

        primaryStage.show();
    }
}
