import { Component, OnInit } from '@angular/core';
import { ActorDetail } from '../actor-detail';
import { ActorService } from '../actor.service';

@Component({
  selector: 'app-actor-list',
  templateUrl: './actor-list.component.html',
  styleUrls: ['./actor-list.component.css']
})
export class ActorListComponent implements OnInit {
  actores: Array<ActorDetail> = [];
  selectedActor!: ActorDetail;
  selected: boolean = false;
  p: number = 1; 
  itemsPerPage: number = 18;
  searchedActor: any;

  constructor(private actorService: ActorService) { }

  getActores(){
    this.actorService.getActores().subscribe({
      next: apiData => this.actores = apiData,
      error: e => console.error(e)
    });
  }
  getActoresAlfabeticamente(): void {
    this.actorService.getActoresAlfabeticamente().subscribe({
      next: apiData => this.actores = apiData,

    });
  }

  getActoresAntiAlfabeticamente(): void {
    this.actorService.getActoresAntiAlfabeticamente().subscribe({
      next: apiData => this.actores = apiData,
    });
  }

  onSearchChangeActor() {
    this.p = 1;
  }


  onSelected(actor: ActorDetail){
    this.selected = true;
    this.selectedActor = actor;
  }

  ngOnInit() {
    this.getActores();
  }

}








