import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable, catchError, throwError } from 'rxjs';
import { Plan } from './plan';

@Injectable({
  providedIn: 'root'
})
export class PlanService {
  private apiUrl: string = environment.baseUrl + 'plataforma';

  constructor(private http: HttpClient) {}

  //Obtener todos los planes
  getPlanes(plataformaId: string): Observable<Plan[]> {
    return this.http.get<Plan[]>(this.apiUrl+'/'+plataformaId+'/planes').pipe(
        catchError((err) => {
          console.error('Error fetching planes:', err);
          return throwError(() => new Error('Error en el servicio al obtener los planes'));
        })
      );
  }

  //Obtener un plan
  getPlan(plataformaId: string, planId: string): Observable<Plan>{
    return this.http.get<Plan>(this.apiUrl+'/'+plataformaId+'/planes/'+planId);
  }

  //Obtener todos los planes en precio creciente
  getPlanesPrecioCreciente(plataformaId: string): Observable<Plan[]>{
    return this.http.get<Plan[]>(this.apiUrl+'/'+plataformaId+'/planes/precioCreciente');
  }

  //Obtener todos los planes en precio decreciente
  getPlanesPrecioDecreciente(plataformaId: string): Observable<Plan[]>{
    return this.http.get<Plan[]>(this.apiUrl+'/'+plataformaId+'/planes/precioDecreciente');
  }

  //Obtener todos los planes en puntaje creciente
  getPlanesPuntajeCreciente(plataformaId: string): Observable<Plan[]>{
    return this.http.get<Plan[]>(this.apiUrl+'/'+plataformaId+'/planes/puntajeCreciente');
  }

  //Obtener todos los planes en puntaje decreciente
  getPlanesPuntajeDecreciente(plataformaId: string): Observable<Plan[]>{
    return this.http.get<Plan[]>(this.apiUrl+'/'+plataformaId+'/planes/puntajeDecreciente');
  }
}
  
