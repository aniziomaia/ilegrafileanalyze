/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.ilegra.filenalyze.service;

import com.br.ilegra.filenalyze.resource.AppProperties;
import com.br.ilegra.filenalyze.utility.Utility;
import com.br.ilegra.model.Client;
import com.br.ilegra.model.Item;
import com.br.ilegra.model.Sale;
import com.br.ilegra.model.Salesman;
import com.br.ilegra.model.FileTransactions;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.control.TextArea;

/**
 *
 * @author ICTS
 */
public class FileAnalyze implements IFileAnalyze{

    private TextArea taLog;
    private final String pathFileDataOut = AppProperties.getProperties(AppProperties.PATH_FILE_OUT) + "out.txt";
    private final String pathFileDataTemp = AppProperties.getProperties(AppProperties.PATH_FILE_TEMP) + "out.txt";
    
    @Override
    public boolean analyzeFileSales(String rootPath, String name, TextArea taLog) {
        this.taLog = taLog;
        String separator = AppProperties.getProperties(AppProperties.CHAR_SEPARATOR);
        String file = rootPath + "\\" + name;
        Path path = Paths.get(file);
        FileTransactions fileTransaction = new FileTransactions();
        
        if(Files.exists(path)){
            try {

                Utility.setTheTextLog(taLog, "Lendo o arquivo: " + file + "\n");
                List<String> fileBody = Files.readAllLines(path, Charset.forName("UTF-8"));
                int lineNumber = 0;

                for (String line : fileBody) {
                    Utility.setTheTextLog(taLog, "Line: " + lineNumber + ": " + line.trim() + "\n");
                    lineNumber++;

                    if (line.trim().length() > 0 ) {
                        Utility.setTheTextLog(taLog, "Line: " + lineNumber + ": " + line.trim() + "\n");
                        String lineSplitted [] = line.split(separator);
                        
                        switch(lineSplitted[0]){
                            case "001":{
                                Salesman salesman = new Salesman();
                                salesman.setId(Long.valueOf(lineSplitted[0]));
                                salesman.setCpf(lineSplitted[1]);
                                salesman.setName(lineSplitted[2]);
                                salesman.setSalary(new Double(lineSplitted[3]));
                                //sale.setSalesman(salesman);
                                fileTransaction.getSalesmanList().add(salesman);
                                continue;
                                
                            }case "002":{
                                Client client = new Client();
                                client.setId(Long.valueOf(lineSplitted[0]));
                                client.setCnpj(lineSplitted[1]);
                                client.setName(lineSplitted[2]);
                                client.setBusiness(lineSplitted[3]);
                                fileTransaction.getClientList().add(client);
                                continue;
                                
                            }case "003":{
                                Sale sale = new Sale();
                                double value = 0;
                                
                                sale.setId(Long.valueOf(lineSplitted[1]));
                                String itensString = lineSplitted[2];
                                itensString = itensString.replace("[", "");
                                itensString = itensString.replace("]", "");
                                
                                Utility.setTheTextLog(taLog, "Itens: " + itensString + "\n");
                                
                                String itensStringList [] = itensString.split(",");
                                for (int i = 0; i < itensStringList.length; i++) {
                                    String itemSplited[] = itensStringList[i].split("-");
                                    Item item  = new Item(new Long(itemSplited[0]), new Long(itemSplited[1]), new Double(itemSplited[2]));
                                    sale.getItens().add(item);
                                    value = value + item.getSumValues();
                                }
                                
                                Salesman salesman = new Salesman();
                                salesman.setName(lineSplitted[3]);
                                
                                sale.setSalesman(salesman);
                                
                                fileTransaction.getSalesList().add(sale);
                                fileTransaction.getSaleListValue().put(value, sale);
                                
                                continue;
                            }
                        }
                    }
                }
                
                if(this.generateSalesAnalysisFile(fileTransaction)){
                    this.moveFileGeneratedToOutDir();
                }else{
                    return false;
                }
                Utility.setTheTextLog(taLog, "FIM LEITURA "+ "\n");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("O arquivo não foi criado: ");
                Utility.setTheTextLog(taLog, e.getMessage() + "\n");
                return false;
            }
        }
        
        return true;
    }
    
    private boolean generateSalesAnalysisFile(FileTransactions fileTransactions){
        
        System.out.println("Quantidade de clientes: " + fileTransactions.getClientList().size());
        System.out.println("Quantidade de vendedores: " + fileTransactions.getSalesmanList().size());
        
        LinkedHashMap<Double, Sale> orderedMap = fileTransactions.getSaleListValue().entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(Map.Entry::getKey, 
                    Map.Entry::getValue,(key, content) -> content,LinkedHashMap::new)); 
        
        System.out.println(orderedMap);
        System.out.println((orderedMap.entrySet().toArray()[orderedMap.size() -1]));
        System.out.println("Venda Mais cara: " + String.valueOf(orderedMap.entrySet().toArray()[orderedMap.size() - 1]));
        
        final long count = orderedMap.entrySet().stream().count();
        Sale sale = orderedMap.entrySet().stream().skip(count - 1).findFirst().get().getValue();
        System.out.println("Venda Mais cara: " + sale.getId());
        
        
        try {

            Utility.setTheTextLog(taLog, "Criando do arquivo temporario:" + pathFileDataTemp + "\n");

            Path p = Paths.get(pathFileDataTemp);
            StringBuilder text = new StringBuilder();
            text.append("Quantidade de clientes: " + fileTransactions.getClientList().size() + "\n");
            text.append("Quantidade de vendedores: " + fileTransactions.getSalesmanList().size() + "\n");
            text.append("Id da venda mais cara: " + sale.getId() + "\n");
            
            Files.write(p, text.toString().getBytes(), StandardOpenOption.WRITE);

            Utility.setTheTextLog(taLog, "Od dados foram gravado no backup: " + "\n");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("O arquivo não foi escrito no backup:: ");
            Utility.setTheTextLog(taLog, "O arquivo não foi escrito no backup: " + "\n");
            return false;
        }
        return true;
    }
    
   /**
     * move o arquivo de backup que contem todos dados processados para o
     * diretorio onde sera especificado para leitura dos dados usados para teste
     *
     * @return
     */
    private boolean moveFileGeneratedToOutDir() {

        try {

            Utility.setTheTextLog(taLog, "Movendo o arquivo:" + pathFileDataTemp + "\n");
            Utility.setTheTextLog(taLog, "Para o arquivo:" + pathFileDataOut + "\n");

            //movendo o arquivo para o diretorio final
            Path pathFrom = Paths.get(pathFileDataTemp);
            Path pathTo = Paths.get(pathFileDataOut);

            Path result = Files.move(pathFrom, pathTo, StandardCopyOption.REPLACE_EXISTING);

            Utility.setTheTextLog(taLog, "Resultado da copia: " + result + "\n");

            if (result != null) {

                Utility.setTheTextLog(taLog, "O arquivo foi copiado: " + result + "\n");
                return true;

            } else {
                Utility.setTheTextLog(taLog, "O arquivo não foi copiado: " + result + "\n");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Utility.setTheTextLog(taLog, "O arquivo não foi copiado do backup para o teste: " + "\n");
            return false;
        }
    }
}
