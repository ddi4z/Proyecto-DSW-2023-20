import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { CategoriaDetailComponent } from './categoria-detail.component';
import { CategoriaService } from '../categoria.service';
import { CategoriaDetail } from '../categoria-detail';
import { RouterTestingModule } from '@angular/router/testing';
import { NgxPaginationModule } from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { SerieDetail } from 'src/app/serie/serie-detail';
import { FormsModule } from '@angular/forms';

describe('CategoriaDetailComponent', () => {
  let component: CategoriaDetailComponent;
  let fixture: ComponentFixture<CategoriaDetailComponent>;
  let mockCategoriaService: jasmine.SpyObj<CategoriaService>;
  let mockActivatedRoute: Partial<ActivatedRoute>;


  beforeEach(() => {
    mockCategoriaService = jasmine.createSpyObj('CategoriaService', ['getCategoria','getSeriesCategoriaAlfabeticamente',
    'getSeriesCategoriaAntiAlfabeticamente',
    'getSeriesCategoria',]);
    mockActivatedRoute = { params: of({ id: '1' }) };

    TestBed.configureTestingModule({
      imports: [RouterTestingModule, NgxPaginationModule, Ng2SearchPipeModule,FormsModule],
      declarations: [CategoriaDetailComponent],
      providers: [
        { provide: CategoriaService, useValue: mockCategoriaService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CategoriaDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load series of a category alphabetically', () => {
    const mockSeries = [
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[],[],[],[],[]),
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[],[],[],[],[])
    ];
    mockCategoriaService.getSeriesCategoriaAlfabeticamente.and.returnValue(of(mockSeries));

    component.getSeriesCategoriaAlfabeticamente();

    expect(component.seriesCategoria).toEqual(mockSeries);
  });

  it('should load series of a category in reverse alphabetical order', () => {
    const mockSeries = [
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[],[],[],[],[]),
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[],[],[],[],[])
    ];
    mockCategoriaService.getSeriesCategoriaAntiAlfabeticamente.and.returnValue(of(mockSeries));

    component.getSeriesCategoriaAntiAlfabeticamente();

    expect(component.seriesCategoria).toEqual(mockSeries);
  });

  it('should load series of a category', () => {
    const mockSeries = [
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[],[],[],[],[]),
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[],[],[],[],[])
    ]
    mockCategoriaService.getSeriesCategoria.and.returnValue(of(mockSeries));

    component.getSeriesCategoria();

    expect(component.seriesCategoria).toEqual(mockSeries);
  });

  it('should shorten sinopsis if its length is greater than 90 characters', () => {
    const longSinopsis = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.';
    const result = component.acortarSinopsisSerieCategoria(longSinopsis);
    expect(result.length).toBe(93); 
    expect(result.endsWith('...')).toBe(true);
  });

  it('should convert categories to string', () => {
    const mockCategorias = [
      { nombre: 'Acción' },
      { nombre: 'Aventura' },
      { nombre: 'Anime' }
    ];

    const result = component.categoriasSerieAString(mockCategorias);

    expect(result).toEqual('Acción, Aventura, Anime');
  });

  it('should shorten sinopsis', () => {
    const longSinopsis = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.';

    const result = component.acortarSinopsisSerieCategoria(longSinopsis);

    expect(result.length).toBeLessThanOrEqual(93);
  });

  it('should set selectedSerie on mouse enter', () => {
    const mockSerie = new SerieDetail(1, 1, 'Nombre', 'Resumen', 'Imagen', 'Trailer', [], [], [], [], []);
    component.onMouseEnterCategoria(mockSerie);
    expect(component.selectedSerieCategoria).toEqual(mockSerie);
  });

  it('should reset selectedSerie on mouse leave', () => {
    component.onMouseLeaveCategoria();
    expect(component.selectedSerieCategoria).toBeNull();
  });

  it('should call getCategoria and getSeriesCategoria on ngOnInit', () => {
    const mockCategoriaDetail: CategoriaDetail =new CategoriaDetail(1,"anime","anime.png",[])
    const mockSeries: SerieDetail[] = [
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[mockCategoriaDetail],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[mockCategoriaDetail],[],[],[],[]),
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[mockCategoriaDetail],[],[],[],[])
    ];

    mockCategoriaService.getCategoria.and.returnValue(of(mockCategoriaDetail));
    mockCategoriaService.getSeriesCategoria.and.returnValue(of(mockSeries));

    fixture.detectChanges(); 

    expect(mockCategoriaService.getCategoria).toHaveBeenCalledWith('1'); 
    expect(component.categoriaDetail).toEqual(mockCategoriaDetail);
    expect(component.showDetail).toBeTruthy();

    expect(mockCategoriaService.getSeriesCategoria).toHaveBeenCalledWith('1'); 
    expect(component.seriesCategoria).toEqual(mockSeries);
  });
});
