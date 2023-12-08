import { Component, OnInit } from '@angular/core';
import { DirectorDetail } from '../director-detail';
import { DirectorService } from '../director.service';

@Component({
  selector: 'app-director-list',
  templateUrl: './director-list.component.html',
  styleUrls: ['./director-list.component.css']
})
export class DirectorListComponent implements OnInit {
  directores: Array<DirectorDetail> = [];
  selectedDirector!: DirectorDetail;
  selected: boolean = false;
  p: number = 1; 
  itemsPerPage: number = 18;
  searchedDirector: any;

  constructor(private directorService: DirectorService) { }

  getDirectores(){
    this.directorService.getDirectores().subscribe({
      next: apiData => this.directores = apiData,
      error: e => console.error(e)
    });
  }

  getDirectoresAlfabeticamente(): void {
    this.directorService.getDirectoresAlfabeticamente().subscribe({
      next: apiData => this.directores = apiData,
    });
  }

  getDirectoresAntiAlfabeticamente(): void {
    this.directorService.getDirectoresAntiAlfabeticamente().subscribe({
      next: apiData => this.directores = apiData,
    });
  }

  onSelected(director: DirectorDetail){
    this.selected = true;
    this.selectedDirector = director;
  }

  onSearchChangeDirector() {
    this.p = 1;
  }

  ngOnInit() {
    this.getDirectores();
  }

}








