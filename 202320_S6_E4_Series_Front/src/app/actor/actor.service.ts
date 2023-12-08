import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActorDetail } from './actor-detail';
import { environment } from 'src/environments/environment';
import { Observable, catchError, map, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ActorService {
  private apiUrl: string = environment.baseUrl + 'participantes';
  constructor(private http: HttpClient) { }

  getActores(): Observable<ActorDetail[]> {
    return this.http.get<ActorDetail[]>(this.apiUrl).pipe(
      catchError((err) => {
        console.error('Error fetching actores:', err);
        return throwError(() => new Error('Error en el servicio al obtener los actores'));
      }),
      map(actores => actores.filter(actor => actor.seriesActuadas && actor.seriesActuadas.length > 0))
    );
  }

  getActoresAlfabeticamente(): Observable<ActorDetail[]> {
    return this.http.get<ActorDetail[]>(this.apiUrl+'/alfabeticamente').pipe(
      map(actores => actores.filter(actor => actor.seriesActuadas && actor.seriesActuadas.length > 0))
    );
  }


  getActoresAntiAlfabeticamente(): Observable<ActorDetail[]> {
    return this.http.get<ActorDetail[]>(this.apiUrl+'/antiAlfabeticamente').pipe(
      map(actores => actores.filter(actor => actor.seriesActuadas && actor.seriesActuadas.length > 0))
    );
  }



  getActor(id: string): Observable<ActorDetail> {
    return this.http.get<ActorDetail>(this.apiUrl + "/" + id);
  }

}


