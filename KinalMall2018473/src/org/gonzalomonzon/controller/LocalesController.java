package org.gonzalomonzon.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gonzalomonzon.bean.Locales;
import org.gonzalomonzon.db.Conexion;
import org.gonzalomonzon.system.Principal;

public class LocalesController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, NINGUNO, ACTUALIZAR, CANCELAR, ELIMNAR};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Locales> listaLocales;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigoLocal;
    @FXML private TextField txtSaldoFavor;
    @FXML private TextField txtSaldoContra;
    @FXML private TextField txtMesesPendientes;
    @FXML private TextField txtDisponibilidad;
    @FXML private TextField txtValorLocal;
    @FXML private TextField txtValorAdministracion;
    @FXML private TableView tblLocales;
    @FXML private TableColumn colCodigoLocal;
    @FXML private TableColumn colSaldoFavor;
    @FXML private TableColumn colSaldoContra;
    @FXML private TableColumn colMesesPendientes;
    @FXML private TableColumn colDisponibilidad;
    @FXML private TableColumn colValorLocal;
    @FXML private TableColumn colValorAdministracion;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblLocales.setItems(getLocales());
        colCodigoLocal.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("codigoLocal"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Locales, Double>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, Double>("saldoContra"));
        colMesesPendientes.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("mesesPendientes"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Locales, Boolean>("disponibilidad"));
        colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, Double>("valorLocal"));
        colValorAdministracion.setCellValueFactory(new PropertyValueFactory<Locales, Double>("valorAdministracion"));
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
    
    public void seleccionarElemento(){
        if(tblLocales.getSelectionModel().getSelectedItem() != null){
            txtCodigoLocal.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getCodigoLocal()));
            txtSaldoFavor.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra()));
            txtMesesPendientes.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getMesesPendientes()));
            txtDisponibilidad.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).isDisponibilidad()));
            txtValorLocal.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getValorLocal()));
            txtValorAdministracion.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getValorAdministracion()));
        }
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
                txtSaldoFavor.setEditable(false);
                txtSaldoContra.setEditable(false);
                txtMesesPendientes.setEditable(false);
                break;
                
            case GUARDAR:
                if(txtDisponibilidad.getText().length() == 0 || txtValorLocal.getText().length() == 0 || txtValorAdministracion.getText().length() == 0){
                    JOptionPane.showMessageDialog(null, "Disponibilidad, valor del local y valor de administración no pueden ir vacíos.");
                }else if(txtValorLocal.getText().length() > 10){
                    JOptionPane.showMessageDialog(null, "El valor que usted ingreso excese la cantidad de números que puede ingresar. Solo puede ingresar 7 números antes del punto y 2 decimales.");
                }else if(txtValorAdministracion.getText().length() > 10){
                    JOptionPane.showMessageDialog(null, "El valor que usted ingreso excese la cantidad de números que puede ingresar. Solo puede ingresar 7 números antes del punto y 2 decimales.");
                }else if(!txtDisponibilidad.getText().contains("true") && !txtDisponibilidad.getText().contains("false")){
                     JOptionPane.showMessageDialog(null, "En disponibilidad solo puede ingresar true o false");
                }else{
                    guardar();
                }
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/gonzalomonzon/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/gonzalomonzon/images/eliminar.png"));
                desactivarControles();
                limpiarControles();
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Locales registro = new Locales();
        registro.setDisponibilidad(Boolean.valueOf(txtDisponibilidad.getText()));
        registro.setValorLocal(Double.parseDouble(txtValorLocal.getText()));
        registro.setValorAdministracion(Double.parseDouble(txtValorAdministracion.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarLocal(?, ?, ?)}");
            procedimiento.setBoolean(1, registro.isDisponibilidad());
            procedimiento.setDouble(2, registro.getValorLocal());
            procedimiento.setDouble(3, registro.getValorAdministracion());
            procedimiento.execute();
            listaLocales.add(registro);
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
                if(tblLocales.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar local", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BorrarLocal(?)}");
                           procedimiento .setInt(1, ((Locales)tblLocales.getSelectionModel().getSelectedItem()).getCodigoLocal());
                           procedimiento.execute();
                           listaLocales.remove(tblLocales.getSelectionModel().getSelectedItem());
                           limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
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
                if(tblLocales.getSelectionModel().getSelectedItem() != null){
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                imgEditar.setImage(new Image("/org/gonzalomonzon/images/actualizar.png"));
                imgReporte.setImage(new Image("/org/gonzalomonzon/images/cancelar.png"));
                activarControles();
                tipoOperacion = operaciones.ACTUALIZAR; 
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro.");
                }break;

            case ACTUALIZAR:
                if(txtDisponibilidad.getText().length() == 0 || txtValorLocal.getText().length() == 0 || txtValorAdministracion.getText().length() == 0){
                    JOptionPane.showMessageDialog(null, "Disponibilidad, valor del local y valor de administración no pueden ir vacíos.");
                }else if(txtSaldoFavor.getText().length() > 14){
                    JOptionPane.showMessageDialog(null, "El valor que usted ingreso excese la cantidad de números que puede ingresar. Solo puede ingresar 11 números enteros y 2 decimales.");
                }else if(txtSaldoContra.getText().length() > 14){
                    JOptionPane.showMessageDialog(null, "El valor que usted ingreso excese la cantidad de números que puede ingresar. Solo puede ingresar 11 números enteros y 2 decimales.");
                }else if(txtValorLocal.getText().length() > 14){
                    JOptionPane.showMessageDialog(null, "El valor que usted ingreso excese la cantidad de números que puede ingresar. Solo puede ingresar 11 números enteros y 2 decimales.");
                }else if(txtValorAdministracion.getText().length() > 14){
                    JOptionPane.showMessageDialog(null, "El valor que usted ingreso excese la cantidad de números que puede ingresar. Solo puede ingresar 11 números enteros y 2 decimales.");
                }else if(!txtDisponibilidad.getText().contains("true") && !txtDisponibilidad.getText().contains("false")){
                     JOptionPane.showMessageDialog(null, "En disponibilidad solo puede ingresar true o false");
                }else{
                    actualizar();
                }
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/gonzalomonzon/images/editar.png"));
                imgReporte.setImage(new Image("/org/gonzalomonzon/images/reporte.png"));
                limpiarControles();
                desactivarControles();
                cargarDatos();
                tipoOperacion = operaciones.NINGUNO;
                break;  
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarLocal(?, ?, ?, ?, ?, ?, ?)}");
            Locales registro = (Locales)tblLocales.getSelectionModel().getSelectedItem();
            registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
            registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
            registro.setMesesPendientes(Integer.valueOf(txtMesesPendientes.getText()));
            registro.setDisponibilidad(Boolean.valueOf(txtDisponibilidad.getText()));
            registro.setValorLocal(Double.parseDouble(txtValorLocal.getText()));
            registro.setValorAdministracion(Double.parseDouble(txtValorAdministracion.getText()));
            procedimiento.setInt(1, registro.getCodigoLocal());
            procedimiento.setDouble(2, registro.getSaldoFavor());
            procedimiento.setDouble(3, registro.getSaldoContra());
            procedimiento.setInt(4, registro.getMesesPendientes());
            procedimiento.setBoolean(5, registro.isDisponibilidad());
            procedimiento.setDouble(6, registro.getValorLocal());
            procedimiento.setDouble(7, registro.getValorAdministracion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/gonzalomonzon/images/editar.png"));
                imgReporte.setImage(new Image("/org/gonzalomonzon/images/reporte.png"));
                limpiarControles();
                desactivarControles();
                tipoOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void desactivarControles(){
        txtCodigoLocal.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
        txtMesesPendientes.setEditable(false);
        txtDisponibilidad.setEditable(false);
        txtValorLocal.setEditable(false);
        txtValorAdministracion.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoLocal.setEditable(false);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
        txtMesesPendientes.setEditable(true);
        txtDisponibilidad.setEditable(true);
        txtValorLocal.setEditable(true);
        txtValorAdministracion.setEditable(true);
    }

    public void limpiarControles(){
        txtCodigoLocal.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        txtMesesPendientes.clear();
        txtDisponibilidad.clear();
        txtValorLocal.clear();
        txtValorAdministracion.clear(); 
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
