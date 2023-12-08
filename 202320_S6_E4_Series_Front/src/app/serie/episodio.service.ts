import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Episodio } from '../serie/episodio';
import { environment } from 'src/environments/environment';
import { Observable, catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EpisodioService {
  private apiUrl: string = environment.baseUrl + 'serie';

  constructor(private http: HttpClient) {}

  getEpisodios(serieId: string): Observable<Episodio[]> {
    return this.http
      .get<Episodio[]>(this.apiUrl+'/'+serieId+'/episodios')
      .pipe(
        catchError((err) => {
          console.error('Error fetching episodios:', err);
          return throwError(() => new Error('Error en el servicio al obtener los episodios'));
        })
      );
  }

  getEpisodiosAlfabetico(serieId: string): Observable<Episodio[]> {
    return this.http
      .get<Episodio[]>(this.apiUrl+'/'+serieId+'/episodios/alfabeticamente');
  }

  getEpisodiosAntiAlfabetico(serieId: string): Observable<Episodio[]> {
    return this.http
      .get<Episodio[]>(this.apiUrl+'/'+serieId+'/episodios/antiAlfabeticamente');
  }

  getEpisodio(serieId: string, id: string): Observable<Episodio> {
    return this.http.get<Episodio>(this.apiUrl + '/' + serieId + '/episodios/' + id);
  }

}
