import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ActorService } from './actor.service';
import { ActorDetail } from './actor-detail';
import { Serie } from '../serie/serie';
import { SerieDetail } from '../serie/serie-detail';

describe('ActorService', () => {
  let service: ActorService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ActorService]
    });

    service = TestBed.inject(ActorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get a list of actors', () => {
    const mockActores: ActorDetail[] = [
      new ActorDetail(1, 'Nombre', 'Biografia', 'Imagen', [
        new SerieDetail(2, 2, 'Nombre', 'Imagen', 'Descripcion', 'Fecha', [], [], [], [], [])
      ],[]),
      new ActorDetail(2, 'Nombre2', 'Biografia2', 'Imagen2', [
        new SerieDetail(1, 1, 'Nombre', 'Imagen', 'Descripcion', 'Fecha', [], [], [], [], [])
      ],[])
    ];

    service.getActores().subscribe(actores => {
      expect(actores).toEqual(mockActores);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockActores);
  });

  it('should get an actor by id', () => {
    const mockActor: ActorDetail = new ActorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[]);
    const actorId = '1';

    service.getActor(actorId).subscribe(actor => {
      expect(actor).toEqual(mockActor);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/${actorId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockActor);
  });


  it('should get actores alfabeticamente', () => {
    const series = new SerieDetail(1, 1, 'Nombre', 'Imagen', 'Descripcion', 'Fecha', [], [], [], [], []);
    const dummyActores = [
      new ActorDetail(1, 'Nombre', 'Biografia', 'Imagen', [series],[]),
      new ActorDetail(2, 'Nombre2', 'Biografia2', 'Imagen2', [series],[])
    ]
    
    service.getActoresAlfabeticamente().subscribe(series => {
      expect(series).toEqual(dummyActores);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/alfabeticamente`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyActores);
  });

  it('should get actores anti-alfabeticamente', () => {
    const series = new SerieDetail(1, 1, 'Nombre', 'Imagen', 'Descripcion', 'Fecha', [], [], [], [], []);
    const dummyActores = [
      new ActorDetail(1, 'Nombre', 'Biografia', 'Imagen', [series],[]),
      new ActorDetail(2, 'Nombre2', 'Biografia2', 'Imagen2', [series],[])
    ];
    
    service.getActoresAntiAlfabeticamente().subscribe(series => {
      expect(series).toEqual(dummyActores);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/antiAlfabeticamente`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyActores);
  });

});