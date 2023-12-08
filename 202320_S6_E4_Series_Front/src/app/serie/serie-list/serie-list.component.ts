import { Component, OnInit } from '@angular/core';
import { SerieDetail } from '../serie-detail';
import { SerieService } from '../serie.service';
import { PlataformaService} from '../../plataforma/plataforma.service';
import { PlataformaDetail } from 'src/app/plataforma/plataforma-detail';
import { CategoriaDetail } from 'src/app/categoria/categoria-detail';


@Component({
  selector: 'app-serie-list',
  templateUrl: './serie-list.component.html',
  styleUrls: ['./serie-list.component.css']
})
export class SerieListComponent implements OnInit {
  series: Array<SerieDetail> = [];
  categorias: Array<CategoriaDetail> = [];
  plataformas: Array<PlataformaDetail> = [];
  selectedSerie!: SerieDetail | null;
  selected: boolean = false;
  p: number = 1; 
  itemsPerPage: number = 18;
  searchedSerie: any;
  searchedPlataforma: string = "";
  sort: string = "";
  anioMenor: number = 0;
  anioSuperior: number = 3000;

  serieCarrousel1!: SerieDetail | null;
  serieCarrousel2!: SerieDetail | null;
  serieCarrousel3!: SerieDetail | null;
  serieCarrousel4!: SerieDetail | null;
  serieCarrousel5!: SerieDetail | null;




  constructor(private serieService: SerieService, private plataformaService: PlataformaService) { }


  ngOnInit() {
    this.getSeries();
    this.getPlataformas();
    this.cargarCarrousel();

  }

  cargarCarrousel() {
    this.serieService.getSeries().subscribe({
      next: apiData => {
        let series = apiData;
        let serie1 = series[4];
        let serie2 = series[29];
        let serie3 = series[17];



        this.serieCarrousel1 = serie1;
        this.serieCarrousel2 = serie2;
        this.serieCarrousel3 = serie3;



      },
      error: e => console.error(e)
    });
  }

  getSeriesAlfabeticamente(): void {
    this.serieService.getSeriesAlfabeticamente().subscribe({
      next: apiData => this.series = apiData
    });
  }

  getSeriesAntiAlfabeticamente(): void {
    this.serieService.getSeriesAntiAlfabeticamente().subscribe({
      next: apiData => this.series = apiData
      
    });
  }

  onSearchChangeSerie() {
    this.p = 1;
  }


  getSeries(): void {
    this.serieService.getSeries().subscribe({
      next: apiData => this.series = apiData
    });
  }



  getPlataformas(): void {
    this.plataformaService.getPlataformas().subscribe({
      next: apiData => this.plataformas = apiData
    });
  }

  onSelected(serie: SerieDetail): void {
    this.selected = true;
    this.selectedSerie = serie;
  }


  
  onChangePlataforma(plataforma: string) {
    this.searchedPlataforma = plataforma;
  }
  onChangeAnio(anio1:number, anio2:number) {
    this.anioMenor = anio1;
    this.anioSuperior = anio2;
  }

  onMouseEnter(serie: any) {
    this.selectedSerie = serie;
  }

  onMouseLeave() {
    this.selectedSerie = null;
  }




  categoriasAString(categorias: any) {
    let categoriasString = "";
    for(let i = 0; i<categorias.length; i++) {
      categoriasString += categorias[i].nombre;
      if(i != categorias.length-1) {
        categoriasString += ", ";
      }
    }
    return categoriasString;
  }

  acortarSinopsis(sinopsis: string) {
    if(sinopsis.length > 90) {
      return sinopsis.substring(0, 90) + "...";
    }
  
    return sinopsis;
  }

  acortarSinopsisCarrousel(sinopsis?: string) {
    if(sinopsis == null) {
      return "";
    }
    if(sinopsis.length > 290) {
      return sinopsis.substring(0, 290) + "...";
    }
  
    return sinopsis;
  }





}
