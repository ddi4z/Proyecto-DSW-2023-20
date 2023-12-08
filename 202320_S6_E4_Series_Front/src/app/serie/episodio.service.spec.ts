import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { EpisodioService } from './episodio.service';
import { environment } from 'src/environments/environment';
import { Episodio } from './episodio';

describe('EpisodioService', () => {
  let service: EpisodioService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [EpisodioService]
    });

    service = TestBed.inject(EpisodioService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve episodios', () => {
    const serieId = '1';
    const mockEpisodios = [
      new Episodio(1,1, 'seiya','empieza la aventura de seiya',24,'seiya.png'),
      new Episodio(2,1, 'shiryu','empieza la aventura de shiryu',24,'shiryu.png'),
      new Episodio(3,1, 'hyoga','empieza la aventura de hyoga',24,'hyoga.png'),
      new Episodio(4,1, 'shun','empieza la aventura de shun',24,'shun.png'),
      new Episodio(5,1, 'ikki','empieza la aventura de ikki',24,'ikki.png')
    ]

    service.getEpisodios(serieId).subscribe(episodios => {
      expect(episodios).toEqual(mockEpisodios);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}serie/${serieId}/episodios`);
    expect(req.request.method).toBe('GET');
    req.flush(mockEpisodios);
  });

  it('should retrieve episodios alfabeticamente', () => {
    const serieId = '1';
    const mockEpisodios = [
      new Episodio(1,1, 'seiya','empieza la aventura de seiya',24,'seiya.png'),
      new Episodio(2,1, 'shiryu','empieza la aventura de shiryu',24,'shiryu.png'),
      new Episodio(3,1, 'hyoga','empieza la aventura de hyoga',24,'hyoga.png'),
      new Episodio(4,1, 'shun','empieza la aventura de shun',24,'shun.png'),
      new Episodio(5,1, 'ikki','empieza la aventura de ikki',24,'ikki.png')
    ]

    service.getEpisodiosAlfabetico(serieId).subscribe(episodios => {
      expect(episodios).toEqual(mockEpisodios);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}serie/${serieId}/episodios/alfabeticamente`);
    expect(req.request.method).toBe('GET');
    req.flush(mockEpisodios);
  });

  it('should retrieve episodios anti-alfabeticamente', () => {
    const serieId = '1';
    const mockEpisodios = [
      new Episodio(1,1, 'seiya','empieza la aventura de seiya',24,'seiya.png'),
      new Episodio(2,1, 'shiryu','empieza la aventura de shiryu',24,'shiryu.png'),
      new Episodio(3,1, 'hyoga','empieza la aventura de hyoga',24,'hyoga.png'),
      new Episodio(4,1, 'shun','empieza la aventura de shun',24,'shun.png'),
      new Episodio(5,1, 'ikki','empieza la aventura de ikki',24,'ikki.png')
    ]

    service.getEpisodiosAntiAlfabetico(serieId).subscribe(episodios => {
      expect(episodios).toEqual(mockEpisodios);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}serie/${serieId}/episodios/antiAlfabeticamente`);
    expect(req.request.method).toBe('GET');
    req.flush(mockEpisodios);
  });

  it('should retrieve a single episodio', () => {
    const serieId = '1';
    const episodioId = '2';
    const mockEpisodio = new Episodio(1,1, 'seiya','empieza la aventura de seiya',24,'seiya.png');

    service.getEpisodio(serieId, episodioId).subscribe(episodio => {
      expect(episodio).toEqual(mockEpisodio);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}serie/${serieId}/episodios/${episodioId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockEpisodio);
  });
});
