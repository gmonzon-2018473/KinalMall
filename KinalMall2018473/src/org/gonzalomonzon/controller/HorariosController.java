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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gonzalomonzon.bean.Horarios;
import org.gonzalomonzon.db.Conexion;
import org.gonzalomonzon.report.GenerarReporte;
import org.gonzalomonzon.system.Principal;

public class HorariosController implements Initializable{
    private enum operaciones{NINGUNO, GUARDAR, ACTUALIZAR, CANCELAR, ELIMINAR};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Horarios> listaHorarios;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;        
    @FXML private TextField txtCodigoHorario;        
    @FXML private TextField txtHorarioEntrada;       
    @FXML private TextField txtHorarioSalida;       
    @FXML private TextField txtLunes;       
    @FXML private TextField txtMartes;      
    @FXML private TextField txtMiercoles;       
    @FXML private TextField txtJueves;
    @FXML private TextField txtViernes;
    @FXML private ImageView imgNuevo; 
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodigoHorario;
    @FXML private TableColumn colHorarioEntrada;
    @FXML private TableColumn colHorarioSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes; 
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos(){
        tblHorarios.setItems(getHorarios());
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("codigoHorario"));
        colHorarioEntrada.setCellValueFactory(new PropertyValueFactory<Horarios, String>("horarioEntrada"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horarios, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));
    }
    
    public ObservableList<Horarios> getHorarios(){
        ArrayList<Horarios> lista = new ArrayList<Horarios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarHorarios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Horarios(resultado.getInt("codigoHorario"),
                                       resultado.getString("horarioEntrada"),
                                       resultado.getString("horarioSalida"),
                                       resultado.getBoolean("lunes"),
                                       resultado.getBoolean("martes"),
                                       resultado.getBoolean("miercoles"),
                                       resultado.getBoolean("jueves"),
                                       resultado.getBoolean("viernes")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaHorarios = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblHorarios.getSelectionModel().getSelectedItem() != null){
            txtCodigoHorario.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario()));
            txtHorarioEntrada.setText(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada());
            txtHorarioSalida.setText(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
            txtLunes.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isLunes()));
            txtMartes.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isMartes()));
            txtMiercoles.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles()));
            txtJueves.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isJueves()));
            txtViernes.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isViernes()));
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
            break;
            
            case GUARDAR:
                if(txtHorarioEntrada.getText().length() == 0 || txtHorarioSalida.getText().length() == 0){
                    JOptionPane.showMessageDialog(null, "Horario de entrada y horario de salida no pueden ir vacíos");
                }else if(txtHorarioEntrada.getText().length() > 45){
                    JOptionPane.showMessageDialog(null, "Usted a superado el límite de caracteres en el horario de entrada. El horario de entrada solo puede contener 45 caracteres.");
                }else if(txtHorarioSalida.getText().length() > 45){
                    JOptionPane.showMessageDialog(null, "Usted a superado el límite de caracteres en el horario de salida. El horario de salida solo puede contener 45 caracteres.");
                }else if(!txtLunes.getText().contains("true") && !txtLunes.getText().contains("false") && txtLunes.getText().length() > 0){
                     JOptionPane.showMessageDialog(null, "En lunes solo puede ingresar true o false");
                }else if(!txtMartes.getText().contains("true") && !txtMartes.getText().contains("false") && txtMartes.getText().length() > 0){
                     JOptionPane.showMessageDialog(null, "En martes solo puede ingresar true o false");
                }else if(!txtMiercoles.getText().contains("true") && !txtMiercoles.getText().contains("false") && txtMiercoles.getText().length() > 0){
                     JOptionPane.showMessageDialog(null, "En miércoles solo puede ingresar true o false");
                }else if(!txtJueves.getText().contains("true") && !txtJueves.getText().contains("false") && txtJueves.getText().length() > 0){
                     JOptionPane.showMessageDialog(null, "En jueves solo puede ingresar true o false");
                }else if(!txtViernes.getText().contains("true") && !txtViernes.getText().contains("false") && txtViernes.getText().length() > 0){
                     JOptionPane.showMessageDialog(null, "En viernes solo puede ingresar true o false");
                }else if(txtHorarioEntrada.getText().length() > 5){
                    JOptionPane.showMessageDialog(null, "El horario de entrada que ingreso contiene mas de 5 caracteres. Solo puede contener 5 caracteres.");
                }else if(txtHorarioSalida.getText().length() == 0){
                    JOptionPane.showMessageDialog(null, "El horario de salida que ingreso contiene mas de 5 caracteres. Solo puede contener 5 caracteres.");

                }else{
                    guardar();
                }
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/gonzalomonzon/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/gonzalomonzon/images/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                desactivarControles();
                limpiarControles();
            break;
        }
    }
    
    public void guardar(){
        Horarios registro = new Horarios();
        registro.setHorarioEntrada(txtHorarioEntrada.getText());
        registro.setHorarioSalida(txtHorarioSalida.getText());
        registro.setLunes(Boolean.valueOf(txtLunes.getText()));
        registro.setMartes(Boolean.valueOf(txtMartes.getText()));
        registro.setMiercoles(Boolean.valueOf(txtMiercoles.getText()));
        registro.setJueves(Boolean.valueOf(txtJueves.getText()));
        registro.setViernes(Boolean.valueOf(txtViernes.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarHorario(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getHorarioEntrada());
            procedimiento.setString(2, registro.getHorarioSalida());
            procedimiento.setBoolean(3, registro.isLunes());
            procedimiento.setBoolean(4, registro.isMartes());
            procedimiento.setBoolean(5, registro.isMiercoles());
            procedimiento.setBoolean(6, registro.isJueves());
            procedimiento.setBoolean(7, registro.isViernes());
            procedimiento.execute();
            listaHorarios.add(registro);
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
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar horario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BorrarHorario(?)}");
                            procedimiento.setInt(1, ((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                            procedimiento.execute();
                            listaHorarios.remove(tblHorarios.getSelectionModel().getSelectedItem());
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
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/gonzalomonzon/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/gonzalomonzon/images/cancelar.png"));
                    activarControles();
                    seleccionarElemento();
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro.");
                }
            break;
            
            case ACTUALIZAR:
                if(txtHorarioEntrada.getText().length() == 0 || txtHorarioSalida.getText().length() == 0){
                    JOptionPane.showMessageDialog(null, "Horario de entrada y horario de salida no pueden ir nulos.");
                }else if(txtHorarioEntrada.getText().length() > 45){
                    JOptionPane.showMessageDialog(null, "Usted a superado el límite de caracteres en el horario de entrada. El horario de entrada solo puede contener 45 caracteres.");
                }else if(txtHorarioSalida.getText().length() > 45){
                    JOptionPane.showMessageDialog(null, "Usted a superado el límite de caracteres en el horario de salida. El horario de salida solo puede contener 45 caracteres.");
                }else if(!txtLunes.getText().contains("true") && !txtLunes.getText().contains("false")){
                     JOptionPane.showMessageDialog(null, "En lunes solo puede ingresar true o false");
                }else if(!txtMartes.getText().contains("true") && !txtMartes.getText().contains("false")){
                     JOptionPane.showMessageDialog(null, "En martes solo puede ingresar true o false");
                }else if(!txtMiercoles.getText().contains("true") && !txtMiercoles.getText().contains("false")){
                     JOptionPane.showMessageDialog(null, "En miércoles solo puede ingresar true o false");
                }else if(!txtJueves.getText().contains("true") && !txtJueves.getText().contains("false")){
                     JOptionPane.showMessageDialog(null, "En jueves solo puede ingresar true o false");
                }else if(!txtViernes.getText().contains("true") && !txtViernes.getText().contains("false")){
                     JOptionPane.showMessageDialog(null, "En viernes solo puede ingresar true o false");
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
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarHorario(?, ?, ?, ?, ?, ?, ?, ?)}");
            Horarios registro = (Horarios)tblHorarios.getSelectionModel().getSelectedItem();
            registro.setHorarioEntrada(txtHorarioEntrada.getText());
            registro.setHorarioSalida(txtHorarioSalida.getText());
            registro.setLunes(Boolean.valueOf(txtLunes.getText()));
            registro.setMartes(Boolean.valueOf(txtMartes.getText()));
            registro.setMiercoles(Boolean.valueOf(txtMiercoles.getText()));
            registro.setJueves(Boolean.valueOf(txtJueves.getText()));
            registro.setViernes(Boolean.valueOf(txtViernes.getText()));
            procedimiento.setInt(1, registro.getCodigoHorario());
            procedimiento.setString(2, registro.getHorarioEntrada());
            procedimiento.setString(3, registro.getHorarioSalida());
            procedimiento.setBoolean(4, registro.isLunes());
            procedimiento.setBoolean(5, registro.isMartes());
            procedimiento.setBoolean(6, registro.isMiercoles());
            procedimiento.setBoolean(7, registro.isJueves());
            procedimiento.setBoolean(8, registro.isViernes());
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
            
            case NINGUNO:
                imprimirReporte();
            break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoHorario", null);
        GenerarReporte.mostrarReporte("ReporteHorarios.jasper", "Reporte de horarios", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoHorario.setEditable(false);
        txtHorarioEntrada.setEditable(false);
        txtHorarioSalida.setEditable(false);
        txtLunes.setEditable(false);
        txtMartes.setEditable(false);
        txtMiercoles.setEditable(false);
        txtJueves.setEditable(false);
        txtViernes.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoHorario.setEditable(false);
        txtHorarioEntrada.setEditable(true);
        txtHorarioSalida.setEditable(true);
        txtLunes.setEditable(true);
        txtMartes.setEditable(true);
        txtMiercoles.setEditable(true);
        txtJueves.setEditable(true);
        txtViernes.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoHorario.clear();
        txtHorarioEntrada.clear();
        txtHorarioSalida.clear();
        txtLunes.clear();
        txtMartes.clear();
        txtMiercoles.clear();
        txtJueves.clear();
        txtViernes.clear();
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
