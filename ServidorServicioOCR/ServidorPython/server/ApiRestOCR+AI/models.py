from pydantic import BaseModel
from typing import List, Optional


class Producto(BaseModel):
    """
    Representa un producto en un ticket de compra.

    :param nombre: Nombre del producto.
    :type nombre: str
    :param precio: Precio unitario del producto.
    :type precio: float
    :param cantidad: Cantidad comprada del producto.
    :type cantidad: float
    :param subtotal: Precio total por el producto (precio * cantidad).
    :type subtotal: float
    """
    nombre: str
    precio: float
    cantidad: float
    subtotal: float


class TicketResponse(BaseModel):
    """
    Representa la respuesta estructurada para un ticket de compra.

    :param establecimiento: Nombre del establecimiento donde se realizó la compra.
    :type establecimiento: str
    :param fecha: Fecha del ticket (formato string, opcional).
    :type fecha: Optional[str]
    :param hora: Hora del ticket (formato string, opcional).
    :type hora: Optional[str]
    :param total: Total pagado en el ticket.
    :type total: float
    :param iva: Monto de IVA pagado.
    :type iva: float
    :param categoria: Categoría del ticket.
    :type categoria: str
    :param articulos: Lista de productos comprados.
    :type articulos: List[Producto]
    :param confianza: Nivel de confianza en el procesamiento del ticket (entre 0 y 1).
    :type confianza: float
    """
    establecimiento: str
    fecha: Optional[str]
    hora: Optional[str]
    total: float
    iva: float
    categoria: Optional[int] = None
    articulos: List[Producto]
    confianza: float

class StatusResponse(BaseModel):
    """
    Representa la respuesta de estado del servidor.

    :param status: Estado del servidor ("true" o "false").
    :type statusServer: bool
    :param serverMode: Estado del servidor ("true" o "false").
    :type demo: bool
    :param ocrMode: Estado del servidor ("true" o "false").
    :type ocrLocal: bool
    """
    statusServer: bool
    demo: bool
    ocrLocal: bool