
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import model.Internamento;
import techvet.DocFXML;
import techvet.GUIUtils;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
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
    
    private final Pane content;
    
    public ListaInternamentosController (Pane content) { 
        this.content = content;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tabela.setPlaceholder(new Label("Não existem internamentos registados."));
        
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
        
        tabela.setRowFactory( tv -> {
            TableRow<Internamento> linha = new TableRow<>();
            linha.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!linha.isEmpty()) && content != null) {
                    Internamento i = linha.getItem();
                    abreFormInternamento(i);
                }
            });
          return linha;
        });

        tabela.setItems(FXCollections.observableList(leListaInternamentos()));
        
        GUIUtils.autoFitTable(tabela);
    }

    private List<Internamento> leListaInternamentos() {
        List<Internamento> lista = new ArrayList<>();
        try {
            lista = Internamento.retrieveAll();
        } catch (Exception e) {
        }
        return lista;
    }
    
    private void abreFormInternamento(Internamento i) {
        Initializable controller = new FormularioInternamentoController(content, i.getIdConsulta());
        try {
            Utils.mudaContentPara(DocFXML.FORMULARIOINTERNAMENTO, controller, content);
        } catch (IOException e) {}
    }
    
}
