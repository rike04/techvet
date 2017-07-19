/*
 * 
 */

package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TextField fieldDataE;
    @FXML
    private TextField fieldDataS;
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
        fieldDataE.setText(DateTimeFormatter.ofPattern("yyyy/MM/dd").format(dataLocal));
    }
    
    private void preencherFields(Internamento i) {
        fieldNomePaciente.setText(consulta.getPaciente().getNome());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fieldDataE.setText(sdf.format(i.getDatae()));
        if (i.getDatas() != null) {
            fieldDataS.setText(sdf.format(i.getDatas()));
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
        try {
            Date.valueOf(fieldDataE.getText());
        } catch (Exception e) {
            saoValidos = false;
        }
        
        if (!fieldDataS.getText().trim().isEmpty()) {
            try {
                Date.valueOf(fieldDataS.getText());
            } catch (Exception e) {
                saoValidos = false;
            }
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
        ListaInternamentosController controller = new ListaInternamentosController(false);
        Utils.mudaContentPara(DocFXML.LISTAINTERNAMENTOS, controller, content);
    }

    private void inserirInternamentoBD() {
        Internamento i = new Internamento();
        i.setIdConsulta(consulta);
        i.setIdPaciente(consulta.getPaciente());
        try {
            i.setDatae(Date.valueOf(fieldDataE.getText()));
            if (!fieldDataS.getText().isEmpty()) {
                i.setDatas(Date.valueOf(fieldDataS.getText()));
            }
        } catch (IllegalArgumentException e) {
            return ;
        }
        i.setGuiamed(fieldGuia.getText());
        i.setObs(fieldObsv.getText());
        consulta.setInternamento(i);
        consulta.updateT();
    }    
}
