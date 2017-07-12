/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package techvet.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.Utilizador;
import techvet.ConsultasCell;

/**
 * FXML Controller class
 *
 * @author rike4
 */
public class ListaConsultasController implements Initializable {
    
    @FXML
    private ListView listaConsultas;
    
    public ListaConsultasController () { }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            popularTabela();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
    private void popularTabela() throws Exception {
        Utilizador user = new Utilizador();
        user.readByNome("Admin");
        ObservableList<Utilizador> consultas = FXCollections.observableArrayList();
        consultas.add(user);
        listaConsultas.setItems(consultas);
        listaConsultas.setCellFactory(param -> new ConsultasCell());
    }
    
}
