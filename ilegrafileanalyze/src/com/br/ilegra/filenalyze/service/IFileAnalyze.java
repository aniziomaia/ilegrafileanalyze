/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.ilegra.filenalyze.service;

import javafx.scene.control.TextArea;

/**
 *
 * @author ICTS
 */
public interface IFileAnalyze {
    
    public abstract boolean analyzeFileSales(String rootPath, String name, TextArea taLog);
}
