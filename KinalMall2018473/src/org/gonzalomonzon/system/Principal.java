package org.gonzalomonzon.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.gonzalomonzon.controller.AdministracionController;
import org.gonzalomonzon.controller.CargosController;
import org.gonzalomonzon.controller.ClientesController;
import org.gonzalomonzon.controller.CuentasPorCobrarController;
import org.gonzalomonzon.controller.CuentasPorPagarController;
import org.gonzalomonzon.controller.DepartamentosController;
import org.gonzalomonzon.controller.EmpleadosController;
import org.gonzalomonzon.controller.HorariosController;
import org.gonzalomonzon.controller.LocalesController;
import org.gonzalomonzon.controller.LoginController;
import org.gonzalomonzon.controller.MenuPrincipalController;
import org.gonzalomonzon.controller.ProgramadorController;
import org.gonzalomonzon.controller.ProveedoresController;
import org.gonzalomonzon.controller.TipoClienteController;
import org.gonzalomonzon.controller.UsuariosController;

public class Principal extends Application {
    private final String paqueteVista = "/org/gonzalomonzon/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("kinalMall2018473");
        
        //Parent root = FXMLLoader.load(getClass().getResource("/org/gonzalomonzon/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        
        ventanaLogin();
        escenarioPrincipal.show();
        escenarioPrincipal.getIcons().add(new Image("/org/gonzalomonzon/images/iconoKinallMall.png"));
        escenarioPrincipal.setResizable(false);
    }
    
    public void ventanaLogin(){
        try{
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml", 500, 350);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 660, 450);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 660, 450);
            programador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaAdministracion(){
        try{
            AdministracionController administracion = (AdministracionController)cambiarEscena("AdministracionView.fxml", 900, 450);
            administracion.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaTipoCliente(){
        try{
          TipoClienteController tipoCliente = (TipoClienteController)cambiarEscena("TipoClienteView.fxml", 900, 450);  
          tipoCliente.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaLocales(){
        try {
            LocalesController locales = (LocalesController)cambiarEscena("LocalesView.fxml", 1250, 450);
            locales.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaClientes(){
        try{
            ClientesController clientes = (ClientesController)cambiarEscena("ClientesView.fxml", 1450, 500);
            clientes.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDepartamentos(){
        try{
            DepartamentosController departamentos = (DepartamentosController)cambiarEscena("DepartamentosView.fxml", 900, 450);
            departamentos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaHorarios(){
        try{
            HorariosController horarios = (HorariosController)cambiarEscena("HorariosView.fxml", 1160, 450);
            horarios.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventaProveedores(){
        try{
            ProveedoresController proveedores = (ProveedoresController)cambiarEscena("ProveedoresView.fxml", 1330, 500);
            proveedores.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCuentasPorPagar(){
        try{
            CuentasPorPagarController cuentasPorPagar = (CuentasPorPagarController)cambiarEscena("CuentasPorPagarView.fxml", 1350, 450);
            cuentasPorPagar.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuarios(){
        try{
            UsuariosController usuarios = (UsuariosController)cambiarEscena("UsuariosView.fxml", 500, 350);
            usuarios.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCargos(){
        try{
            CargosController cargos = (CargosController)cambiarEscena("CargosView.fxml", 900, 450);
            cargos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCuentasPorCobrar(){
        try{
            CuentasPorCobrarController cuentasPorCobrar = (CuentasPorCobrarController)cambiarEscena("CuentasPorCobrarView.fxml", 1310, 500);
            cuentasPorCobrar.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpleados(){
        try{
            EmpleadosController empleados = (EmpleadosController)cambiarEscena("EmpleadosView.fxml", 1500, 500);
            empleados.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws IOException{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(paqueteVista + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(paqueteVista + fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
    
}
