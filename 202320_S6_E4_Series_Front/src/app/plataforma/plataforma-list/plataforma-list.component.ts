import { Component, OnInit } from '@angular/core';
import { PlataformaDetail } from '../plataforma-detail';
import { PlataformaService } from '../plataforma.service';

@Component({
  selector: 'app-plataforma-list',
  templateUrl: './plataforma-list.component.html',
  styleUrls: ['./plataforma-list.component.css']
})
export class PlataformaListComponent implements OnInit {
  plataformas: Array<PlataformaDetail> = [];
  selectedPlataforma!: PlataformaDetail;
  selected: boolean = false;
  p: number = 1; 
  itemsPerPage: number = 18;
  searchedPlataforma: any;

  constructor(private plataformaService: PlataformaService) { }


  ngOnInit() {
    this.getPlataformas();
  }

  getPlataformas(): void {
    this.plataformaService.getPlataformas().subscribe({
      next: apiData => this.plataformas = apiData,
      error: e => console.error(e)
    });
  }

  getPlataformasAlfabeticamente(): void {
    this.plataformaService.getPlataformasAlfabeticamente().subscribe({
      next: apiData => this.plataformas = apiData,
    });
  }

  getPlataformasAntiAlfabeticamente(): void {
    this.plataformaService.getPlataformasAntiAlfabeticamente().subscribe({
      next: apiData => this.plataformas = apiData,
    });
  }
  onSearchChangePlataforma() {
    this.p = 1;
  }

  onSelected(plataforma: PlataformaDetail): void {
    this.selected = true;
    this.selectedPlataforma = plataforma;
  }
}



