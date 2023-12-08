import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DirectorService } from './director.service';
import { DirectorDetail } from './director-detail';
import { Serie } from '../serie/serie';
import { SerieDetail } from '../serie/serie-detail';

describe('DirectorService', () => {
  let service: DirectorService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [DirectorService]
    });

    service = TestBed.inject(DirectorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get a list of directors', () => {
    const mockDirectores = [
      new DirectorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[
        new SerieDetail(2, 2, 'Nombre', 'Imagen', 'Descripcion', 'Fecha', [], [], [], [], [])
      ]),
      new DirectorDetail(2, 'Nombre2', 'Biografia2', 'Imagen2', [],[
        new SerieDetail(1, 1, 'Nombre', 'Imagen', 'Descripcion', 'Fecha', [], [], [], [], [])
      ])
    ]

    service.getDirectores().subscribe(directores => {
      expect(directores).toEqual(mockDirectores);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockDirectores);
  });

  it('should get a director by id', () => {
    const mockDirector = new DirectorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[]);
    const directorId = '1';

    service.getDirector(directorId).subscribe(director => {
      expect(director).toEqual(mockDirector);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/${directorId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockDirector);
  });


  it('should get actores alfabeticamente', () => {
    const series = new SerieDetail(1, 1, 'Nombre', 'Imagen', 'Descripcion', 'Fecha', [], [], [], [], []);
    const dummyDirectores = [
      new DirectorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[series]),
      new DirectorDetail(2, 'Nombre2', 'Biografia2', 'Imagen2', [],[series])
    ]
    
    service.getDirectoresAlfabeticamente().subscribe(series => {
      expect(series).toEqual(dummyDirectores);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/alfabeticamente`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyDirectores);
  });

  it('should get actores anti-alfabeticamente', () => {
    const series = new SerieDetail(1, 1, 'Nombre', 'Imagen', 'Descripcion', 'Fecha', [], [], [], [], []);
    const dummyDirectores = [
      new DirectorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[series]),
      new DirectorDetail(2, 'Nombre2', 'Biografia2', 'Imagen2', [],[series])
    ];
    
    service.getDirectoresAntiAlfabeticamente().subscribe(series => {
      expect(series).toEqual(dummyDirectores);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/antiAlfabeticamente`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyDirectores);
  });

});
