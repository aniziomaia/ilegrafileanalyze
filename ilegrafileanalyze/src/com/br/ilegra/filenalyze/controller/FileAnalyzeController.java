package com.br.ilegra.filenalyze.controller;

import com.br.ilegra.filenalyze.resource.AppProperties;
import com.br.ilegra.filenalyze.service.FileMonitorService;
import com.br.ilegra.filenalyze.utility.Utility;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author José Maia
 *
 */
public class FileAnalyzeController implements Initializable {

    @FXML
    private TextArea taLog;

    @FXML
    private Label lbMessage;

    @FXML
    private Pane pnHeader;

    @FXML
    private Pane pnCenter;

    @FXML
    private Pane pnFooter;

    @FXML
    private ProgressIndicator piLoading;
    
    @FXML
    private ImageView iwAnimation;

    private final String pathFileDataIn = AppProperties.getProperties(AppProperties.PATH_FILE_IN);
    private final String pathFileDataOut = AppProperties.getProperties(AppProperties.PATH_FILE_OUT);
    private final String pathFileDataTemp = AppProperties.getProperties(AppProperties.PATH_FILE_TEMP);

    private boolean isStartedMonitoringFolder = false;
    private boolean allowStartProcess;

    /**
     * inicia a tela e prepara as funcionalidades para atender as necessidades
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.setInitialScreen();
        if(!Utility.checkJnotifyDLL(taLog, lbMessage)){
            
        }
    }
   
    private void setInitialScreen() {

        Utility.changeStyleScreenInit(pnCenter);
        Utility.changeStyleScreenInit(pnFooter);
        Utility.changeStyleScreenInit(pnHeader);

        taLog.getStyleClass().add("log");
        taLog.setEditable(false);

        taLog.textProperty().addListener(new ChangeListener<Object>() {

            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                taLog.setScrollTop(Double.MAX_VALUE);
            }
        });
        
        try{
            
            Path path = Paths.get(pathFileDataOut + "out.txt");
            if(Files.exists(path)){
                Files.delete(path);
            }
            Files.createDirectories(Paths.get(pathFileDataOut));
            Files.createFile(path).toFile();
            
        }catch(Exception e){
            e.printStackTrace();
            Utility.setTheTextLog(taLog, "Problema ao criar o arquivo: " + pathFileDataOut + "out.txt" + "\n");
        }
        
        try{
            
            Path path = Paths.get(pathFileDataTemp + "out.txt");
            if(Files.exists(path)){
                Files.delete(path);
            }
            Files.createDirectories(Paths.get(pathFileDataTemp));
            Files.createFile(path).toFile();
            
        }catch(Exception e){
            e.printStackTrace();
             Utility.setTheTextLog(taLog, "Problema ao criar o arquivo: " + pathFileDataTemp + "out.txt" + "\n");
        }
        
        try{
            
            Path path = Paths.get(pathFileDataIn);
            if(!Files.exists(path)){
                Utility.setTheTextLog(taLog, "O diretório para monitoramento não existe: " + pathFileDataIn+ "\n");
                Utility.setMessage(lbMessage, "Diretório de monitoramento nao existe: " + pathFileDataIn, AlertType.NONE);
            }else{
                piLoading.setVisible(true);
                new FileMonitorService(pathFileDataIn, taLog, lbMessage, pnCenter,pnFooter,pnHeader).startMonitor();
                piLoading.setVisible(false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
