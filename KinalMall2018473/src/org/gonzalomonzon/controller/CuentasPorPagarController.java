package org.gonzalomonzon.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.gonzalomonzon.bean.Administracion;
import org.gonzalomonzon.bean.CuentasPorPagar;
import org.gonzalomonzon.bean.Proveedores;
import org.gonzalomonzon.system.Principal;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.gonzalomonzon.db.Conexion;

public class CuentasPorPagarController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, CANCELAR, ELIMINAR, NINGUNO, ACTUALIZAR};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<CuentasPorPagar> listaCuentasPorPagar;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Proveedores> listaProveedores;
    private DatePicker fechaLimitePago;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;       
    @FXML private Button btnEditar;       
    @FXML private Button btnReporte;       
    @FXML private ImageView imgNuevo;       
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;       
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigoCuentaPorPagar;
    @FXML private TextField txtNumeroFactura;
    @FXML private TextField txtEstadoPago;
    @FXML private TextField txtValorNetoPago;
    @FXML private ComboBox cmbCodigoAdministracion;
    @FXML private ComboBox cmbCodigoProveedor;
    @FXML private GridPane grpFechaLimitePago;
    @FXML private TableView tblCuentasPorPagar;
    @FXML private TableColumn colCodigoCuentaPorPagar;
    @FXML private TableColumn colNumeroFactura;       
    @FXML private TableColumn colFechaLimitePago;       
    @FXML private TableColumn colEstadoPago;       
    @FXML private TableColumn colValorNetoPago;       
    @FXML private TableColumn colCodigoAdministracion;       
    @FXML private TableColumn colCodigoProveedor;
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        fechaLimitePago = new DatePicker(Locale.ENGLISH);
        fechaLimitePago.setPromptText("Fecha");
        fechaLimitePago.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaLimitePago.getCalendarView().todayButtonTextProperty().set("Hoy");
        fechaLimitePago.getCalendarView().setShowWeeks(false);
        grpFechaLimitePago.add(fechaLimitePago, 0, 0);
        fechaLimitePago.getStylesheets().add("org/gonzalomonzon/resource/DatePicker.css");
        fechaLimitePago.setDisable(true);
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblCuentasPorPagar.setItems(getCuentasPorPagar());
        colCodigoCuentaPorPagar.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("codigoCuentaPorPagar"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("numeroFactura"));
        colFechaLimitePago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Date>("fechaLimitePago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("estadoPago"));
        colValorNetoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Double>("valorNetoPago"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("codigoProveedor"));  
        cmbCodigoAdministracion.setItems(getAdministracion());
        cmbCodigoProveedor.setItems(getProveedores());
    }
    
    public ObservableList<CuentasPorPagar> getCuentasPorPagar(){
        ArrayList<CuentasPorPagar> lista = new ArrayList<CuentasPorPagar>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCuentasPorPagar()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new CuentasPorPagar(resultado.getInt("codigoCuentaPorPagar"),
                                              resultado.getString("numeroFactura"),
                                              resultado.getDate("fechaLimitePago"),
                                              resultado.getString("estadoPago"),
                                              resultado.getDouble("valorNetoPago"),
                                              resultado.getInt("codigoAdministracion"),
                                              resultado.getInt("codigoProveedor")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCuentasPorPagar = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Administracion> getAdministracion(){
        ArrayList<Administracion> lista = new ArrayList<Administracion>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{ call sp_ListarAdministracion()}");
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
    
    public ObservableList<Proveedores> getProveedores(){
        ArrayList<Proveedores> lista = new ArrayList<Proveedores>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Proveedores(resultado.getInt("codigoProveedor"),
                                          resultado.getString("NITProveedor"),
                                          resultado.getString("servicioPrestado"),
                                          resultado.getString("telefonoProveedor"),
                                          resultado.getString("direccionProveedor"),
                                          resultado.getDouble("saldoFavor"),
                                          resultado.getDouble("saldoContra"),
                                          resultado.getInt("codigoAdministracion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null){
        txtCodigoCuentaPorPagar.setText(String.valueOf(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoCuentaPorPagar()));
        txtNumeroFactura.setText(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getNumeroFactura());
        fechaLimitePago.selectedDateProperty().set(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getFechaLimitePago());
        txtEstadoPago.setText(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getEstadoPago());
        txtValorNetoPago.setText(String.valueOf(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
        cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
        cmbCodigoProveedor.getSelectionModel().select(buscarProveedor(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        }
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
    
    public Proveedores buscarProveedor(int codigoProveedor){
        Proveedores resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProveedor(?)}");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Proveedores(registro.getInt("codigoProveedor"),
                                          registro.getString("NITProveedor"),
                                          registro.getString("servicioPrestado"),
                                          registro.getString("telefonoProveedor"),
                                          registro.getString("direccionProveedor"),
                                          registro.getDouble("saldoFavor"),
                                          registro.getDouble("saldoContra"),
                                          registro.getInt("codigoAdministracion"));
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
                if(txtNumeroFactura.getText().length() == 0 || String.valueOf(fechaLimitePago).length() == 0 || txtEstadoPago.getText().length() == 0 || txtValorNetoPago.getText().length() == 0 || cmbCodigoAdministracion.getSelectionModel().getSelectedItem() == null || cmbCodigoProveedor.getSelectionModel().getSelectedItem() == null){
                   JOptionPane.showMessageDialog(null, "Debe completar los datos"); 
                }else if(txtEstadoPago.getText().length() > 45){
                   JOptionPane.showMessageDialog(null, "Usted a superado el límite de los caracteres en estado de pago. Estado de pago solo pueden contener 45 caracteres.");

                }else if(txtNumeroFactura.getText().length() > 45){
                   JOptionPane.showMessageDialog(null, "Usted a superado el límite de los caracteres el número de factura. El número de factura solo pueden contener 45 números."); 
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
        CuentasPorPagar registro = new CuentasPorPagar();
        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setFechaLimitePago(fechaLimitePago.getSelectedDate());
        registro.setEstadoPago(txtEstadoPago.getText());
        registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
        registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        registro.setCodigoProveedor(((Proveedores)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCuentaPorPagar(?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getNumeroFactura());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaLimitePago().getTime()));
            procedimiento.setString(3, registro.getEstadoPago());
            procedimiento.setDouble(4, registro.getValorNetoPago());
            procedimiento.setInt(5, registro.getCodigoAdministracion());
            procedimiento.setInt(6, registro.getCodigoProveedor());
            procedimiento.execute();
            listaCuentasPorPagar.add(registro);
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
                if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar cuenta por pagar.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BorrarCuentaPorPagar(?)}");
                            procedimiento.setInt(1, ((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoCuentaPorPagar());
                            procedimiento.execute();
                            listaCuentasPorPagar.remove(tblCuentasPorPagar.getSelectionModel().getSelectedItem());
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
                if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/gonzalomonzon/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/gonzalomonzon/images/cancelar.png"));
                    activarControles();
                    cmbCodigoAdministracion.setDisable(true);
                    cmbCodigoProveedor.setDisable(true);
                    tipoOperacion = operaciones.ACTUALIZAR;
                    seleccionarElemento();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro.");
                }
            break;
            
            case ACTUALIZAR:
                if(txtNumeroFactura.getText().length() == 0 || String.valueOf(fechaLimitePago).length() == 0 || txtEstadoPago.getText().length() == 0 || txtValorNetoPago.getText().length() == 0 || cmbCodigoAdministracion.getSelectionModel().getSelectedItem() == null || cmbCodigoProveedor.getSelectionModel().getSelectedItem() == null){
                   JOptionPane.showMessageDialog(null, "Debe completar los datos"); 
                }else if(txtEstadoPago.getText().length() > 45){
                   JOptionPane.showMessageDialog(null, "Usted a superado el límite de los caracteres en estado de pago. Estado de pago solo pueden contener 45 caracteres.");

                }else if(txtNumeroFactura.getText().length() > 45){
                   JOptionPane.showMessageDialog(null, "Usted a superado el límite de los caracteres el número de factura. El número de factura solo pueden contener 45 números."); 
                }else{
                    actualizar();
                }
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/gonzalomonzon/images/editar.png"));
                imgReporte.setImage(new Image("/org/gonzalomonzon/images/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                desactivarControles();
                limpiarControles();
            break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCuentaPorPagar(?, ?, ?, ?, ?)}");
            CuentasPorPagar registro = (CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem();
            registro.setNumeroFactura(txtNumeroFactura.getText());
            registro.setFechaLimitePago(fechaLimitePago.getSelectedDate());
            registro.setEstadoPago(txtEstadoPago.getText());
            registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
            procedimiento.setInt(1, registro.getCodigoCuentaPorPagar());
            procedimiento.setString(2, registro.getNumeroFactura());
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaLimitePago().getTime()));
            procedimiento.setString(4, registro.getEstadoPago());
            procedimiento.setDouble(5, registro.getValorNetoPago());
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
        }
    }
    
    public void desactivarControles(){
        txtCodigoCuentaPorPagar.setEditable(false);
        txtNumeroFactura.setEditable(false);
        fechaLimitePago.setDisable(true);
        txtEstadoPago.setEditable(false);
        txtValorNetoPago.setEditable(false);
        cmbCodigoAdministracion.setDisable(true);
        cmbCodigoProveedor.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoCuentaPorPagar.setEditable(false);
        txtNumeroFactura.setEditable(true);
        fechaLimitePago.setDisable(false);
        txtEstadoPago.setEditable(true);
        txtValorNetoPago.setEditable(true);
        cmbCodigoAdministracion.setDisable(false);
        cmbCodigoProveedor.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCuentaPorPagar.clear();
        txtNumeroFactura.clear();
        txtEstadoPago.clear();
        txtValorNetoPago.clear();
        cmbCodigoAdministracion.setValue(null);
        cmbCodigoProveedor.setValue(null);
        fechaLimitePago.selectedDateProperty().set(null);
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
