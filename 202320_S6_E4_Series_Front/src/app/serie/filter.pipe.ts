import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterByPlataforma',
})
export class FilterByPlataformaPipe implements PipeTransform {
  transform(series: any[], plataforma: string): any[] {
    if (!plataforma || plataforma.length === 0) {
      return series;
    }
    return series.filter(serie => serie.plataformas.some((plataformaSerie: { nombre: string; }) => plataformaSerie.nombre === plataforma));
  }
}

@Pipe({
  name: 'filterByAnio',
})
export class FilterByAnioPipe implements PipeTransform {
  transform(series: any[], anioMenor: number, anioSuperior: number): any[] {
    if (!anioMenor || !anioSuperior) {
      return series;
    }
    return series.filter(serie => serie.anio >= anioMenor && serie.anio <= anioSuperior);
  }
}


