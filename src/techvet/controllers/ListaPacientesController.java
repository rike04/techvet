/*
 * 
 */
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Cliente;
import model.Paciente;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
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
    private TableColumn<Paciente, Circle> colEstado;
    @FXML
    private TableColumn<Paciente, String> colCliente;
    @FXML
    private Button botaoSelecionar;
    @FXML
    private Button botaoCancelar;
    
    private boolean foiConfirmado;
    private final boolean devolveEscolha; 
    
    private final Pane content;
    
    private Cliente cliente;
    
    public ListaPacientesController(boolean devolveEscolha) {
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
        this.content = null;
    }
    
    public ListaPacientesController(boolean devolveEscolha, Pane content) {
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
        this.content = content;
    }
    
    public ListaPacientesController(boolean devolveEscolha, Cliente cliente) {
        this.foiConfirmado = false;
        this.devolveEscolha = devolveEscolha;
        this.cliente = cliente;
        this.content = null;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botaoSelecionar.setVisible(devolveEscolha);
        botaoSelecionar.setDisable(!devolveEscolha);
        
        botaoCancelar.setVisible(devolveEscolha);
        botaoCancelar.setDisable(!devolveEscolha);
        
        tabelaPacientes.setPlaceholder(new Label("Não existem pacientes registados"));
        
        tabelaPacientes.setRowFactory( tv -> {
            TableRow<Paciente> linha = new TableRow<>();
            linha.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!linha.isEmpty()) && content != null) {
                    Paciente p = linha.getItem();
                    FormularioPacienteController controller = new FormularioPacienteController(content, p);
                    try {
                        Utils.mudaContentPara(DocFXML.FORMULARIOPACIENTE, controller, content);
                    } catch (IOException e) {
                    }
                }
            });
          return linha;
        });
        
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
        
        colEstado.setCellFactory((TableColumn<Paciente, Circle> p) -> {
            TableCell<Paciente, Circle> tc = new TableCell<Paciente, Circle>(){
                @Override
                public void updateItem(Circle item, boolean empty) {
                    if (item != null){
                        setGraphic(item);
                    }
                }
            };
            tc.setAlignment(Pos.CENTER);
            return tc;
        });
       
 
        colEstado.setCellValueFactory(dadosCell -> {
            Circle circle = new Circle(5);
            Paciente p = (Paciente) dadosCell.getValue();
            if (p.getEstado() == 0) {
                circle.setFill(Color.RED);
            } else {
                circle.setFill(Color.GREEN);
            }
            SimpleObjectProperty<Circle> obj = new SimpleObjectProperty<>(circle);
            return obj;
        });
        colCliente.setCellValueFactory(dadosCell -> 
                new SimpleStringProperty(dadosCell.getValue().getIdCliente().getNome()));
        
        tabelaPacientes.setRowFactory((TableView<Paciente> param) -> {
            final TableRow<Paciente> row = new TableRow<Paciente>() {
                @Override
                protected void updateItem(Paciente item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        if (item.getEstado() == (short) 0) setStyle("-fx-text-fill: red");
                    }
                }
            };
            return row;
        });        
        
        tabelaPacientes.setItems(FXCollections.observableList(leListaPacientes()));
        
        GUIUtils.autoFitTable(tabelaPacientes);
    }    
    
    private List<Paciente> leListaPacientes() {
        List<Paciente> listaPacientes = new ArrayList<>();
        if (cliente == null) {
            try {
                listaPacientes = Paciente.retrieveAll();
            } catch (Exception e) {
            }
        } else listaPacientes = cliente.getListaPacientes();
        
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
