import { Serie } from './serie';
import { Categoria } from '../categoria/categoria';
import { Director } from '../director/director';
import { Actor } from '../actor/actor';
import { Episodio } from './episodio';
import { Plataforma } from '../plataforma/plataforma';

export class SerieDetail extends Serie {

  categorias: Array<Categoria> = [];
  directores: Array<Director> = [];
  actores: Array<Actor> = [];
  episodios: Array<Episodio> = [];
  plataformas: Array<Plataforma> = [];

  constructor(
    id:number, 
    anio:number,
    nombre: string, 
    imagen: string, 
    sinopsis: string,
    wallpaper: string,
    categorias: Array<Categoria>,
    directores: Array<Director>,
    actores: Array<Actor>,
    episodios: Array<Episodio>,
    plataformas: Array<Plataforma> 
    ) { 
    super(id, anio, nombre, imagen, sinopsis,wallpaper);
    this.categorias = categorias;
    this.directores = directores;
    this.actores = actores;
    this.episodios = episodios;
    this.plataformas = plataformas;
  }

}
