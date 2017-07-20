
package techvet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Consulta;
import model.Paciente;
import model.TipoConsulta;
import bll.Util;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Objects;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import techvet.DocFXML;
import techvet.Utils;

/**
 * @author Henrique Faria e Sergio Araujo
 */
public class FormularioConsultaController implements Initializable {

    @FXML
    private TextField fieldNomePaciente;
    @FXML
    private DatePicker fieldData;
    @FXML 
    private TextArea descricao;
    @FXML 
    private ChoiceBox<Choice> boxTipoConsulta;
    @FXML
    private ChoiceBox boxLocal;
    @FXML
    private TextField localConsulta;
    @FXML
    private TextField fieldHoras;
    @FXML
    private TextField fieldMin;
    @FXML
    private Button botaoPesquisar;
    
    private final Pane content;
    
    private Paciente paciente;
    private Consulta consulta;
    
    public FormularioConsultaController(Pane content) {
        this.content = content;
    }
    
    public FormularioConsultaController (Consulta consulta, Pane content) {
        this.consulta = consulta;
        this.content = content;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popularChoiceBox();
        
        fieldHoras.addEventFilter(KeyEvent.KEY_TYPED, Utils.validacaoNumerica(2));
        fieldMin.addEventFilter(KeyEvent.KEY_TYPED, Utils.validacaoNumerica(2));
        
        //O textfield do local da consulta só é activado caso seja selecionada 
        //a opção "Exterior" 
        boxLocal.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.equals(0)) {
                localConsulta.setDisable(true);
                localConsulta.clear();
            } else {
                localConsulta.setDisable(false);
            }
        });
               
        //Listeners para os textfields
        //https://stackoverflow.com/questions/30249493/using-threads-to-make-database-requests
        fieldNomePaciente.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue == false && !fieldNomePaciente.getText().trim().isEmpty()) {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String nome = fieldNomePaciente.getText();
                        atualizaPacienteBD(nome);
                        return null;
                    }
                };
                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
            }
        });
        if (consulta != null) preencherCampos(); 
    }    
    
    private void popularChoiceBox() {
        ObservableList<Choice> escolhasTipoConsulta = FXCollections.observableArrayList();
        List<TipoConsulta> tiposConsulta = TipoConsulta.retrieveAll();
        tiposConsulta.forEach( (TipoConsulta tipo) -> {
            escolhasTipoConsulta.add(new Choice(tipo));
        });
        boxTipoConsulta.getItems().addAll(escolhasTipoConsulta);
        boxTipoConsulta.getSelectionModel().select(0);
        
        ObservableList<String> escolhasLocal = FXCollections.observableArrayList();
        escolhasLocal.add("Local");
        escolhasLocal.add("Exterior");
        boxLocal.getItems().addAll(escolhasLocal);
        boxLocal.getSelectionModel().select(0);
    }
    
    private void preencherCampos() {
        boolean isEditavel = consulta.getEstado() == (short) 1;
        fieldNomePaciente.setText(consulta.getPaciente().getNome());
        fieldNomePaciente.setDisable(isEditavel);
        
        Instant i = Instant.ofEpochMilli(consulta.getDatahora().getTime());
        LocalDateTime ldt = LocalDateTime.ofInstant(i, ZoneId.systemDefault());
        
        fieldHoras.setText(String.valueOf(ldt.getHour()));
        fieldHoras.setDisable(isEditavel);
        fieldMin.setText(String.valueOf(ldt.getMinute()));
        fieldMin.setDisable(isEditavel);
        
        fieldData.setValue(ldt.toLocalDate());
        fieldData.setDisable(isEditavel);
        
        descricao.setText(consulta.getDesctratamento());
        descricao.setDisable(isEditavel);
        if (!consulta.getLocal().equals(boxLocal.getSelectionModel().getSelectedItem())) {
            boxLocal.getSelectionModel().select(1);
            boxLocal.setDisable(isEditavel);
            localConsulta.setText(consulta.getLocal());
            localConsulta.setDisable(isEditavel);
        }
        
        boxTipoConsulta.getSelectionModel().select(new Choice(consulta.getTipoConsulta()));
        boxTipoConsulta.setDisable(isEditavel);
        botaoPesquisar.setDisable(isEditavel);
        atualizaPacienteBD(consulta.getPaciente().getNome());
    }
    
    private void atualizaPacienteBD(String nome) {
        List<Paciente> pacientes = Paciente.retrievePacientesbyNome(nome);
        if (pacientes.size() == 1) {
            paciente = pacientes.get(0);
        } 
    }
    
    @FXML
    public void cliqueConfirmar(ActionEvent event) {
        if (osCamposPreenchidos()) {
            inserirConsultaBD();
            mudarContent();
        } 
    }
    
    private void mudarContent() {
        Initializable controller = new ListaConsultasController(false, content);
        try {
            Utils.mudaContentPara(DocFXML.LISTACONSULTAS, controller, content);
        } catch (IOException e) {}
    }
    
    @FXML
    public void cliqueCancelar(ActionEvent event) {
        mudarContent();
    }
    
    @FXML
    private void cliqueProcurarPaciente(ActionEvent event) {
        ListaPacientesController controller;
        try {
            controller = abrirListaPacientes(event);
        } catch (IOException ex) {
            return ;
        }
        if (controller.foiSelecionadaOpcao()) {
            paciente = controller.getPacienteSelecionado();
            fieldNomePaciente.setText(paciente.getNome());
        }
    }
    
    private ListaPacientesController abrirListaPacientes(Event event) throws IOException {
        ListaPacientesController controller = new ListaPacientesController(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DocFXML.LISTAPACIENTES.getPath()));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        Stage owner = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Stage stage = Utils.preparaNovaJanela(owner);
        stage.setScene(scene);
        stage.showAndWait();
        return controller;
    }
    
    private boolean osCamposPreenchidos() {
        boolean eValido = true;
        
        if(fieldNomePaciente.getText().trim().isEmpty()) {
           eValido = false;
        }
        
        if(fieldData.getValue() == null) {
            eValido = false;
        }
        
        if (boxTipoConsulta.getValue() == null) {
            eValido = false;
        }
        
        if (!boxLocal.getValue().equals("Local") && localConsulta.getText().trim().isEmpty()) {
            eValido = false;
        }
        
        int horas;
        int mins;      
        
        try {
            horas = Integer.parseInt(fieldHoras.getText());
            mins = Integer.parseInt(fieldMin.getText());
            
            if (horas < 0 && horas >= 24) {
                eValido = false;
            }
            
            if (mins < 0 && mins > 59) {
                eValido = false;
            }
        } catch (NumberFormatException e) {
            eValido = false;
        }
        atualizaPacienteBD(fieldNomePaciente.getText());
        
        return eValido;
    }
    
    private void inserirConsultaBD() { 
        boolean isNovaConsulta;
        isNovaConsulta = consulta == null;
        if (isNovaConsulta) consulta = new Consulta();
        
        consulta.setEstado((short) 0);
        consulta.setPago((short) 0);
        consulta.setDatahora(prepararData());
        consulta.setPaciente(paciente);
        consulta.setTipoConsulta(boxTipoConsulta.getSelectionModel().getSelectedItem().getTipoConsulta());
        consulta.setLocal(boxLocal.getSelectionModel().getSelectedItem().toString());
        consulta.setDesctratamento(descricao.getText());
        
        if (isNovaConsulta) consulta.createT();
        else consulta.updateT();
    }
    
    private Date prepararData() {       
        int horas = Integer.parseInt(fieldHoras.getText());
        int mins = Integer.parseInt(fieldMin.getText());
        
        LocalTime time = LocalTime.of(horas, mins);
        LocalDate date = fieldData.getValue();
        
        LocalDateTime ldt = LocalDateTime.of(date, time);
        Instant i = ldt.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(i);
        
        return data;
    }
    
    private class Choice {
        private final TipoConsulta tipo;
        
        public Choice(TipoConsulta tipo) {
           this.tipo = tipo;
        }
       
        public TipoConsulta getTipoConsulta() {
            return this.tipo;
        }
        
        @Override 
        public String toString() {
            return tipo.getNome();
        }   
        
        
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!Choice.class.isAssignableFrom(obj.getClass())) {
                return false;
            }
            final Choice other = (Choice) obj;
            if ((this.tipo == null) ? (other.tipo != null) : !this.tipo.equals(other.tipo)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 41 * hash + Objects.hashCode(this.tipo);
            return hash;
        }

    }
    
}
