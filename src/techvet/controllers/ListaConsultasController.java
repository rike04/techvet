/*
 *
 */

package techvet.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Consulta;
import techvet.GUIUtils;

/**
 * FXML Controller class
 *
 * @author rike4
 */
public class ListaConsultasController implements Initializable {
        
    @FXML
    private TableView<Consulta> tabelaConsultas;
    @FXML
    private TableColumn<Consulta, String> colCliente;
    @FXML
    private TableColumn<Consulta, String> colPaciente;
    @FXML
    private TableColumn<Consulta, String>  colData;
    @FXML
    private TableColumn<Consulta, String>  colLocal;
    @FXML
    private TableColumn<Consulta, String> colValor;
    @FXML
    private TableColumn<Consulta, String> colPago;
    @FXML
    private Button botaoSelecionar;
    @FXML
    private Button botaoCancelar;
    
    private boolean foiConfirmado;
    private final boolean devolveEscolha;
    
    public ListaConsultasController (boolean devolveEscolha) { 
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botaoSelecionar.setVisible(devolveEscolha);
        botaoSelecionar.setDisable(!devolveEscolha);
        
        botaoCancelar.setVisible(devolveEscolha);
        botaoCancelar.setDisable(!devolveEscolha);
        
        tabelaConsultas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        colCliente.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getIdPaciente().getIdCliente().getNome()));
        colPaciente.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getIdPaciente().getNome()));
        colData.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getDatahora().toString()));
        colLocal.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getLocal()));
        
        colPago.setCellValueFactory(dadosCell -> {
            SimpleStringProperty string = new SimpleStringProperty();
            Consulta c = (Consulta) dadosCell.getValue();
            if (c.getPago() == 1) {
                string.set("Pago");
            } else {
                string.set("Por pagar");
            }
            return string;
        });
        colValor.setCellValueFactory(dadosCell -> new SimpleStringProperty(dadosCell.getValue().getValor().toString()));
        
        tabelaConsultas.setItems(FXCollections.observableList(leListaConsultas()));
        
        GUIUtils.autoFitTable(tabelaConsultas);
    }

    private List<Consulta> leListaConsultas() {
        List<Consulta> listaConsultas = new ArrayList<>();
        try {
            listaConsultas = Consulta.retrieveAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaConsultas;
    }
    
    public boolean foiSelecionadaOpcao() {
        return foiConfirmado;
    }
    
    public Consulta getConsultaSelecionada() {
        return tabelaConsultas.getSelectionModel().getSelectedItem();
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
