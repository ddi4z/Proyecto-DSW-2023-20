import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { PlataformaDetailComponent } from './plataforma-detail.component';
import { PlataformaService } from '../plataforma.service';
import { PlataformaDetail } from '../plataforma-detail';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { NgxPaginationModule } from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FormsModule } from '@angular/forms';

describe('PlataformaDetailComponent', () => {
  let component: PlataformaDetailComponent;
  let fixture: ComponentFixture<PlataformaDetailComponent>;
  let mockPlataformaService: jasmine.SpyObj<PlataformaService>;
  let mockActivatedRoute: Partial<ActivatedRoute>;

  beforeEach(() => {
    mockPlataformaService = jasmine.createSpyObj('PlataformaService', ['getPlataforma']);
    mockActivatedRoute = { params: of({ id: '1' }) };

    TestBed.configureTestingModule({
      declarations: [PlataformaDetailComponent],
      imports:[HttpClientModule, RouterTestingModule, NgxPaginationModule, Ng2SearchPipeModule,FormsModule],
      providers: [
        { provide: PlataformaService, useValue: mockPlataformaService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PlataformaDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch plataforma details on init', () => {
    const mockPlataformaDetail = new PlataformaDetail(1, 'Nombre',"sitio", 'Imagen', [],[]);
    mockPlataformaService.getPlataforma.and.returnValue(of(mockPlataformaDetail));

    fixture.detectChanges(); 

    expect(mockPlataformaService.getPlataforma).toHaveBeenCalledWith('1');
    expect(component.plataformaDetail).toEqual(mockPlataformaDetail);
    expect(component.showDetail).toBe(true);
  });
});
