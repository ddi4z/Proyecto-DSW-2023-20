import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SerieDetail } from './serie-detail';
import { environment } from 'src/environments/environment';
import { Observable, catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SerieService {
  private apiUrl: string = environment.baseUrl + 'series';

  constructor(private http: HttpClient) {}

  getSeries(): Observable<SerieDetail[]> {
    return this.http
      .get<SerieDetail[]>(this.apiUrl)
      .pipe(
        catchError((err) => {
          console.error('Error fetching series:', err);
          return throwError(() => new Error('Error en el servicio al obtener las series'));
        })
      );
  }
  getSeriesAlfabeticamente(): Observable<SerieDetail[]> {
    return this.http
      .get<SerieDetail[]>(this.apiUrl+'/alfabeticamente');
  }
  getSeriesAntiAlfabeticamente(): Observable<SerieDetail[]> {
    return this.http
      .get<SerieDetail[]>(this.apiUrl+'/antiAlfabeticamente');
  }

  getSerie(id: string): Observable<SerieDetail> {
    return this.http.get<SerieDetail>(this.apiUrl + '/' + id);
  }

}
