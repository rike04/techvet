/*
 *
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Consulta;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Utils;

/**
 * @author rike4
 */
public class ListaConsultasController implements Initializable {
        
    @FXML
    private TableView<Consulta> tabelaConsultas;
    @FXML
    private TableColumn<Consulta, String> colTipoConsulta;
    @FXML
    private TableColumn<Consulta, String> colCliente;
    @FXML
    private TableColumn<Consulta, String> colPaciente;
    @FXML
    private TableColumn<Consulta, String>  colData;
    @FXML
    private TableColumn<Consulta, String>  colLocal;
    @FXML
    private TableColumn<Consulta, Double> colValor;
    @FXML
    private TableColumn<Consulta, String> colPago;
    @FXML
    private Button botaoSelecionar;
    @FXML
    private Button botaoCancelar;
    
    private boolean foiConfirmado;
    private final boolean devolveEscolha;
    
    private final Pane content;
    
    public ListaConsultasController (boolean devolveEscolha, Pane content) { 
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
        
        tabelaConsultas.setPlaceholder(new Label("Não existem consultas registadas."));
        
        tabelaConsultas.setRowFactory( tv -> {
            TableRow<Consulta> linha = new TableRow<>();
            linha.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!linha.isEmpty())) {
                    Consulta c = linha.getItem();
                    if (c.getEstado() == 0) {
                        abrirProcessarConsulta(c);
                    }
                }
            });
          return linha;
        });
        
        colTipoConsulta.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getTipoConsulta().getNome()));
        colCliente.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getPaciente().getIdCliente().getNome()));
        colPaciente.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getPaciente().getNome()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        colData.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(sdf.format(dadosCell.getValue().getDatahora())));
        colLocal.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getLocal()));        
        colValor.setCellValueFactory(dadosCell -> 
                new SimpleDoubleProperty(dadosCell.getValue().getValor()).asObject());
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
    
    private void abrirProcessarConsulta(Consulta c) {
        FormularioConsultaController controller = new FormularioConsultaController(c, content);
        try {
            Utils.mudaContentPara(DocFXML.FORMULARIOCONSULTA, controller, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
