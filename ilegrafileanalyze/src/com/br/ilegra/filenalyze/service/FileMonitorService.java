/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.ilegra.filenalyze.service;

import com.br.ilegra.filenalyze.controller.FileAnalyzeController;
import com.br.ilegra.filenalyze.utility.Dialog;
import com.br.ilegra.filenalyze.utility.Utility;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

/**
 *
 * @author ICTS
 */
public class FileMonitorService {
    
    private String path;
    private boolean isStartedMonitoringFolder = false;
    private TextArea taLog;
    private Label lbMessage;
    private Pane panes[];
    
    public FileMonitorService(String path, TextArea taLog){
        this.path = path;
        this.taLog = taLog;
    }
    
    public FileMonitorService(String path, TextArea taLog,Label lbMessage, Pane...panes){
        this.path = path;
        this.taLog = taLog;
        this.lbMessage = lbMessage;
        this.panes = panes;
    }
    
    public void startMonitor() {
        new Thread() {
            @Override
            public void run() {
                if (!isStartedMonitoringFolder) {
                    isStartedMonitoringFolder = true;
                    doMonitoring();
                }
            }
        }.start();
    }
    
    private void doMonitoring() {

        isStartedMonitoringFolder = true;
        Utility.setTheTextLog(taLog, "Monitorando o diretório " + path + "\n");

        try {

            // watch mask, specify events you care about,
            // or JNotify.FILE_ANY for all events.
            int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;

            // watch subtree?
            boolean watchSubtree = false;

            // add actual watch
            int watchID = JNotify.addWatch(path, mask, watchSubtree, new FileMonitorService.Listener());
            System.out.println("watchID: " + watchID);
            // sleep a little, the application will exit if you
            // don't (watching is asynchronous), depending on your
            // application, this may not be required
            Thread.sleep(3600000 * 10);//faz durar o monitoramento 10 horas depois disso

            // to remove watch the watch
            boolean res = JNotify.removeWatch(watchID);
            Utility.setTheTextLog(taLog, "Depois do removeWatch: " + res + "\n");
            if (!res) {
                // invalid watch ID specified.
                System.out.println("Dentro do invalid id removeWatch");
            }

            System.exit(0);//fecha o sistema sozinho

        } catch (JNotifyException | InterruptedException e) {
            e.printStackTrace();
            Utility.setTheTextLog(taLog, Dialog.MSG_ERROR_FLEX_DIR_MONITOR + path + "\n");
            System.out.println(Dialog.MSG_ERROR_FLEX_DIR_MONITOR + path);
            Utility.setMessage(lbMessage, Dialog.MSG_ERROR_FLEX_DIR_MONITOR + "\n" + path, Alert.AlertType.ERROR);
        }
        
    }
    
    /**
     * Classe anonima do framework JNotify, responsavel por monitorar um
     * diretorio
     */
    class Listener implements JNotifyListener {

        public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
            //print("Renamed " + rootPath + " : " + oldName + " -> " + newName);
        }

        public void fileModified(int wd, String rootPath, String name) {
            //print("Arquivo modificado:" + rootPath + "\\" + name + "\n");
        }

        public void fileDeleted(int wd, String rootPath, String name) {
            print("Deleted " + rootPath + " \\ " + name);
        }

        public void fileCreated(int wd, String rootPath, String name) {
            print("***Detectou um novo arquivo:" + rootPath + name + "\n");
            //aguarda meio segundo para que o sistema operacional
            //atualize as informacoes do arquivo, pelo fato de a 
            //aplicacao ter acessado ele para checar sua existencia
            Utility.setTimeToSleepByThread(1000);
            if(new FileAnalyze().analyzeFileSales(rootPath, name, taLog)){
                Utility.setTheTextLog(taLog, "O arquivo foi analizado com sucesso" + "\n");
                Utility.setMessage(lbMessage, "O arquivo foi analizado com sucesso", Alert.AlertType.NONE);
                for (Pane pane : panes) {
                    Utility.changeStyleScreenSuccess(pane);
                }
                
            }else{
                Utility.setTheTextLog(taLog, "Erro durante a analize do arquivo" + "\n");
                Utility.setMessage(lbMessage, "Erro durante a analize do arquivo", Alert.AlertType.NONE);
                for (Pane pane : panes) {
                    Utility.changeStyleScreenFail(pane);
                }
            }
        }

        void print(String msg) {
            Utility.setTheTextLog(taLog, msg + "\n");
        }
    }
    
}
