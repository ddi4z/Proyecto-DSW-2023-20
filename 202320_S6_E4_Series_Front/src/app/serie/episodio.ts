export class Episodio {
    id: number;
    numeroEpisodio: number;
    nombre: string;
    resumen: string;
    duracionMinutos: number;
    imagen: string;
  
    constructor(id:number, numeroEpisodio:number, nombre: string, resumen: string, duracionMinutos:number, imagen: string ) { 
      this.id = id;
      this.numeroEpisodio = numeroEpisodio;
      this.nombre = nombre;
      this.resumen = resumen;
      this.duracionMinutos = duracionMinutos;
      this.imagen = imagen;
    }
  }