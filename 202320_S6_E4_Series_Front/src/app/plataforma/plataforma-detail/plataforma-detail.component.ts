import { Component, OnInit } from '@angular/core';
import { PlataformaService } from '../plataforma.service';
import { ActivatedRoute } from '@angular/router';
import { PlataformaDetail } from '../plataforma-detail';
import { PlanService } from '../plan.service';
import { Plan } from '../plan';


@Component({
  selector: 'app-plataforma-detail',
  templateUrl: './plataforma-detail.component.html',
  styleUrls: ['./plataforma-detail.component.css']
})
export class PlataformaDetailComponent implements OnInit {
  plataformaId!: string;
  planes: Array<Plan> = [];
  plataformaDetail: PlataformaDetail | null = null; 
  showDetail: boolean = false; 
  searchedPlan: any;

  constructor(
    private route: ActivatedRoute,
    private plataformaService: PlataformaService,
    private planService: PlanService
  ) { }

  //Obtener todos los planes
  getPlanes(): void {
    this.planService.getPlanes(this.plataformaId).subscribe({
      next: apiData => this.planes = apiData,
      error: e => console.error(e)
    });
  }

  //Obtener todos los planes en precio creciente
  getPlanesPrecioCreciente(): void{
    this.planService.getPlanesPrecioCreciente(this.plataformaId).subscribe({
      next: apiData => this.planes = apiData,
    });
  }

  //Obtener todos los planes en precio decreciente
  getPlanesPrecioDecreciente(): void {
    this.planService.getPlanesPrecioDecreciente(this.plataformaId).subscribe({
      next: apiData => this.planes = apiData,
    });
  }

  //Obtener todos los planes en puntaje creciente
  getPlanesPuntajeCreciente(): void{
    this.planService.getPlanesPuntajeCreciente(this.plataformaId).subscribe({
      next: apiData => this.planes = apiData,
    });
  }

  //Obtener todos los planes en puntaje decreciente
  getPlanesPuntajeDecreciente(): void{
    this.planService.getPlanesPuntajeDecreciente(this.plataformaId).subscribe({
      next: apiData => this.planes = apiData,
    });
  }


  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.plataformaId = params['id'];
      if (this.plataformaId) {
        this.plataformaService.getPlataforma(this.plataformaId).subscribe(
          (apiData) => {
            this.plataformaDetail = apiData;
            this.showDetail = true; 
          },

        );
      }
    });
    this.getPlanes();
  }
}
