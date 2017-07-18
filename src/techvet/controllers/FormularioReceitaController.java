/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package techvet.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Consulta;
import model.Receita;
import techvet.Util;

/**
 * FXML Controller class
 *
 * @author Henrique Faria e Sérgio Araújo
 */
public class FormularioReceitaController implements Initializable {

    @FXML
    private TextArea fieldMedicamentos;
    @FXML
    private TextArea fieldTratamentos;
    
    private final Consulta consulta;
    
    public FormularioReceitaController(Consulta consulta) {
        this.consulta = consulta;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (consulta.getReceita() != null) {
            preencheCampos(consulta.getReceita());
        }
        
        fieldMedicamentos.addEventFilter(KeyEvent.KEY_TYPED, Util.validacaoLimiteMax(100));
        fieldTratamentos.addEventFilter(KeyEvent.KEY_TYPED, Util.validacaoLimiteMax(200));
    }    
    
    @FXML
    public void cliqueVerPerfil(ActionEvent event) {
        
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            preencheReceita();
            fecharJanela(event);
        }
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        fecharJanela(event);
    }
    
    @FXML
    public void cliqueEliminar(ActionEvent event) {
        consulta.setReceita(null);
        fecharJanela(event);
    }
    
    private void preencheCampos(Receita receita) {
        if (!receita.getDescricao().trim().isEmpty()) {
            fieldTratamentos.setText(receita.getDescricao());
        }
        
        if (!receita.getMedicamentos().trim().isEmpty()) {
            fieldMedicamentos.setText(receita.getMedicamentos());
        }
    }
    
    private boolean osDadosSaoValidos() {
        boolean saoValidos = true;
        
        if (fieldMedicamentos.getText().trim().isEmpty()) {
            saoValidos = false;
        }
        
        return saoValidos;
    }

    private void preencheReceita() {
        Receita r = new Receita();
        r.setConsulta(consulta);
        r.setMedicamentos(fieldMedicamentos.getText());
        r.setDescricao(fieldTratamentos.getText());
        consulta.setReceita(r);
    }
    
    private void fecharJanela(Event event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

}
