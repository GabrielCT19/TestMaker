/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testsmaker;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

    
/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Reloj {
    private int min;
    private int sec;
    private JLabel reloj;
    
    public Reloj(JLabel reloj){
        min = 0;
        sec = 0;
        this.reloj = reloj;
    }
    
    
    public void grow(){
        if(sec == 59){
            sec = 0;
            if(min == 59){
                JOptionPane.showMessageDialog(null, "Limite de tiempo alcanzado");
                
            }else{
                min++;
            }
        }else{
            sec++;
        }
        reloj.setText(toStringp());
    }
    
    private String toStringp(){
        String show;
        if(sec < 10){
            if(min < 10){
                show = "0"+min+" : "+"0"+sec;
            }else{
                show = min+" : "+"0"+sec;
            }
        }else{
            if(min < 10){
                show = "0"+min+" : "+sec;
            }else{
                show = min+" : "+sec;
            }
        }
        return show;
    }
    
    public void restart(){
        min = 0;
        sec = 0;
        reloj.setText("00 : 00");
    }
    
}
