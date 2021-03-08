package classes;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JTextField;

/**
 *
 * @author Gabriel Cabredo
 */
public class ComprovadorTextField {

    private final ArrayList<JTextField> caja;

    public ComprovadorTextField(JTextField text1) {
        caja = new ArrayList();
        caja.add(text1);
    }

    public void add(JTextField text1) {
        caja.add(text1);
    }

    public boolean comprobarTexto() {
        boolean res = true;
        for (JTextField x : caja) {
            if (x.getText().isBlank()) {
                res = false;
                x.setBackground(Color.RED);
            } else {
                x.setBackground(Color.GREEN);
            }
        }
        
        return res;
    }

    public void todoRojo() {

        caja.forEach(x -> {
            x.setBackground(Color.RED);
        });
        todoInicial();

    }
    public void vaciarTodo() {

        caja.forEach(x -> {
            x.setText("");
        });

    }

    public void todoInicial() {

        caja.forEach(x -> {
            x.setBackground(Color.WHITE);
        });
        

    }

}
