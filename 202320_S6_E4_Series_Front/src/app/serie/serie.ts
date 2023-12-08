export class Serie {
    id: number;
    anio: number;
    nombre: string;
    imagen: string;
    sinopsis: string;
    wallpaper: string;
  
    constructor(id:number, anio:number,nombre: string, imagen: string, sinopsis: string, wallpaper: string) { 
      this.id = id;
      this.anio = anio;
      this.nombre = nombre;
      this.sinopsis = sinopsis;
      this.imagen = imagen;
      this.wallpaper = imagen;
    }
  }