/*
 * 
 */
package techvet.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.Consulta;
import techvet.ConsultasCell;

/**
 * @author Henrique Faria e Sérgio Araújo
 */
public class HorarioConsultasController implements Initializable {

    @FXML
    private ListView listaHorario;
    
    private ObservableList<Consulta> consultaObservableList;
    
    public HorarioConsultasController() {
        consultaObservableList = FXCollections.observableArrayList();
        consultaObservableList.setAll(Consulta.retrieveAll());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       listaHorario.setCellFactory(horarioListView -> new ConsultasCell());
       listaHorario.setItems(consultaObservableList);
    }    
    
}
