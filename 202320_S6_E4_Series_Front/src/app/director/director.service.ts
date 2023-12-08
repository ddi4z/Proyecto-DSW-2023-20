import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DirectorDetail } from './director-detail';
import { environment } from 'src/environments/environment';
import { Observable, catchError, map, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DirectorService {
  private apiUrl: string = environment.baseUrl + 'participantes';
  constructor(private http: HttpClient) { }

  getDirectores(): Observable<DirectorDetail[]> {
    return this.http.get<DirectorDetail[]>(this.apiUrl).pipe(
      catchError((err) => {
        console.error('Error fetching directores:', err);
        return throwError(() => new Error('Error en el servicio al obtener los directores'));
      }),
      map(directores => directores.filter(director => director.seriesDirigidas && director.seriesDirigidas.length > 0))
    );
  }

  getDirectoresAlfabeticamente(): Observable<DirectorDetail[]> {
    return this.http.get<DirectorDetail[]>(this.apiUrl+'/alfabeticamente').pipe(
      map(directores => directores.filter(director => director.seriesDirigidas && director.seriesDirigidas.length > 0))
    );
  }


  getDirectoresAntiAlfabeticamente(): Observable<DirectorDetail[]> {
    return this.http.get<DirectorDetail[]>(this.apiUrl +'/antiAlfabeticamente').pipe(
      map(directores => directores.filter(director => director.seriesDirigidas && director.seriesDirigidas.length > 0))
    );
  }
  

  getDirector(id: string): Observable<DirectorDetail> {
    return this.http.get<DirectorDetail>(this.apiUrl + "/" + id);
  }

}

