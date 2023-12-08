import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PlanService } from './plan.service';
import { environment } from 'src/environments/environment';
import { Plan } from './plan';

describe('PlanService', () => {
  let service: PlanService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PlanService]
    });

    service = TestBed.inject(PlanService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve plans', () => {
    const plataformaId = '1';
    const mockPlans = [
      new Plan(1, 'Nombre',2500, 5),
      new Plan(2, 'Nombre2',1500, 10),
      new Plan(3, 'Nombre3',500, 7)
    ]

    service.getPlanes(plataformaId).subscribe(plans => {
      expect(plans).toEqual(mockPlans);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}plataforma/${plataformaId}/planes`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPlans);
  });

  it('should retrieve plans with decreasing price', () => {
    const plataformaId = '1';
    const mockPlans = [
      new Plan(1, 'Nombre',2500, 5),
      new Plan(2, 'Nombre2',1500, 10),
      new Plan(3, 'Nombre3',500, 7)
    ]

    service.getPlanesPrecioDecreciente(plataformaId).subscribe(plans => {
      expect(plans).toEqual(mockPlans);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}plataforma/${plataformaId}/planes/precioDecreciente`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPlans);
  });

  it('should retrieve a single plan', () => {
    const plataformaId = '1';
    const planId = '2';
    const mockPlan = new Plan(1, 'Nombre',2500, 5);
    service.getPlan(plataformaId, planId).subscribe(plan => {
      expect(plan).toEqual(mockPlan);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}plataforma/${plataformaId}/planes/${planId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPlan);
  });
});
