/*
 * 
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.Consulta;
import model.Internamento;
import techvet.DocFXML;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
 */ 

public class FormularioInternamentoController implements Initializable {
    
    @FXML 
    private TextField fieldNomePaciente;
    @FXML
    private DatePicker fieldDataE;
    @FXML
    private DatePicker fieldDataS;
    @FXML 
    private TextArea fieldObsv;
    @FXML
    private TextArea fieldGuia;
    @FXML
    private CheckBox boxSaida;
    
    private final Pane content;
    private final Consulta consulta;
    
    public FormularioInternamentoController(Pane content, Consulta consulta) {
        this.content = content;
        this.consulta = consulta;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        if (consulta.getInternamento() != null) {
            preencherFields(consulta.getInternamento());
        } else {
                preencherLabels();
        }
        BooleanBinding desativaBotao = boxSaida.selectedProperty().not();
        fieldDataS.disableProperty().bind(desativaBotao);
    }    
    
    private void preencherLabels() {
        fieldNomePaciente.setText(consulta.getPaciente().getNome());
        LocalDate dataLocal = LocalDate.now();
        fieldDataE.setValue(dataLocal);
    }
    
    private void preencherFields(Internamento i) {
        fieldNomePaciente.setText(consulta.getPaciente().getNome());
        fieldObsv.setText(i.getObs());
        fieldGuia.setText(i.getGuiamed());
        fieldDataE.setValue(Utils.toLocalDate(i.getDatae()));
        if (i.getDatas() != null) {
            desativarCampos();
            fieldDataS.setValue(Utils.toLocalDate(i.getDatas()));
        }
    }
    
    private void desativarCampos() {
        fieldNomePaciente.setDisable(true);
        fieldObsv.setDisable(true);
        fieldGuia.setDisable(true);
        fieldDataE.setDisable(true);
        fieldDataS.setDisable(true);
        boxSaida.setDisable(true);
    }
       
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            inserirInternamentoBD();
            try {
                mudarContent();
            } catch (IOException ex) {
                Logger.getLogger(FormularioInternamentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private Boolean osDadosSaoValidos() {
        boolean saoValidos = true;     
        
        if (fieldDataE.getValue() == null) {
            saoValidos =true;
        }
        
        return saoValidos;
    }
    
    @FXML 
    public void cliqueCancelar(ActionEvent event) {
        try {
            mudarContent();
        } catch (IOException e) {
            Logger.getLogger(FormularioInternamentoController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void mudarContent() throws IOException{
        ListaInternamentosController controller = new ListaInternamentosController(content);
        Utils.mudaContentPara(DocFXML.LISTAINTERNAMENTOS, controller, content);
    }

    private void inserirInternamentoBD() {
        Internamento i = consulta.getInternamento();
        if (i == null) i = new Internamento(); 
        i.setIdConsulta(consulta);
        i.setIdPaciente(consulta.getPaciente());
        
        i.setDatae(Utils.toDate(fieldDataE.getValue()));

        if (fieldDataS.getValue() != null) {
            i.setDatas(Utils.toDate(fieldDataS.getValue()));
        }
        
        i.setGuiamed(fieldGuia.getText());
        i.setObs(fieldObsv.getText());
        consulta.setInternamento(i);
        consulta.updateT();
    }    
}
