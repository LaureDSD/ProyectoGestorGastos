package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.exceptions.ErrorConexionServidorException;
import Proyecto.GestorAPI.modelsDTO.ServerInfoDto;
import Proyecto.GestorAPI.services.SpentService;
import Proyecto.GestorAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

/**
 * Servicio que recopila y proporciona información detallada sobre el estado del servidor.
 *
 * Utiliza la librería OSHI para obtener métricas del hardware y sistema operativo,
 * además de combinar datos propios de la aplicación a través de servicios inyectados.
 *
 * Anotado con @Service para ser detectado como componente de Spring,
 * y @RequiredArgsConstructor para inyección automática de dependencias finales.
 */
@Service
@RequiredArgsConstructor
public class ServerStatsServiceImpl {

    /**
     * Servicio para obtener la cantidad de usuarios registrados en el sistema.
     */
    private final UserService userService;

    /**
     * Servicio OCR para obtener estado del servidor OCR.
     */
    private final OCRServiceImpl ocrService;

    /**
     * Servicio para obtener la cantidad de gastos registrados.
     */
    private final SpentService spentService;

    /**
     * Objeto principal de OSHI para acceder a información del hardware y sistema operativo.
     */
    private final SystemInfo systemInfo = new SystemInfo();

    /**
     * Obtiene un objeto DTO con información completa del servidor, incluyendo:
     * - Nombre del servidor (hardcodeado como "GESTHOR1")
     * - Cantidad de usuarios
     * - Estado del servicio OCR (demo y servidor activo)
     * - Cantidad de gastos registrados
     * - Espacio total y usado en disco (en GB)
     * - Información de sistema operativo y hardware:
     *   carga CPU (%), tiempo de actividad, memoria total y usada,
     *   disco total y usado, temperatura CPU.
     * - Fecha de creación y actualización del DTO (momento de la consulta).
     *
     * @return ServerInfoDto con los datos completos de estado del servidor.
     */
    public ServerInfoDto getFullServerInfo() throws ErrorConexionServidorException {
        // Obtiene la capa de hardware y sistema operativo
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();

        // Obtiene datos de CPU y calcula la carga tras 1 segundo de espera
        CentralProcessor processor = hal.getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks);

        // Obtiene memoria total y memoria usada actualmente
        GlobalMemory memory = hal.getMemory();
        long totalMem = memory.getTotal();
        long usedMem = totalMem - memory.getAvailable();

        // Obtiene espacio total y usado en disco raíz "/"
        File root = new File("/");
        long totalDisk = root.getTotalSpace();
        long usedDisk = totalDisk - root.getFreeSpace();

        // Obtiene temperatura de CPU mediante sensores
        Sensors sensors = hal.getSensors();
        double cpuTemp = sensors.getCpuTemperature();

        // Crea y rellena DTO con toda la información recopilada
        ServerInfoDto dto = new ServerInfoDto();
        dto.setName("GESTHOR1");                                // Nombre estático del servidor
        dto.setUsers(userService.getCountUsers());              // Cantidad de usuarios registrados
        dto.setActiveocr(ocrService.getStatus().isDemo());      // Estado demo del OCR
        dto.setSpenses(spentService.getCountSpents());          // Cantidad de gastos registrados
        dto.setActiveapi(ocrService.getStatus().isStatusServer()); // Estado activo del servidor OCR

        // Conversión a gigabytes para almacenamiento
        dto.setStorage((int) (totalDisk / 1_073_741_824));
        dto.setUsedStorage((int) (usedDisk / 1_073_741_824));

        // Fecha y hora actuales para creación y actualización del DTO
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());

        // Información detallada del sistema operativo y hardware
        dto.setOs(os.toString());               // Descripción del sistema operativo
        dto.setCpuLoad(cpuLoad * 100);          // Carga CPU en porcentaje (0-100)
        dto.setUptimeSeconds(os.getSystemUptime()); // Tiempo de actividad del sistema en segundos
        dto.setTotalMemory(totalMem);           // Memoria total en bytes
        dto.setUsedMemory(usedMem);              // Memoria usada en bytes
        dto.setTotalDisk(totalDisk);             // Espacio total en disco en bytes
        dto.setUsedDisk(usedDisk);               // Espacio usado en disco en bytes
        dto.setCpuTemperature(cpuTemp);          // Temperatura de CPU en grados Celsius

        // Retorna el DTO con toda la información lista para consumo
        return dto;
    }
}
