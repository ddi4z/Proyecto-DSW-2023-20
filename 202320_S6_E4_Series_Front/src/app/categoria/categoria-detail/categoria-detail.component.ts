import { Component, OnInit } from '@angular/core';
import { CategoriaService } from '../categoria.service';
import { ActivatedRoute } from '@angular/router';
import { CategoriaDetail } from '../categoria-detail';
import { SerieDetail } from 'src/app/serie/serie-detail';


@Component({
  selector: 'app-categoria-detail',
  templateUrl: './categoria-detail.component.html',
  styleUrls: ['./categoria-detail.component.css']
})
export class CategoriaDetailComponent implements OnInit {
  categoriaId!: string;
  categoriaDetail: CategoriaDetail | null = null;
  showDetail = false;
  p: number = 1; 
  itemsPerPage: number = 18;
  searchedSerie: any;
  selectedSerieCategoria!: SerieDetail | null;
  selected: boolean = false;
  seriesCategoria: Array<SerieDetail> = [];
  
  constructor(
    private route: ActivatedRoute,
    private categoriaService: CategoriaService
  ) { }
  onMouseEnterCategoria(serie: any) {
    this.selectedSerieCategoria = serie;
  }

  onMouseLeaveCategoria() {
    this.selectedSerieCategoria = null;
  }

  categoriasSerieAString(categorias: any) {
    let categoriasSerieString = "";
    for(let i = 0; i<categorias.length; i++) {
      categoriasSerieString += categorias[i].nombre;
      if(i != categorias.length-1) {
        categoriasSerieString += ", ";
      }
    }
    return categoriasSerieString;
  }

  acortarSinopsisSerieCategoria(sinopsisSerie: string) {
    if(sinopsisSerie.length > 90) {
      return sinopsisSerie.substring(0, 90) + "...";
    }

    return sinopsisSerie;
  }

  getSeriesCategoriaAlfabeticamente(): void {
    this.categoriaService.getSeriesCategoriaAlfabeticamente(this.categoriaId).subscribe({
      next: apiData => this.seriesCategoria = apiData
    });
  }

  getSeriesCategoriaAntiAlfabeticamente(): void {
    this.categoriaService.getSeriesCategoriaAntiAlfabeticamente(this.categoriaId).subscribe({
      next: apiData => this.seriesCategoria = apiData
    });
  }


  getSeriesCategoria(): void {
    this.categoriaService.getSeriesCategoria(this.categoriaId).subscribe({
      next: apiData => this.seriesCategoria = apiData
    });
  }

  onSearchChangeSerieCategoria() {
    this.p = 1;
  }
  
  ngOnInit() {
    
    this.route.params.subscribe((params) => {
      this.categoriaId = params['id'];
      if (this.categoriaId) {
        this.categoriaService.getCategoria(this.categoriaId).subscribe(
          (apiData) => {
            this.categoriaDetail = apiData;
            this.showDetail = true; 
          },
        );
      }
      this.getSeriesCategoria();
    });
    
  }
}
