import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PlataformaService } from './plataforma.service';
import { environment } from 'src/environments/environment';
import { PlataformaDetail } from './plataforma-detail';

describe('PlataformaService', () => {
  let service: PlataformaService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PlataformaService]
    });
    service = TestBed.inject(PlataformaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve platforms from the API via GET', () => {
    const dummyPlatforms = [
      new PlataformaDetail(1, 'Plataforma1', 'sitio', 'logo',[],[]),
      new PlataformaDetail(2, 'Plataforma2', 'sitio', 'logo',[],[])
      
    ];

    service.getPlataformas().subscribe(platforms => {
      expect(platforms.length).toBe(dummyPlatforms.length);
      expect(platforms).toEqual(dummyPlatforms);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}plataformas`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyPlatforms);
  });

  it('should retrieve a platform by id from the API via GET', () => {
    const dummyPlatform = new PlataformaDetail(1, 'Plataforma1', 'sitio', 'logo',[],[]);

    const dummyId = '1';

    service.getPlataforma(dummyId).subscribe(platform => {
      expect(platform).toEqual(dummyPlatform);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}plataformas/${dummyId}`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyPlatform);
  });
  it('should get plataformas alfabeticamente', () => {
    const mockPlataformas  = [
      new PlataformaDetail(1, 'Plataforma1', 'sitio', 'logo',[],[]),
      new PlataformaDetail(2, 'Plataforma2', 'sitio', 'logo',[],[])
    ];
    
    service.getPlataformasAlfabeticamente().subscribe(plataformas => {
      expect(plataformas).toEqual(mockPlataformas);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/alfabeticamente`);
    expect(req.request.method).toEqual('GET');
    req.flush(mockPlataformas);
  });

  it('should get plataformas anti-alfabeticamente', () => {
    const mockPlataformas  = [
      new PlataformaDetail(1, 'Plataforma1', 'sitio', 'logo',[],[]),
      new PlataformaDetail(2, 'Plataforma2', 'sitio', 'logo',[],[])
    ];
    
    service.getPlataformasAntiAlfabeticamente().subscribe(plataformas => {
      expect(plataformas).toEqual(mockPlataformas);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/antiAlfabeticamente`);
    expect(req.request.method).toEqual('GET');
    req.flush(mockPlataformas);
  });
});
