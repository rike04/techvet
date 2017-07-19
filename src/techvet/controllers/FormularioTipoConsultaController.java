
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import model.TipoConsulta;
import techvet.DocFXML;
import techvet.Utils;

/**
 * FXML Controller class
 *
 * @author rike4
 */
public class FormularioTipoConsultaController implements Initializable {

    @FXML 
    private TextField fieldNome;
    @FXML
    private TextField fieldValor;

    private final Pane content;
    
    public FormularioTipoConsultaController(Pane content) {
        this.content = content;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fieldNome.addEventFilter(KeyEvent.KEY_TYPED, Utils.validacaoLimiteMax(40));
        fieldValor.addEventFilter(KeyEvent.KEY_TYPED, Utils.validacaoPrecos(35));
    }    
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            insereTipoConsultaBD();
            mudaContent();
        }
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        
    }
    
    private boolean osDadosSaoValidos() {
        boolean saoValidos = true;
        
        if (fieldNome.getText().trim().isEmpty()) {
            saoValidos = false;
        }
        
        if (fieldValor.getText().trim().isEmpty()) {
            saoValidos = false;
            
        } else {
            try {
                Double.parseDouble(fieldValor.getText());
            } catch (NumberFormatException e) {
                saoValidos = false;
            }
        }
        
        return saoValidos;
    }
    
    private void insereTipoConsultaBD() {
        TipoConsulta tipo = new TipoConsulta();
        tipo.setNome(fieldNome.getText());
        tipo.setValor(Double.parseDouble(fieldValor.getText()));
        tipo.createT();
    }
    
    private void mudaContent() {
        Initializable controller = new ListaConsultasController(false, content);
        try {
            Utils.mudaContentPara(DocFXML.LISTACONSULTAS, controller, content);
        } catch (IOException e) {
        }
    }
}
