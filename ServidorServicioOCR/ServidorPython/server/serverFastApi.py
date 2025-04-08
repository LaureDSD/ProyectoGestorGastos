from fastapi import FastAPI, UploadFile, Form
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Optional, List, Dict, Union

app = FastAPI(title="Servicio Test OCR GesThor")

# Permitir CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class TicketResponse(BaseModel):
    """
    Modelo de resposta para o procesamento de tickets.
    """
    establecimiento: str
    fecha: Optional[str]
    hora: Optional[str]
    total: float
    articulos: List[Dict[str, List[str]]]
    confianza: float

@app.post("/api/ocr/tickets")
async def procesar(
    archivo: UploadFile,
):

    """
    Este endpoint sempre retorna un ticket por defecto para fins de test.
    """
    ticket_default = {
        "establecimiento": "Establecimiento de prueba",
        "fecha": "01/01/2025",
        "hora": "12:00",
        "total": 25.50,
        "articulos": [
            {"nombre": "Producto 1",
            "cantidad": "1",
            "precio": "10.00",
            "categoria":["carne","proteina"]},

            {"nombre": "Producto 2", 
            "cantidad": "1", 
            "precio": "15.50",
            "categoria":["item"]}
        ],
        "confianza": 0.9
    }
    
    return {
        "exito": True,
        "datos": ticket_default
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="localhost", port=5000)
