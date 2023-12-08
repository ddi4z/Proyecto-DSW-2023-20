export class Plataforma {
    id: number;
    nombre: string;
    sitioWeb: string;
    logo: string;

    constructor( 
        id: number,
        nombre: string,
        sitioWeb: string,
        logo: string
    ) {
        this.id = id;
        this.nombre = nombre;
        this.sitioWeb = sitioWeb;
        this.logo = logo;
    }
}