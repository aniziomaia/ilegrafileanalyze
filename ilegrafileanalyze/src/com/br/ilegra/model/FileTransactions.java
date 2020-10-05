/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.ilegra.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ICTS
 */
public class FileTransactions {
    
    private List<Sale> salesList = new ArrayList<Sale>();
    private Set<Client> clientList = new HashSet<Client>();
    private Set<Salesman> salesmanList = new HashSet<Salesman>();
    private Map<Double,Sale> saleListValue= new HashMap<Double,Sale>();

    public FileTransactions() {
    }

    public List<Sale> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<Sale> salesList) {
        this.salesList = salesList;
    }

    public Set<Client> getClientList() {
        return clientList;
    }

    public void setClientList(Set<Client> clientList) {
        this.clientList = clientList;
    }

    public Set<Salesman> getSalesmanList() {
        return salesmanList;
    }

    public void setSalesmanList(Set<Salesman> salesmanList) {
        this.salesmanList = salesmanList;
    }

    public Map<Double,Sale> getSaleListValue() {
        return saleListValue;
    }

    public void setSaleListValue(Map<Double,Sale> saleListValue) {
        this.saleListValue = saleListValue;
    }
    
    
}
