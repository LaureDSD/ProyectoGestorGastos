from fastapi import FastAPI, UploadFile, File
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Dict, Optional
import uuid
import cv2
import numpy as np

app = FastAPI(title="OCR Ticket Processor")

# Config CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

class Producto(BaseModel):
    nombre: str
    cantidad: str
    precio: str
    categoria: List[str]

class TicketResponse(BaseModel):
    establecimiento: str
    fecha: Optional[str]
    hora: Optional[str]
    total: float
    articulos: List[Producto]
    confianza: float

def mock_ocr_processing(image_path: str) -> dict:
    """Función simulada de procesamiento OCR"""
    # Aquí iría tu lógica real de OCR
    return {
        "establecimiento": "Supermercado Ejemplo",
        "fecha": "15/06/2023",
        "hora": "18:30",
        "total": 45.75,
        "articulos": [
            {
                "nombre": "Leche Entera",
                "cantidad": "2",
                "precio": "12.50",
                "categoria": ["lacteos", "frescos"]
            },
            {
                "nombre": "Pan Integral",
                "cantidad": "1",
                "precio": "5.25",
                "categoria": ["panaderia"]
            },
            {
                "nombre": "Manzanas",
                "cantidad": "1.5",
                "precio": "28.00",
                "categoria": ["frutas", "frescos"]
            }
        ],
        "confianza": 0.92
    }

@app.post("/process", response_model=TicketResponse)
async def process_ticket_image(image: UploadFile = File(...)):
    # 1. Guardar imagen temporalmente
    temp_file = f"/tmp/{uuid.uuid4()}.jpg"
    with open(temp_file, "wb") as buffer:
        buffer.write(await image.read())
    
    try:
        # 2. Procesar imagen (aquí iría tu lógica OCR real)
        ocr_result = mock_ocr_processing(temp_file)
        
        # 3. Devolver resultado estructurado
        return {
            "establecimiento": ocr_result["establecimiento"],
            "fecha": ocr_result["fecha"],
            "hora": ocr_result["hora"],
            "total": ocr_result["total"],
            "articulos": ocr_result["articulos"],
            "confianza": ocr_result["confianza"]
        }
    finally:
        # Limpiar archivo temporal
        import os
        if os.path.exists(temp_file):
            os.remove(temp_file)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=5000)