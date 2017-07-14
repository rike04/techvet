/*
 *
 */

package techvet.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Utilizador;

/**
 * FXML Controller class
 *
 * @author rike4
 */

public class ListaUtilizadoresController implements Initializable {

    @FXML
    private TableView<Utilizador> tabelaUtilizadores;
    @FXML
    private TableColumn<Utilizador, String> colUsername;
    @FXML
    private TableColumn<Utilizador, String> colNome;
    @FXML
    private TableColumn<Utilizador, String>  colFuncao;
    @FXML
    private Button botaoSelecionar;
    @FXML
    private Button botaoCancelar;
    
    private boolean foiConfirmado;
    private final boolean devolveEscolha;
    
    public ListaUtilizadoresController (boolean devolveEscolha) { 
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botaoSelecionar.setVisible(devolveEscolha);
        botaoSelecionar.setDisable(!devolveEscolha);
        
        botaoCancelar.setVisible(devolveEscolha);
        botaoCancelar.setDisable(!devolveEscolha);
        
        colUsername.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getUsername()));
        colNome.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getNome()));
        colFuncao.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getFuncao()));
        
        tabelaUtilizadores.getItems().addAll(leListaUtilizadores());
    }

    private List<Utilizador> leListaUtilizadores() {
        List<Utilizador> listaUtilizadores = new ArrayList<>();
        try {
            listaUtilizadores = Utilizador.retrieveAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUtilizadores;
    }
    
    public boolean foiSelecionadaOpcao() {
        return foiConfirmado;
    }
    
    public Utilizador getUtilizadorSelecionado() {
        return tabelaUtilizadores.getSelectionModel().getSelectedItem();
    }
    
    @FXML 
    public void cliqueSelecionar(ActionEvent event) {
        foiConfirmado = true;
        fechaJanela(event);
    }
    
    @FXML 
    public void cliqueCancelar(ActionEvent event) {
        foiConfirmado = false;
        fechaJanela(event);
    }

    private void fechaJanela(Event event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
    
}
