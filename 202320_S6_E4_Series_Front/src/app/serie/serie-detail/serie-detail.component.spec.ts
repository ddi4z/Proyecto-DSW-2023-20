import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SerieDetailComponent } from './serie-detail.component';
import { SerieService } from '../serie.service';
import { SerieDetail } from '../serie-detail';
import { NgxPaginationModule } from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientModule } from '@angular/common/http';
import { Episodio } from '../episodio';
import { FormsModule } from '@angular/forms';


describe('SerieDetailComponent', () => {
  let component: SerieDetailComponent;
  let fixture: ComponentFixture<SerieDetailComponent>;
  let mockActivatedRoute: any;
  let mockSerieService: jasmine.SpyObj<SerieService>;


  beforeEach(async(() => {
    mockActivatedRoute = {
      params: of({ id: '1' }) 
    };
    mockSerieService = jasmine.createSpyObj('SerieService', ['getSerie']);

    TestBed.configureTestingModule({
      declarations: [SerieDetailComponent],
      imports:[HttpClientModule, RouterTestingModule, NgxPaginationModule, Ng2SearchPipeModule,FormsModule],
      providers: [
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SerieService, useValue: mockSerieService }
      ]
    
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SerieDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch serie details on ngOnInit', () => {
    const mockSerieDetail = new SerieDetail(1, 1, 'Nombre', 'Resumen', 'Imagen', 'Trailer', [], [], [], [], []);
    mockSerieService.getSerie.and.returnValue(of(mockSerieDetail));

    fixture.detectChanges(); 

    expect(mockSerieService.getSerie).toHaveBeenCalledWith('1'); 
    expect(component.serieDetail).toEqual(mockSerieDetail);
    expect(component.showDetail).toBeTruthy();
    expect(component.episodioSeleccionado).toEqual(mockSerieDetail.episodios[0]);
  });

  it('should set episodioSeleccionado when mostrarDetallesEpisodio is called', () => {

    const episodio = new Episodio(1, 1, 'Nombre', 'Resumen', 2, 'Imagen')


    component.mostrarDetallesEpisodio(episodio);
    expect(component.episodioSeleccionado).toEqual(episodio);
  });
});
