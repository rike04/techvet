/*
 * 
 */

package techvet.controllers;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Cliente;
import model.Paciente;

/**
 * FXML Controller class
 *
 * @author rike4
 */ 
public class FormularioPacienteController implements Initializable {
    
    @FXML 
    private TextField fieldNome;
    @FXML
    private TextField fieldEspecie;
    @FXML
    private TextField fieldIdade;
    @FXML
    private ChoiceBox boxSexo;
    @FXML 
    private TextField fieldCliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popularChoiceBox();
    }    
    
    @FXML
    public void cliqueProcurarCliente(ActionEvent event) {
        
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osDadosSaoValidos()) {
            Cliente c;
            try {
                c = buscaCliente();
                inserirNaBD(c);
            } catch (Exception ex) {
                Logger.getLogger(FormularioPacienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML 
    public void cliqueCancelar(ActionEvent event) {
        
    }
    
    private Boolean osDadosSaoValidos() {
        return true;
    }
    
    private void popularChoiceBox() {
        ObservableList<String> opcoes = FXCollections.observableArrayList();
        opcoes.add("Masculino");
        opcoes.add("Feminino");
        
        boxSexo.getItems().setAll(opcoes);
        boxSexo.getSelectionModel().select(0);
    }
    
    private void inserirNaBD(Cliente c) {
        Paciente p = new Paciente();
        p.setNome(fieldNome.getText());
        p.setEspecie(fieldEspecie.getText());
        p.setDatanasc(new Date());
        p.setIdCliente(c);
        p.setRaca("Raca");
        p.setCor("Cor");
        p.setSexo(boxSexo.getSelectionModel().getSelectedItem().toString());
        p.setEstado((short) 1);
        p.createT();
    } 
    
    private Cliente buscaCliente() throws Exception {
        System.out.println("Procurar por: " + fieldCliente.getText());
        List<Cliente> clientes = Cliente.readByNome( fieldCliente.getText() );
        return clientes.get(0);
    }
}
