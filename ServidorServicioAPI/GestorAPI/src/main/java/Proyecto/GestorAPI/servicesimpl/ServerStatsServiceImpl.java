package Proyecto.GestorAPI.servicesimpl;


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

@Service
@RequiredArgsConstructor
public class ServerStatsServiceImpl {

    private final UserService userService;
    private final OCRServiceImpl ocrService;
    private final SpentService spentService;

    private final SystemInfo systemInfo = new SystemInfo();

    public ServerInfoDto getFullServerInfo() {
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();

        CentralProcessor processor = hal.getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks);

        GlobalMemory memory = hal.getMemory();
        long totalMem = memory.getTotal();
        long usedMem = totalMem - memory.getAvailable();

        File root = new File("/");
        long totalDisk = root.getTotalSpace();
        long usedDisk = totalDisk - root.getFreeSpace();

        Sensors sensors = hal.getSensors();
        double cpuTemp = sensors.getCpuTemperature();

        ServerInfoDto dto = new ServerInfoDto();
        dto.setName("GESTHOR1");
        dto.setUsers(userService.getCountUsers());
        dto.setActiveocr(ocrService.getStatus().isDemo());
        dto.setSpenses(spentService.getCountSpents());
        dto.setActiveapi(ocrService.getStatus().isStatusServer());
        dto.setStorage((int) (totalDisk / 1_073_741_824));
        dto.setUsedStorage((int) (usedDisk / 1_073_741_824));
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());

        dto.setOs(os.toString());
        dto.setCpuLoad(cpuLoad * 100);
        dto.setUptimeSeconds(os.getSystemUptime());
        dto.setTotalMemory(totalMem);
        dto.setUsedMemory(usedMem);
        dto.setTotalDisk(totalDisk);
        dto.setUsedDisk(usedDisk);
        dto.setCpuTemperature(cpuTemp);

        return dto;
    }
}
