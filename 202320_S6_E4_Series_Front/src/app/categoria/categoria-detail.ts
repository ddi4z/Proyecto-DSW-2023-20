import { Serie } from '../serie/serie';
import { Categoria } from './categoria';

export class CategoriaDetail extends Categoria {
    series : Array<Serie> = [];

    constructor(id:number, nombre:string, imagen:string,series:Array<Serie>){
        super(id, nombre,imagen);
        this.series = series;
    }

}
