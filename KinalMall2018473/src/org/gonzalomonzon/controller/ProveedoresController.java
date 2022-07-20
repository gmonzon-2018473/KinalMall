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
import org.gonzalomonzon.bean.Proveedores;
import org.gonzalomonzon.db.Conexion;
import org.gonzalomonzon.report.GenerarReporte;
import org.gonzalomonzon.system.Principal;

public class ProveedoresController implements Initializable{
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NUEVO, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Proveedores> listaProveedores;
    private ObservableList<Administracion> listaAdministracion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;       
    @FXML private Button btnEditar;       
    @FXML private Button btnReporte;       
    @FXML private ImageView imgNuevo;       
    @FXML private ImageView imgEliminar;       
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblProveedores;
    @FXML private TableColumn colCodigoProveedor;
    @FXML private TableColumn colNITProveedor;
    @FXML private TableColumn colServicioPrestado;
    @FXML private TableColumn colTelefonoProveedor;       
    @FXML private TableColumn colDireccionProveedor;
    @FXML private TableColumn colSaldoFavor;
    @FXML private TableColumn colSaldoContra;
    @FXML private TableColumn colCodigoAdministracion;
    @FXML private TextField txtCodigoProveedor;
    @FXML private TextField txtNITProveedor;
    @FXML private TextField txtServicioPrestado;
    @FXML private TextField txtTelefonoProveedor;
    @FXML private TextField txtDireccionProveedor;      
    @FXML private TextField txtSaldoFavor;       
    @FXML private TextField txtSaldoContra;
    @FXML private ComboBox cmbCodigoAdministracion;        
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblProveedores.setItems(getProveedores());
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("codigoProveedor"));
        colNITProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("NITProveedor"));
        colServicioPrestado.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("servicioPrestado"));
        colTelefonoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("telefonoProveedor"));
        colDireccionProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccionProveedor"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedores, Double>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Proveedores, Double>("saldoContra"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        cmbCodigoAdministracion.setItems(getAdministracion());
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
    
    public void seleccionarElemento(){
        if(tblProveedores.getSelectionModel().getSelectedItem() != null){
            txtCodigoProveedor.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            txtNITProveedor.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNITProveedor());
            txtServicioPrestado.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getServicioPrestado());
            txtTelefonoProveedor.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getTelefonoProveedor());
            txtDireccionProveedor.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor());
            txtSaldoFavor.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra()));
            cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
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
                 if(cmbCodigoAdministracion.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null, "Debe completar los datos");
                }else if(txtNITProveedor.getText().length() == 0 || txtServicioPrestado.getText().length() == 0 || txtDireccionProveedor.getText().length() == 0){
                    JOptionPane.showMessageDialog(null, "Debe completar los datos");
                }else if(txtNITProveedor.getText().length() > 45 || txtServicioPrestado.getText().length() > 45 || txtDireccionProveedor.getText().length() > 45){
                    JOptionPane.showMessageDialog(null, "Usted a superado el límite de los caracteres en alguno de los campos de arriba. Los campos solo pueden contener 45 caracteres.");
                }else if(txtTelefonoProveedor.getText().length() > 8){
                    JOptionPane.showMessageDialog(null, "El teléfono que ingreso tiene más de 8 números. Ingrese un teléfono válido.");
                }else if(txtTelefonoProveedor.getText().length() < 8){
                      JOptionPane.showMessageDialog(null, "El teléfono que ingreso tiene menos de 8 números. Ingrese un teléfono válido.");                                                                                                                                                                                              
                }else if(txtSaldoFavor.getText().length() >  14 || txtSaldoContra.getText().length() > 14){
                    JOptionPane.showMessageDialog(null, "El valor que usted ingreso excese la cantidad de números que puede ingresar. Solo puede ingresar 11 números enteros y 2 decimales.");
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
        Proveedores registro = new Proveedores();
        registro.setNITProveedor(txtNITProveedor.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setTelefonoProveedor(txtTelefonoProveedor.getText());
        registro.setDireccionProveedor(txtDireccionProveedor.getText());
        registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
        registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
        registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProveedor(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getNITProveedor());
            procedimiento.setString(2, registro.getServicioPrestado());
            procedimiento.setString(3, registro.getTelefonoProveedor());
            procedimiento.setString(4, registro.getDireccionProveedor());
            procedimiento.setDouble(5, registro.getSaldoFavor());
            procedimiento.setDouble(6, registro.getSaldoContra());
            procedimiento.setInt(7, registro.getCodigoAdministracion());
            procedimiento.execute();
            listaProveedores.add(registro);
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
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar proveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BorrarProveedor(?)}");
                            procedimiento.setInt(1, ((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProveedores.remove(tblProveedores.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        }catch(Exception e){
                            
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro.");
                }
        }
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                activarControles();
                limpiarControles();
                cmbCodigoAdministracion.setDisable(true);
                btnReporte.setText("Cancelar");
                btnEditar.setText("Actualizar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                imgEditar.setImage(new Image("/org/gonzalomonzon/images/actualizar.png"));
                imgReporte.setImage(new Image("/org/gonzalomonzon/images/cancelar.png"));
                tipoOperacion = operaciones.ACTUALIZAR;
                seleccionarElemento();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro.");
                }

            break;
            
            case ACTUALIZAR:
                 if(cmbCodigoAdministracion.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null, "Debe completar los datos");
                }else if(txtNITProveedor.getText().length() == 0 || txtServicioPrestado.getText().length() == 0 || txtDireccionProveedor.getText().length() == 0){
                    JOptionPane.showMessageDialog(null, "Debe completar los datos");
                }else if(txtNITProveedor.getText().length() > 45 || txtServicioPrestado.getText().length() > 45 || txtDireccionProveedor.getText().length() > 45){
                    JOptionPane.showMessageDialog(null, "Usted a superado el límite de los caracteres en alguno de los campos de arriba. Los campos solo pueden contener 45 caracteres.");
                }else if(txtTelefonoProveedor.getText().length() > 8){
                    JOptionPane.showMessageDialog(null, "El teléfono que ingreso tiene más de 8 números. Ingrese un teléfono válido.");
                }else if(txtTelefonoProveedor.getText().length() < 8){
                      JOptionPane.showMessageDialog(null, "El teléfono que ingreso tiene menos de 8 números. Ingrese un teléfono válido.");                                                                                                                                                                                              
                }else if(txtSaldoFavor.getText().length() >  14 || txtSaldoContra.getText().length() > 14){
                    JOptionPane.showMessageDialog(null, "El valor que usted ingreso excese la cantidad de números que puede ingresar. Solo puede ingresar 11 números enteros y 2 decimales.");
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProveedor(?, ?, ?, ?, ?, ?, ?)}");
            Proveedores registro = (Proveedores)tblProveedores.getSelectionModel().getSelectedItem();
            registro.setNITProveedor(txtNITProveedor.getText());
            registro.setServicioPrestado(txtServicioPrestado.getText());
            registro.setTelefonoProveedor(txtTelefonoProveedor.getText());
            registro.setDireccionProveedor(txtDireccionProveedor.getText());
            registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
            registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getServicioPrestado());
            procedimiento.setString(4, registro.getTelefonoProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setDouble(6, registro.getSaldoFavor());
            procedimiento.setDouble(7, registro.getSaldoContra());
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
                imprimirReporte();
            break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoProveedor", null);
        GenerarReporte.mostrarReporte("ReporteProveedores.jasper", "Reporte de proveedores", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoProveedor.setEditable(false);
        txtCodigoProveedor.setEditable(false);
        txtNITProveedor.setEditable(false);
        txtServicioPrestado.setEditable(false);
        txtTelefonoProveedor.setEditable(false);
        txtDireccionProveedor.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
        cmbCodigoAdministracion.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoProveedor.setEditable(false);
        txtNITProveedor.setEditable(true);
        txtServicioPrestado.setEditable(true);
        txtTelefonoProveedor.setEditable(true);
        txtDireccionProveedor.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
        cmbCodigoAdministracion.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoProveedor.clear();
        txtNITProveedor.clear();
        txtServicioPrestado.clear();
        txtTelefonoProveedor.clear();
        txtDireccionProveedor.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        cmbCodigoAdministracion.setValue(null);
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
