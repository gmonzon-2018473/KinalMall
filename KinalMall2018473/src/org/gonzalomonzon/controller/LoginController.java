package org.gonzalomonzon.controller;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gonzalomonzon.bean.Login;
import org.gonzalomonzon.bean.Usuarios;
import org.gonzalomonzon.db.Conexion;
import org.gonzalomonzon.system.Principal;

public class LoginController implements Initializable{
    private enum modosContrasenia{VISIBLE, INVISIBLE};
    private modosContrasenia modoContrasenia = modosContrasenia.INVISIBLE;
    private Principal escenarioPrincipal;
    private ObservableList<Usuarios> listaUsuarios;
    @FXML private Button btnIniciarSesion;
    @FXML private Button btnContrasenia;
    @FXML private Label lblRegistrarse;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtContrasenia;
    @FXML private PasswordField pswContrasenia;
    @FXML private ImageView imgContrasenia;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }
    
    public ObservableList<Usuarios> getUsuarios(){
        ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuarios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Usuarios(resultado.getInt("codigoUsuario"),
                                      resultado.getString("nombresUsuario"),
                                      resultado.getString("apellidosUsuario"),
                                      resultado.getString("idUsuario"),
                                      resultado.getString("contrasenia")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaUsuarios = FXCollections.observableArrayList(lista);
    }
    
    @FXML
    private void Login() throws NoSuchAlgorithmException{
        Login lo = new Login();
        int x = 0;
        boolean bandera = false;
        lo.setUsuarioMaster(txtUsuario.getText());
        if(txtContrasenia.visibleProperty().get() == true){
            lo.setPasswordLogin(txtContrasenia.getText());
        }else{
            lo.setPasswordLogin(pswContrasenia.getText());
        }
        while(x < getUsuarios().size()){
            String user = getUsuarios().get(x).getIdUsuario();
            String pass = getUsuarios().get(x).getContrasenia();
            if(user.equals(lo.getUsuarioMaster()) && pass.equals(lo.getPasswordLogin())){
                JOptionPane.showMessageDialog(null, "      Su sesión a iniciado correctamente\n" + "Bienvenido" + " " + getUsuarios().get(x).getNombresUsuario() + " " + getUsuarios().get(x).getApellidosUsuario());
                escenarioPrincipal.menuPrincipal();
                x = getUsuarios().size();
                bandera = true;
            }
            x++;
        }
        if(bandera == false){
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecta." );
            txtUsuario.clear();
            txtContrasenia.clear();
            pswContrasenia.clear();
        }
    }
    
    public void visualizacionContrasenia(){
        switch(modoContrasenia){
            case INVISIBLE:
                txtContrasenia.setText(pswContrasenia.getText());
                txtContrasenia.setVisible(true);
                txtContrasenia.setEditable(true);
                pswContrasenia.setVisible(false);
                pswContrasenia.setEditable(false);
                modoContrasenia = modosContrasenia.VISIBLE;
                imgContrasenia.setImage(new Image("/org/gonzalomonzon/images/invisible.png"));
            break;
            
            case VISIBLE:
                pswContrasenia.setText(txtContrasenia.getText());
                txtContrasenia.setVisible(false);
                txtContrasenia.setEditable(false);
                pswContrasenia.setVisible(true);
                pswContrasenia.setEditable(true);
                modoContrasenia = modosContrasenia.INVISIBLE;
                imgContrasenia.setImage(new Image("/org/gonzalomonzon/images/visible.png"));
            break;
        }
    }
    
    public void ventanaUsuarios(){
        escenarioPrincipal.ventanaUsuarios();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}