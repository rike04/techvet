/*
 * 
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        
    }    
    
    private void preencherLabels() {
        fieldNomePaciente.setText(consulta.getPaciente().getNome());
        LocalDate dataLocal = LocalDate.now();
        fieldDataE.setValue(dataLocal);
    }
    
    private void preencherFields(Internamento i) {
        fieldNomePaciente.setText(consulta.getPaciente().getNome());
        
        LocalDate lsE = Instant.ofEpochMilli(i.getDatae().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        fieldDataE.setValue(lsE);
        if (i.getDatas() != null) {
            LocalDate ldS = Instant.ofEpochMilli(i.getDatas().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            fieldDataS.setValue(ldS);
        }
        fieldObsv.setText(i.getObs());
        fieldGuia.setText(i.getGuiamed());
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
        
        Date dataE = Date.from(fieldDataE.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        
        i.setDatae(dataE);

        if (fieldDataS.getValue() != null) {
            Date dataS = Date.from(fieldDataS.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            i.setDatas(dataS);
        }
        
        i.setGuiamed(fieldGuia.getText());
        i.setObs(fieldObsv.getText());
        consulta.setInternamento(i);
        consulta.updateT();
    }    
}
