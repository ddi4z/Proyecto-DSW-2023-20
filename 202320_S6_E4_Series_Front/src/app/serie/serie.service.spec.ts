/* tslint:disable:no-unused-variable */
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed, inject } from '@angular/core/testing';
import { SerieService } from './serie.service';
import { SerieDetail } from './serie-detail';

describe('Service: Serie', () => {
  let service: SerieService;
  let httpMock: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SerieService]
    });
    service = TestBed.inject(SerieService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should ...', inject([SerieService], (service: SerieService) => {
    expect(service).toBeTruthy();
  }));

  it('should get series alfabeticamente', () => {
    const dummySeries = [
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[],[],[],[],[]),
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[],[],[],[],[])
    ]
    
    service.getSeriesAlfabeticamente().subscribe(series => {
      expect(series).toEqual(dummySeries);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/alfabeticamente`);
    expect(req.request.method).toBe('GET');
    req.flush(dummySeries);
  });

  it('should get series anti-alfabeticamente', () => {
    const dummySeries = [
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[],[],[],[],[]),
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[],[],[],[],[])
    ];
    
    service.getSeriesAntiAlfabeticamente().subscribe(series => {
      expect(series).toEqual(dummySeries);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/antiAlfabeticamente`);
    expect(req.request.method).toBe('GET');
    req.flush(dummySeries);
  });

  it('should get a single Serie by ID', inject([SerieService, HttpTestingController], (service: SerieService, httpMock: HttpTestingController) => {
    const dummySerie = new SerieDetail(1, 1, 'Nombre', 'Resumen', 'Imagen', 'Trailer', [], [], [], [], []);
    const serieId = '1';


    service.getSerie(serieId).subscribe(serie => {
      expect(serie).toEqual(dummySerie);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/${serieId}`);
    expect(req.request.method).toBe('GET');


    req.flush(dummySerie);

    httpMock.verify();
  }));
});
