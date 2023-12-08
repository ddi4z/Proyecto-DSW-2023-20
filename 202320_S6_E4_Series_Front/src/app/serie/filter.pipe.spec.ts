import { TestBed } from "@angular/core/testing";
import { FilterByAnioPipe, FilterByPlataformaPipe } from "./filter.pipe";
import { SerieDetail } from "./serie-detail";

describe('FilterByPlataformaPipe', () => {
  let pipe: FilterByPlataformaPipe;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FilterByPlataformaPipe],
    });

    pipe = TestBed.inject(FilterByPlataformaPipe);
  });

  it('should filter series by plataforma', () => {
    const series = [
      { nombre: 'Serie1', plataformas: [{ nombre: 'Plataforma1' }] },
      { nombre: 'Serie2', plataformas: [{ nombre: 'Plataforma2' }] },

    ];

    const plataforma = 'Plataforma1';

    const result = pipe.transform(series, plataforma);

    expect(result).toEqual([{ nombre: 'Serie1', plataformas: [{ nombre: 'Plataforma1' }] }]);
  });

  it('should return all series if no plataforma is provided', () => {
    const series = [
      { nombre: 'Serie1', plataformas: [{ nombre: 'Plataforma1' }] },
      { nombre: 'Serie2', plataformas: [{ nombre: 'Plataforma2' }] },
    ];

    const result = pipe.transform(series, '');

    expect(result).toEqual(series);
  });
});

describe('FilterByAnioPipe', () => {
  let pipe: FilterByAnioPipe;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FilterByAnioPipe],
    });

    pipe = TestBed.inject(FilterByAnioPipe);
  });

  it('should filter series by anio range', () => {
    const series = [
      { nombre: 'Serie1', anio: 2020 },
      { nombre: 'Serie2', anio: 2021 },
    ];

    const anioMenor = 2019;
    const anioSuperior = 2021;

    const result = pipe.transform(series, anioMenor, anioSuperior);

    expect(result).toEqual([
      { nombre: 'Serie1', anio: 2020 },
      { nombre: 'Serie2', anio: 2021 },
    ]);
  });

  it('should return all series if no anio range is provided', () => {
    const series = [
      { nombre: 'Serie1', anio: 2020 },
      { nombre: 'Serie2', anio: 2021 },
     
    ];

    const result = pipe.transform(series, 0, 3000);

    expect(result).toEqual(series);
  });


});




