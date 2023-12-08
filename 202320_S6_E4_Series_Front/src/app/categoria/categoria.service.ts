import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CategoriaDetail } from './categoria-detail';
import { environment } from 'src/environments/environment';
import { Observable, catchError, throwError } from 'rxjs';
import { SerieDetail } from '../serie/serie-detail';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {
  private apiUrl: string = environment.baseUrl + 'categorias';

  constructor(private http: HttpClient) {}

  getCategorias(): Observable<CategoriaDetail[]> {
    return this.http
      .get<CategoriaDetail[]>(this.apiUrl)
      .pipe(
        catchError((err) => {
          console.error('Error fetching categorias:', err);
          return throwError(() => new Error('Error en el servicio al obtener las categorias'));
        })
      );
  }
  getSeriesCategoria(categoriaId: string): Observable<SerieDetail[]> {
    return this.http
      .get<SerieDetail[]>(this.apiUrl+'/'+categoriaId+'/series');
  }
  getSeriesCategoriaAlfabeticamente(categoriaId: string): Observable<SerieDetail[]> {
    return this.http
      .get<SerieDetail[]>(this.apiUrl+'/'+categoriaId+'/series'+'/alfabeticamente');
  }
  getSeriesCategoriaAntiAlfabeticamente(categoriaId: string): Observable<SerieDetail[]> {
    return this.http.get<SerieDetail[]>(this.apiUrl+'/'+categoriaId+'/series'+'/antiAlfabeticamente')
  }


  getCategoria(id: string): Observable<CategoriaDetail> {
    return this.http.get<CategoriaDetail>(this.apiUrl + '/' + id);
  }

}
