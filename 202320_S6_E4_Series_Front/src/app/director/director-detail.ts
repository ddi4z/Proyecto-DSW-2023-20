import { Director } from './director';
import { Serie } from '../serie/serie';

export class DirectorDetail extends Director {

  seriesActuadas : Array<Serie> = [];
  seriesDirigidas : Array<Serie> = [];

  constructor(id:number, nombre:string, foto:string, descripcion:string, seriesActuadas:Array<Serie>, seriesDirigidas:Array<Serie>){
      super(id, nombre, foto, descripcion);
      this.seriesActuadas = seriesActuadas;
      this.seriesDirigidas = seriesDirigidas;
  }
}
