/*
 * 
 */
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import model.Consulta;
import techvet.ConsultasCell;
import techvet.DocFXML;
import techvet.Utils;

/**
 * @author Henrique Faria e Sérgio Araújo
 */
public class HorarioConsultasController implements Initializable {

    @FXML
    private ListView listaHorario;
    @FXML
    private DatePicker fieldData;
    
    private final Pane content;
    private ObservableList<Consulta> consultaObservableList;
    
    public HorarioConsultasController(Pane content) {
        this.content = content;
        consultaObservableList = FXCollections.observableArrayList();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preencheData();
       
        fieldData.valueProperty().addListener((ov, valorAntigo, novoValor) -> {
            if(novoValor != null) {
               consultaObservableList.setAll(fetchConsultasPorDia(novoValor));
               listaHorario.refresh();
            }
        });
        
        listaHorario.setCellFactory(horarioListView -> new ConsultasCell());
        
        listaHorario.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Consulta>() { 
            @Override
            public void changed(ObservableValue<? extends Consulta> observable, Consulta oldValue, Consulta newValue) {
                if (newValue != null) {
                    Initializable controller = new ProcessarConsultaController(newValue, content);
                    try {
                        Utils.mudaContentPara(DocFXML.PROCESSARCONSULTA, controller, content);
                    } catch (IOException e) {}
                }
            }
            
        });
        
        listaHorario.setItems(consultaObservableList);
    }    
    
    private void preencheData() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        fieldData.setValue(localDate);
        
        consultaObservableList.setAll(fetchConsultasPorDia(localDate));
    }
    
    private List<Consulta> fetchConsultasPorDia(LocalDate data) {
       List<Consulta> listaConsultas = Consulta.readByData(data);
       return listaConsultas;
    }
}
