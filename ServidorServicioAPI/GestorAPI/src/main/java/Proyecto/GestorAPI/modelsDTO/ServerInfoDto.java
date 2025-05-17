package Proyecto.GestorAPI.modelsDTO;

import lombok.Data;
import java.util.Date;

@Data
public class ServerInfoDto {

    private String name;
    private int users;
    private boolean activeocr;
    private int spenses;
    private boolean activeapi;
    private int storage;       // en GB
    private int usedStorage;   // en GB
    private Date createdAt;
    private Date updatedAt;
    private String os;
    private double cpuLoad;           // en porcentaje
    private long uptimeSeconds;
    private long totalMemory;         // en bytes
    private long usedMemory;          // en bytes
    private long totalDisk;           // en bytes
    private long usedDisk;            // en bytes
    private double cpuTemperature;    // en °C
}
