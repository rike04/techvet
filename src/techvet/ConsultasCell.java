/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package techvet;

import javafx.scene.control.ListCell;
import model.Utilizador;

/**
 *
 * @author rike4
 */
public class ConsultasCell extends ListCell<Utilizador>{
    
    @Override 
    public void updateItem (Utilizador item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            String nome = item.getNome();
            this.setText(nome);
            
        }
    }
}
