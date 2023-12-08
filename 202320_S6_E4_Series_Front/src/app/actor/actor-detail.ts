import { Actor } from './actor';
import { Serie } from '../serie/serie';

export class ActorDetail extends Actor {

    seriesActuadas : Array<Serie> = [];
    seriesDirigidas : Array<Serie> = [];

    constructor(id:number, nombre:string, foto:string, descripcion:string, seriesActuadas:Array<Serie>, seriesDirigidas:Array<Serie>){
        super(id, nombre, foto, descripcion);
        this.seriesActuadas = seriesActuadas;
        this.seriesDirigidas = seriesDirigidas;
    }
}        