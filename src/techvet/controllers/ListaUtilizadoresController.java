/*
 *
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Utilizador;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
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
    @FXML
    private TextField fieldFiltroProcura;
    
    private boolean foiConfirmado;
    private final boolean devolveEscolha;
    
    private final Pane content;
    
    public ListaUtilizadoresController (boolean devolveEscolha, Pane content) { 
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
        this.content = content;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botaoSelecionar.setVisible(devolveEscolha);
        botaoSelecionar.setDisable(!devolveEscolha);
        
        botaoCancelar.setVisible(devolveEscolha);
        botaoCancelar.setDisable(!devolveEscolha);
        
        tabelaUtilizadores.setPlaceholder(new Label("Não existem utilizadores registados."));

        tabelaUtilizadores.setRowFactory( tv -> {
            TableRow<Utilizador> linha = new TableRow<>();
            linha.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!linha.isEmpty())) {
                    Utilizador u = linha.getItem();
                    abreFormUtilizador(u);
                }
            });
          return linha;
        });
        
        colUsername.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getUsername()));
        colNome.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getNome()));
        colFuncao.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getFuncao()));
        
        ObservableList<Utilizador> listaUtilizadores = FXCollections.observableList(leListaUtilizadores());
        FilteredList<Utilizador> listaFiltrada = new FilteredList<>(listaUtilizadores, p -> true);
        fieldFiltroProcura.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(utilizador -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }         
                String filtro = newValue.toLowerCase();
                if (utilizador.getNome().toLowerCase().contains(filtro)) {
                    return true;
                }
                
                if (utilizador.getUsername().toLowerCase().contains(filtro)) {
                    return true;
                }
                
                return false;
            });
        });
        SortedList<Utilizador> sortedList = new SortedList<>(listaFiltrada);
        sortedList.comparatorProperty().bind(tabelaUtilizadores.comparatorProperty());
        
        tabelaUtilizadores.setItems(sortedList);
        
        GUIUtils.autoFitTable(tabelaUtilizadores);
    }
    
    private void abreFormUtilizador(Utilizador u) {
        Initializable controller = new FormularioUtilizadorController(content, u);
        try {
            Utils.mudaContentPara(DocFXML.FORMULARIOUTILIZADOR, controller, content);
        } catch (IOException e) {}
    }

    private List<Utilizador> leListaUtilizadores() {
        List<Utilizador> listaUtilizadores = new ArrayList<>();
        try {
            listaUtilizadores = Utilizador.retrieveAll();
        } catch (Exception e) {
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
