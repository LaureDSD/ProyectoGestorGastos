package Proyecto.GestorAPI.models.log;

import Proyecto.GestorAPI.models.usuario.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


// (Correcto)

@Entity
@Table(name = "logs")
@Schema(description = "Entidad que representa los registros de logs del sistema")
public class LogUsuario {

    //ID del log
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del log", example = "1")
    private Long log_id;

    // Usuario relacionado
    @ManyToOne
    @JoinColumn(name = "usuarioId")
    //@Column(name = "usuario_id", nullable = false)
    @Schema(description = "Usuario relacionado con el log", example = "123")
    private Usuario usuario;

    //Tipo de log
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_log", nullable = false)
    @Schema(description = "Tipo de log", example = "informacion")
    private TipoLog tipoLog;

    //Mensaje descriptivo
    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Mensaje del log", example = "Error al guardar los datos")
    private String mensaje;

    //Fecha de emision
    @DateTimeFormat
    @Column(name = "fecha_log", nullable = false)
    @Schema(description = "Fecha y hora del log", example = "2023-10-01T12:00:00")
    private Date fechaLog = new Date();

    public LogUsuario() {
    }

    public Long getLog_id() {
        return log_id;
    }

    public void setLog_id(Long log_id) {
        this.log_id = log_id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoLog getTipoLog() {
        return tipoLog;
    }

    public void setTipoLog(TipoLog tipoLog) {
        this.tipoLog = tipoLog;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaLog() {
        return fechaLog;
    }

    public void setFechaLog(Date fechaLog) {
        this.fechaLog = fechaLog;
    }
}

