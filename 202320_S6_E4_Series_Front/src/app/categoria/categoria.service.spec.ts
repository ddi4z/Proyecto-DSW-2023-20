import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CategoriaService } from './categoria.service';
import { environment } from 'src/environments/environment';
import { CategoriaDetail } from './categoria-detail';
import { SerieDetail } from '../serie/serie-detail';

describe('CategoriaService', () => {
  let service: CategoriaService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CategoriaService]
    });
    service = TestBed.inject(CategoriaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve categories from the API via GET', () => {
    const dummyCategories = [
      new CategoriaDetail(1, 'Categoria1', 'Imagen1', []),
      new CategoriaDetail(2, 'Categoria2', 'Imagen2', [])
    ]

    service.getCategorias().subscribe(categories => {
      expect(categories.length).toBe(dummyCategories.length);
      expect(categories).toEqual(dummyCategories);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}categorias`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyCategories);
  });

  it('should retrieve a category by id from the API via GET', () => {
    const dummyCategory = new CategoriaDetail( 1, 'Nombre', 'Imagen', []);
    const dummyId = '1';

    service.getCategoria(dummyId).subscribe(category => {
      expect(category).toEqual(dummyCategory);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}categorias/${dummyId}`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyCategory);
  });

  it('should handle errors when fetching categories', () => {
    const errorMessage = 'Error fetching categories';

    service.getCategorias().subscribe(
      () => fail('Expected an error, but received categories'),
      error => {
        expect(error.message).toBe('Error en el servicio al obtener las categorias');
        expect(error).toBeTruthy();
      }
    );

    const req = httpMock.expectOne(`${environment.baseUrl}categorias`);
    req.error(new ErrorEvent('error', { error: new Error(errorMessage) }));
  });

  it('should get series for a categoria', () => {
    const categoriaId = '1';
    const dummySeries = [
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[],[],[],[],[]),
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[],[],[],[],[])
    ];

    service.getSeriesCategoria(categoriaId).subscribe(series => {
      expect(series).toEqual(dummySeries);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/${categoriaId}/series`);
    expect(req.request.method).toBe('GET');
    req.flush(dummySeries);
  });

  it('should get series for a categoria alfabeticamente', () => {
    const categoriaId = '1';
    const dummySeries = [
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[],[],[],[],[]),
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[],[],[],[],[])
    ];

    service.getSeriesCategoriaAlfabeticamente(categoriaId).subscribe(series => {
      expect(series).toEqual(dummySeries);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/${categoriaId}/series/alfabeticamente`);
    expect(req.request.method).toBe('GET');
    req.flush(dummySeries);
  });

  it('should get series for a categoria anti-alfabeticamente', () => {
    const categoriaId = '1';
    const dummySeries = [
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[],[],[],[],[]),
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[],[],[],[],[])
    ];

    service.getSeriesCategoriaAntiAlfabeticamente(categoriaId).subscribe(series => {
      expect(series).toEqual(dummySeries);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/${categoriaId}/series/antiAlfabeticamente`);
    expect(req.request.method).toBe('GET');
    req.flush(dummySeries);
  });

});
