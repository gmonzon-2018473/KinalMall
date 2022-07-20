package org.gonzalomonzon.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gonzalomonzon.bean.Administracion;
import org.gonzalomonzon.bean.Clientes;
import org.gonzalomonzon.bean.Locales;
import org.gonzalomonzon.bean.TipoCliente;
import org.gonzalomonzon.db.Conexion;
import org.gonzalomonzon.report.GenerarReporte;
import org.gonzalomonzon.system.Principal;

public class ClientesController implements Initializable{
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigoCliente;
    @FXML private TextField txtNombresCliente;
    @FXML private TextField txtApellidosCliente;
    @FXML private TextField txtTelefonoCliente;
    @FXML private TextField txtDireccionCliente;
    @FXML private TextField txtEmailCliente;
    @FXML private ComboBox cmbCodigoLocal;
    @FXML private ComboBox cmbCodigoAdministracion;
    @FXML private ComboBox cmbCodigoTipoCliente;
    @FXML private TableView tblClientes; 
    @FXML private TableColumn colCodigoCliente;
    @FXML private TableColumn colNombresCliente;
    @FXML private TableColumn colApellidosCliente;
    @FXML private TableColumn colTelefonoCliente;
    @FXML private TableColumn colDireccionCliente;
    @FXML private TableColumn colEmailCliente;
    @FXML private TableColumn colCodigoLocal;
    @FXML private TableColumn colCodigoAdministracion;
    @FXML private TableColumn colCodigoTipoCliente;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<Locales> listaLocales;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<TipoCliente> listaTipoCliente; 
    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblClientes.setItems(getClientes());
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoCliente"));
        colNombresCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombresCliente"));
        colApellidosCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidosCliente"));
        colTelefonoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoCliente"));
        colDireccionCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionCliente"));
        colEmailCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
        colCodigoLocal.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("codigoLocal"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        colCodigoTipoCliente.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("codigoTipoCliente"));
        cmbCodigoLocal.setItems(getLocales());
        cmbCodigoAdministracion.setItems(getAdministracion());
        cmbCodigoTipoCliente.setItems(getTipoCliente());
    }    
    
    public ObservableList<Clientes> getClientes(){
        ArrayList<Clientes> lista = new ArrayList<Clientes>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Clientes(resultado.getInt("codigoCliente"),
                                        resultado.getString("nombresCliente"),
                                        resultado.getString("apellidosCliente"),
                                        resultado.getString("telefonoCliente"),
                                        resultado.getString("direccionCliente"),
                                        resultado.getString("email"),
                                        resultado.getInt("codigoLocal"),
                                        resultado.getInt("codigoAdministracion"),
                                        resultado.getInt("codigoTipoCliente")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Locales> getLocales(){
        ArrayList<Locales> lista = new ArrayList<Locales>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarLocales()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Locales(resultado.getInt("codigoLocal"),
                                      resultado.getDouble("saldoFavor"),
                                      resultado.getDouble("saldoContra"),
                                      resultado.getInt("mesesPendientes"),
                                      resultado.getBoolean("disponibilidad"),
                                      resultado.getDouble("valorLocal"),
                                      resultado.getDouble("valorAdministracion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaLocales = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Administracion> getAdministracion(){
        ArrayList<Administracion> lista = new ArrayList<Administracion>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Administracion(resultado.getInt("codigoAdministracion"),
                                             resultado.getString("direccion"),
                                             resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaAdministracion = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<TipoCliente> getTipoCliente(){
    ArrayList<TipoCliente> lista = new ArrayList<TipoCliente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTIpoCliente()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoCliente(resultado.getInt("codigoTipoCliente"),
                                          resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaTipoCliente =  FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblClientes.getSelectionModel().getSelectedItem() != null){
            txtCodigoCliente.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            txtNombresCliente.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombresCliente());
            txtApellidosCliente.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidosCliente());
            txtTelefonoCliente.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente());
            txtDireccionCliente.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente());
            txtEmailCliente.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getEmail());
            cmbCodigoLocal.getSelectionModel().select(buscarLocal(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoLocal()));
            cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbCodigoTipoCliente.getSelectionModel().select(buscarTipoCliente(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));
        }
    }
    
    public Locales buscarLocal(int codigoLocal){
        Locales resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarLocal(?)}");
            procedimiento.setInt(1, codigoLocal);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Locales(registro.getInt("codigoLocal"),
                                        registro.getDouble("saldoFavor"),
                                        registro.getDouble("saldoContra"),
                                        registro.getInt("mesesPendientes"),
                                        registro.getBoolean("disponibilidad"),
                                        registro.getDouble("valorLocal"),
                                        registro.getDouble("valorAdministracion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado; 
    }
    
    public Administracion buscarAdministracion(int codigoAdministracion){
        Administracion resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarAdministracion(?)}");
            procedimiento.setInt(1, codigoAdministracion);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Administracion(registro.getInt("codigoAdministracion"),
                                               registro.getString("direccion"),
                                               registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public TipoCliente buscarTipoCliente(int codigoTipoCliente){
        TipoCliente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoCliente(?)}");
            procedimiento.setInt(1, codigoTipoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoCliente(registro.getInt("codigoTipoCliente"),
                                            registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/gonzalomonzon/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/gonzalomonzon/images/cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(txtNombresCliente.getText().length() == 0 || txtApellidosCliente.getText().length() == 0 || txtTelefonoCliente.getText().length() == 0 || txtDireccionCliente.getText().length() == 0 || txtEmailCliente.getText().length() == 0){
                    JOptionPane.showMessageDialog(null, "Debe completar los datos");
                }else if(cmbCodigoLocal.getSelectionModel().getSelectedItem() == null || cmbCodigoAdministracion.getSelectionModel().getSelectedItem() == null || cmbCodigoTipoCliente.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null, "Debe completar los datos");
                }else if(txtNombresCliente.getText().length() > 45 || txtApellidosCliente.getText().length() > 45 || txtDireccionCliente.getText().length() > 45 || txtEmailCliente.getText().length() > 45){
                    JOptionPane.showMessageDialog(null, "Usted a superado el límite de los caracteres en alguno de los campos de arriba. Los campos solo pueden contener 45 caracteres.");
                }else if(txtTelefonoCliente.getText().length() > 8){
                    JOptionPane.showMessageDialog(null, "El teléfono que ingreso tiene más de 8 números. Ingrese un teléfono válido.");
                }else if(txtTelefonoCliente.getText().length() < 8){
                    JOptionPane.showMessageDialog(null, "El teléfono que ingreso tiene menos de 8 números. Ingrese un teléfono válido.");
                }else{
                    guardar();
                }
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/gonzalomonzon/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/gonzalomonzon/images/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Clientes registro = new Clientes();
        registro.setNombresCliente(txtNombresCliente.getText());
        registro.setApellidosCliente(txtApellidosCliente.getText());
        registro.setTelefonoCliente(txtTelefonoCliente.getText());
        registro.setDireccionCliente(txtDireccionCliente.getText());
        registro.setEmail(txtEmailCliente.getText());
        registro.setCodigoLocal(((Locales)cmbCodigoLocal.getSelectionModel().getSelectedItem()).getCodigoLocal());
        registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        registro.setCodigoTipoCliente(((TipoCliente)cmbCodigoTipoCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCliente(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getNombresCliente());
            procedimiento.setString(2, registro.getApellidosCliente());
            procedimiento.setString(3, registro.getTelefonoCliente());
            procedimiento.setString(4, registro.getDireccionCliente());
            procedimiento.setString(5, registro.getEmail());
            procedimiento.setInt(6, registro.getCodigoLocal());
            procedimiento.setInt(7, registro.getCodigoAdministracion());
            procedimiento.setInt(8, registro.getCodigoTipoCliente());
            procedimiento.execute();
            listaClientes.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/gonzalomonzon/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/gonzalomonzon/images/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                break;
                
            default:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Cliente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BorrarCliente(?)}");
                            procedimiento.setInt(1, ((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            listaClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro.");
                    limpiarControles();
                }
        }
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/gonzalomonzon/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/gonzalomonzon/images/cancelar.png"));
                    activarControles();
                    seleccionarElemento();
                    cmbCodigoLocal.setDisable(true);
                    cmbCodigoAdministracion.setDisable(true);
                    cmbCodigoTipoCliente.setDisable(true);
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro.");
                }
            break;
            
            case ACTUALIZAR:
                if(txtNombresCliente.getText().length() == 0 || txtApellidosCliente.getText().length() == 0 || txtTelefonoCliente.getText().length() == 0 || txtDireccionCliente.getText().length() == 0 || txtEmailCliente.getText().length() == 0){
                    JOptionPane.showMessageDialog(null, "No puede ingresar datos nulos");
                }else if(cmbCodigoLocal.getSelectionModel().getSelectedItem() == null || cmbCodigoAdministracion.getSelectionModel().getSelectedItem() == null || cmbCodigoTipoCliente.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null, "No puede ingresar datos nulos");
                }else if(txtNombresCliente.getText().length() > 45 || txtApellidosCliente.getText().length() > 45 || txtDireccionCliente.getText().length() > 45 || txtEmailCliente.getText().length() > 45){
                    JOptionPane.showMessageDialog(null, "Usted a superado el límite de los caracteres en alguno de los campos de arriba. Los campos solo pueden contener 45 caracteres.");
                }else if(txtTelefonoCliente.getText().length() > 8){
                    JOptionPane.showMessageDialog(null, "El teléfono que ingreso tiene más de 8 números. Ingrese un teléfono válido.");
                }else if(txtTelefonoCliente.getText().length() < 8){
                    JOptionPane.showMessageDialog(null, "El teléfono que ingreso tiene menos de 8 números. Ingrese un teléfono válido.");
                }else{
                    actualizar();
                }
                    desactivarControles();
                    limpiarControles();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    imgEditar.setImage(new Image("/org/gonzalomonzon/images/editar.png"));
                    imgReporte.setImage(new Image("/org/gonzalomonzon/images/reporte.png"));
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
            break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCliente(?, ?, ?, ?, ?, ?)}");;
            Clientes registro = (Clientes)tblClientes.getSelectionModel().getSelectedItem();
            registro.setNombresCliente(txtNombresCliente.getText());
            registro.setApellidosCliente(txtApellidosCliente.getText());
            registro.setTelefonoCliente(txtTelefonoCliente.getText());
            registro.setDireccionCliente(txtDireccionCliente.getText());
            registro.setEmail(txtEmailCliente.getText());
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNombresCliente());
            procedimiento.setString(3, registro.getApellidosCliente());
            procedimiento.setString(4, registro.getTelefonoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(6, registro.getEmail());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/gonzalomonzon/images/editar.png"));
                imgReporte.setImage(new Image("/org/gonzalomonzon/images/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
            break;
            
            case NINGUNO:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    imprimirReporteIndividual();
                }else{
                    imprimirReporteGeneral();
                }
            break;
        }
    }
    
    public void imprimirReporteGeneral(){
        Map parametros = new HashMap();
        parametros.put("codigoCliente", null);
        GenerarReporte.mostrarReporte("ReporteClientes.jasper", "Reporte de clientes", parametros);
    }
    
    public void imprimirReporteIndividual(){
        Map parametros = new HashMap();
        int codCliente = ((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente();
        parametros.put("codCliente", codCliente);
        GenerarReporte.mostrarReporte("ReporteUnCliente.jasper", "Reporte cliente", parametros);
    }
    
    public void activarControles(){
        txtCodigoCliente.setEditable(false);
        txtNombresCliente.setEditable(true);
        txtApellidosCliente.setEditable(true);
        txtTelefonoCliente.setEditable(true);
        txtDireccionCliente.setEditable(true);
        txtEmailCliente.setEditable(true);
        cmbCodigoLocal.setDisable(false);
        cmbCodigoAdministracion.setDisable(false);
        cmbCodigoTipoCliente.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoCliente.setEditable(false);
        txtNombresCliente.setEditable(false);
        txtApellidosCliente.setEditable(false);
        txtTelefonoCliente.setEditable(false);
        txtDireccionCliente.setEditable(false);
        txtEmailCliente.setEditable(false);
        cmbCodigoLocal.setDisable(true);
        cmbCodigoAdministracion.setDisable(true);
        cmbCodigoTipoCliente.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoCliente.clear();
        txtNombresCliente.clear();
        txtApellidosCliente.clear();
        txtTelefonoCliente.clear();
        txtDireccionCliente.clear();
        txtEmailCliente.clear();
        cmbCodigoLocal.setValue(null);
        cmbCodigoAdministracion.setValue(null);
        cmbCodigoTipoCliente.setValue(null);
    }   
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
