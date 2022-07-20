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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gonzalomonzon.bean.Login;
import org.gonzalomonzon.bean.Usuarios;
import org.gonzalomonzon.db.Conexion;
import org.gonzalomonzon.system.Principal;

public class UsuariosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Usuarios> listaUsuarios;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private TextField txtNombresUsuario;
    @FXML private TextField txtApellidosUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtContrasenia;
    @FXML private ImageView imgNuevo;   
    @FXML private PasswordField pswContraseniaAdmin;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }
    
    public void nuevo() throws NoSuchAlgorithmException{
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/gonzalomonzon/images/guardar.png"));
                tipoOperacion = operaciones.GUARDAR;
            break;
            
            case GUARDAR:
                Login();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/gonzalomonzon/images/nuevo.png"));
                limpiarControles();
                tipoOperacion = operaciones.NINGUNO;
                ventanaLogin();
            break;
        }
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
    public void Login() throws NoSuchAlgorithmException{
        Login lo = new Login();
        boolean bandera = false;
        lo.setPasswordLogin(pswContraseniaAdmin.getText());
        String pass = getUsuarios().get(0).getContrasenia();
        if(pass.equals(lo.getPasswordLogin())){
            guardar();
            bandera = true;
        }
        
        if(bandera == false){
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
        }
    }
    
    public void cancelar(){
        ventanaLogin();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    public void guardar(){
        Usuarios registro = new Usuarios();
        registro.setNombresUsuario(txtNombresUsuario.getText());
        registro.setApellidosUsuario(txtApellidosUsuario.getText());
        registro.setIdUsuario(txtUsuario.getText());
        registro.setContrasenia(txtContrasenia.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombresUsuario());
            procedimiento.setString(2, registro.getApellidosUsuario());
            procedimiento.setString(3, registro.getIdUsuario());
            procedimiento.setString(4, registro.getContrasenia());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtNombresUsuario.setEditable(false);
        txtApellidosUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtContrasenia.setEditable(false);
        txtNombresUsuario.setDisable(true);
        txtApellidosUsuario.setDisable(true);
        txtUsuario.setDisable(true);
        txtContrasenia.setDisable(true);
        pswContraseniaAdmin.setDisable(true);
    }
    
    public void activarControles(){
        txtNombresUsuario.setEditable(true);
        txtApellidosUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtContrasenia.setEditable(true);
        txtNombresUsuario.setDisable(false);
        txtApellidosUsuario.setDisable(false);
        txtUsuario.setDisable(false);
        txtContrasenia.setDisable(false);
        pswContraseniaAdmin.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNombresUsuario.clear();
        txtApellidosUsuario.clear();
        txtUsuario.clear();
        txtContrasenia.clear();
        pswContraseniaAdmin.clear();
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}
