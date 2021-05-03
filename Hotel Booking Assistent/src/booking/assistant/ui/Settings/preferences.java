/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.ui.Settings;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;


/**
 *
 * @author nelly
 */
public class preferences {
    
    public static final String CONFIG_FILE="config.txt";
    
    float ammoutPerDay;
    String username;
    String password;

    public preferences() {
        ammoutPerDay= 500;
        username= "admin";
        setPassword("admin");
    }

    public float getAmmoutPerDay() {
        return ammoutPerDay;
    }

    public void setAmmoutPerDay(float ammoutPerDay) {
        this.ammoutPerDay = ammoutPerDay;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password.length()<25){
        this.password = DigestUtils.sha1Hex(password);
        } else{
            this.password=password;
        }
    }
    
    public static void initConfig(){
        Writer writer = null;
        try {
            preferences preference = new preferences();
            Gson gson= new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
        } catch (IOException ex) {
            Logger.getLogger(preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    public static preferences getpreferences(){
        Gson gson= new Gson();
        preferences pref= new preferences();
        try {
             pref = gson.fromJson(new FileReader(CONFIG_FILE), preferences.class);
        } catch (FileNotFoundException ex) {
            initConfig();
            Logger.getLogger(preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pref;
    }
    
    public static void writePreferencesToFile(preferences preference ){
         Writer writer = null;
        try {
            Gson gson= new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
            JOptionPane.showMessageDialog(null, "Successfully  made  changes on the  Additional  information!", "Succesful Operation", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot Save The Configuration File!!!!" , "Error Occured", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
