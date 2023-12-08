import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PlataformaDetail } from './plataforma-detail';
import { environment } from 'src/environments/environment';
import { Observable, catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PlataformaService {
  private apiUrl: string = environment.baseUrl + 'plataformas';

  constructor(private http: HttpClient) {}

  getPlataformas(): Observable<PlataformaDetail[]> {
    return this.http
      .get<PlataformaDetail[]>(this.apiUrl)
      .pipe(
        catchError((err) => {
          console.error('Error fetching plataformas:', err);
          return throwError(() => new Error('Error en el servicio al obtener las plataformas'));
        })
      );
  }

  getPlataformasAlfabeticamente(): Observable<PlataformaDetail[]> {
    return this.http
      .get<PlataformaDetail[]>(this.apiUrl+'/alfabeticamente');
  }

  getPlataformasAntiAlfabeticamente(): Observable<PlataformaDetail[]> {
    return this.http
      .get<PlataformaDetail[]>(this.apiUrl+'/antiAlfabeticamente');
  }


  getPlataforma(id: string): Observable<PlataformaDetail> {
    return this.http.get<PlataformaDetail>(this.apiUrl + '/' + id);
  }

}
