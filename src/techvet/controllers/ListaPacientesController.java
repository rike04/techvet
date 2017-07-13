/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Cliente;
import model.Paciente;

/**
 * FXML Controller class
 *
 * @author rike4
 */
public class ListaPacientesController implements Initializable {

    @FXML
    private TableView<Paciente> tabelaPacientes;
    @FXML
    private TableColumn<Paciente, String> colNome;
    @FXML
    private TableColumn<Paciente, String> colEspecie;
    @FXML
    private TableColumn<Paciente, String> colRaca;
    @FXML
    private TableColumn<Paciente, String> colSexo;
    @FXML
    private TableColumn<Paciente, String> colCor;
    @FXML
    private TableColumn<Paciente, String> colEstado;
    @FXML
    private TableColumn<Paciente, String> colCliente;
    @FXML
    private Button botaoSelecionar;
    @FXML
    private Button botaoCancelar;
    
    private boolean foiConfirmado;
    private final boolean devolveEscolha; 
    
    public ListaPacientesController(boolean devolveEscolha) {
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botaoSelecionar.setVisible(devolveEscolha);
        botaoSelecionar.setDisable(!devolveEscolha);
        
        botaoCancelar.setVisible(devolveEscolha);
        botaoCancelar.setDisable(!devolveEscolha);
        
        colNome.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getNome()));
        colEspecie.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getEspecie()));
        colRaca.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getRaca()));
        colSexo.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getSexo()));
        colCor.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getCor()));
       
        //Esta a atribuir vivo ou morto porque o tipo de variavel SHORT nao existe em JAVAFX
        colEstado.setCellValueFactory(dadosCell -> {
            SimpleStringProperty property = new SimpleStringProperty();
            Paciente p = (Paciente) dadosCell.getValue();
            if (p.getEstado() == 0) {
                
                property.set("Morto");
            } else {
                property.set("Vivo");
            }
            return property;
        });
        colCliente.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getIdCliente().getNome()));
        
        //Muda a cor da linha para verde se o paciente estiver vivo e para vermleho se estiver morto
        //ALTERAR: Ficar apenas a celula da coluna Estado verde ou vermelha 
        tabelaPacientes.setRowFactory((TableView<Paciente> param) -> {
            final TableRow<Paciente> row = new TableRow<Paciente>() {
                @Override
                protected void updateItem(Paciente item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        if (item.getEstado() == 0) {
                            setStyle("-fx-background: red");
                        } else if (item.getEstado() == 1) {
                            setStyle("-fx-background: green");                        
                        }
                    }
                }
            };
            return row;
        });        
        tabelaPacientes.getItems().setAll(leListaPacientes());
    }    
    
    private List<Paciente> leListaPacientes() {
        List<Paciente> listaPacientes = new ArrayList<>();
        try {
            listaPacientes = Paciente.retrieveAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPacientes;
    }
    
    public boolean foiSelecionadaOpcao() {
        return foiConfirmado;
    }
    
    public Paciente getPacienteSelecionado() {
        return tabelaPacientes.getSelectionModel().getSelectedItem();
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
