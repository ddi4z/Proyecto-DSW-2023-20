export class Director {
    id: number;
    descripcion: string;
    foto: string;
    nombre: string;
  
    constructor(id: number, descripcion: string, foto: string, nombre: string) {
      this.id = id;
      this.descripcion = descripcion;
      this.foto = foto;
      this.nombre = nombre;
    }
  }