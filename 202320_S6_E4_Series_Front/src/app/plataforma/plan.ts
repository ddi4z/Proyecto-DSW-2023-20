export class Plan {
    id: number;
    nombre: string;
    precio: number;
    puntaje: number;

    constructor(id: number, nombre: string, precio: number, puntaje: number) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.puntaje = puntaje;
    }
}
