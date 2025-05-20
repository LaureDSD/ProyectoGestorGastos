package Proyecto.GestorAPI.modelsDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class StatusServerResponse {
    private boolean statusServer;
    private boolean demo;
    private boolean ocrLocal;

    public StatusServerResponse(boolean statusServer, boolean demo, boolean ocrLocal) {
        this.statusServer = statusServer;
        this.demo = demo;
        this.ocrLocal = ocrLocal;
    }
}
