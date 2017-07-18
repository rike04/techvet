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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Internamento;
import techvet.GUIUtils;

/**
 * FXML Controller class
 *
 * @author rike4
 */
public class ListaInternamentosController implements Initializable {
        
    @FXML
    private TableView<Internamento> tabela;
    @FXML
    private TableColumn<Internamento, String> colPaciente;
    @FXML
    private TableColumn<Internamento, String> colConsulta;
    @FXML
    private TableColumn<Internamento, String> colGuia;
    @FXML
    private TableColumn<Internamento, String> colDataE;
    @FXML
    private TableColumn<Internamento, String> colDataS;
    @FXML
    private Button botaoSelecionar;
    @FXML
    private Button botaoCancelar;
    
    private boolean foiConfirmado;
    private final boolean devolveEscolha;
    
    public ListaInternamentosController (boolean devolveEscolha) { 
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botaoSelecionar.setVisible(devolveEscolha);
        botaoSelecionar.setDisable(!devolveEscolha);
        
        botaoCancelar.setVisible(devolveEscolha);
        botaoCancelar.setDisable(!devolveEscolha);
        
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setPlaceholder(new Label("Não existem internamentos registados"));
        
        colConsulta.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getIdConsulta().getDatahora().toString()));
        colPaciente.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getIdPaciente().getNome()));
        colDataE.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getDatae().toString()));
        colDataS.setCellValueFactory(dadosCell -> {
            SimpleStringProperty string = new SimpleStringProperty();       
            Internamento i = (Internamento) dadosCell.getValue();
            if (i.getDatas() != null) {
                string.set(i.getDatas().toString());
            }
            return string;
        });        
        colGuia.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getGuiamed()));
        
        tabela.setItems(FXCollections.observableList(leListaInternamentos()));
        
        GUIUtils.autoFitTable(tabela);
    }

    private List<Internamento> leListaInternamentos() {
        List<Internamento> lista = new ArrayList<>();
        try {
            lista = Internamento.retrieveAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public boolean foiSelecionadaOpcao() {
        return foiConfirmado;
    }
    
    public Internamento getConsultaSelecionada() {
        return tabela.getSelectionModel().getSelectedItem();
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
