import { Component, OnInit } from '@angular/core';
import { SerieService } from '../serie.service';
import { ActivatedRoute } from '@angular/router';
import { SerieDetail } from '../serie-detail';
import { EpisodioService } from '../episodio.service';
import { Episodio } from '../episodio';

@Component({
  selector: 'app-serie-detail',
  templateUrl: './serie-detail.component.html',
  styleUrls: ['./serie-detail.component.css']
})
export class SerieDetailComponent implements OnInit {
  serieId!: string;
  episodios: Array<Episodio> = [];
  serieDetail: SerieDetail | null = null;
  showDetail = false;
  episodioSeleccionado: any;
  searchedEpisodio: any;
  itemsPerPage: number = 12;
  p: number = 1; 

  mostrarDetallesEpisodio(episodio: any) {
    this.episodioSeleccionado = episodio;
    const element = document.getElementById('episodio-detail');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  constructor(
    private route: ActivatedRoute,
    private serieService: SerieService,
    private episodioService: EpisodioService
  ) { }

  getSerie(): void {
    this.route.params.subscribe((params) => {
      this.serieId = params['id'];
      if (this.serieId) {
        this.serieService.getSerie(this.serieId).subscribe(
          (apiData) => {
            this.serieDetail = apiData;
            this.showDetail = true; 
          },
        );
      }
    });
  }
  getEpisodios(): void {
    this.episodioService.getEpisodios(this.serieId).subscribe({
      next: apiData => {
        this.episodios = apiData;
        this.episodioSeleccionado = this.episodios[0];
      },
      error: e => console.error(e)
    });
  }

  getEpisodiosAlfabetico(): void {
    this.episodioService.getEpisodiosAlfabetico(this.serieId).subscribe({
      next: apiData => this.episodios = apiData,
      error: e => console.error(e)
    });
  }
  getEpisodiosAntiAlfabetico(): void {
    this.episodioService.getEpisodiosAntiAlfabetico(this.serieId).subscribe({
      next: apiData => this.episodios = apiData,
      error: e => console.error(e)
    });
  }

  onSearchChangeEpisodio() {
    this.p = 1;
  }

  ngOnInit() {
    this.getSerie();
    this.getEpisodios();
  }
}
