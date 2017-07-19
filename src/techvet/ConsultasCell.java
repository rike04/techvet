/*
 * 
 */

package techvet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import model.Consulta;

/**
 * @author Henrique Faria e Sérgio Araújo
 */
public class ConsultasCell extends ListCell<Consulta>{
    
    @FXML
    private Label labelPaciente;
    @FXML 
    private Label labelData;
    @FXML
    private Label labelTipoConsulta;
    @FXML
    private GridPane gridPane;
    
    private FXMLLoader loader;
    
    @Override 
    public void updateItem (Consulta consulta, boolean empty) {
        super.updateItem(consulta, empty);
        if (empty || consulta == null) {
            setText(null);
            setGraphic(null);
        } else {    
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource(DocFXML.CONSULTACELL.getPath()));
                loader.setController(this);
            }
            
            try {
                loader.load();
            } catch (IOException e) {
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            labelData.setText(sdf.format(consulta.getDatahora()));
            labelPaciente.setText(consulta.getPaciente().getNome());
            labelTipoConsulta.setText(consulta.getTipoConsulta().getNome());
            
            setText(null);
            setGraphic(gridPane);
        } 
    }
    
}
