/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { faker } from '@faker-js/faker';
import { fakeAsync, tick } from '@angular/core/testing';
import { SerieListComponent } from './serie-list.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { SerieDetail } from '../serie-detail';
import { SerieService } from '../serie.service';
import { NgxPaginationModule } from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FilterByAnioPipe, FilterByPlataformaPipe } from '../filter.pipe';
import { Episodio } from '../episodio';
import { PlataformaService } from 'src/app/plataforma/plataforma.service';
import { of } from 'rxjs';
import { PlataformaDetail } from 'src/app/plataforma/plataforma-detail';
import { FormsModule } from '@angular/forms';


describe('SerieListComponent', () => {
  let component: SerieListComponent;
  let fixture: ComponentFixture<SerieListComponent>;
  let debug: DebugElement;
  let debugSerieDetail: SerieDetail;
  let serieService: SerieService;
  let plataformaService: PlataformaService;
  
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientModule, RouterTestingModule, NgxPaginationModule, Ng2SearchPipeModule,FormsModule],
      declarations: [ SerieListComponent, FilterByPlataformaPipe, FilterByAnioPipe],
      providers: [ SerieService ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SerieListComponent);
    component = fixture.componentInstance;
    serieService = TestBed.inject(SerieService);
    plataformaService = TestBed.inject(PlataformaService);

    let testSeries: Array<SerieDetail> = [];

    for(let i = 0; i<18; i++) {
      testSeries[i] = new SerieDetail(
        faker.number.int(),faker.number.int(), faker.lorem.sentence(), faker.lorem.sentence(), faker.lorem.sentence(),faker.lorem.sentence(), [], [], [], [], []
      );
    }
    
    component.series = testSeries;
    fixture.detectChanges();
    debug = fixture.debugElement;
  });



  it('should create', () => {
    expect(component).toBeTruthy();
  });



  it('should convert categories to string', () => {
    const mockCategorias = [
      { nombre: 'Acción' },
      { nombre: 'Aventura' },
      { nombre: 'Anime' }
    ];

    const result = component.categoriasAString(mockCategorias);

    expect(result).toEqual('Acción, Aventura, Anime');
  });

  it('should shorten sinopsis', () => {
    const longSinopsis = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.';

    const result = component.acortarSinopsis(longSinopsis);

    expect(result.length).toBeLessThanOrEqual(93);
  });

  it('should set selectedSerie on mouse enter', () => {
    const mockSerie = new SerieDetail(1, 1, 'Nombre', 'Resumen', 'Imagen', 'Trailer', [], [], [], [], []);
    component.onMouseEnter(mockSerie);
    expect(component.selectedSerie).toEqual(mockSerie);
  });

  it('should reset selectedSerie on mouse leave', () => {
    component.onMouseLeave();
    expect(component.selectedSerie).toBeNull();
  });

  it('should set searchedPlataforma on onChangePlataforma', () => {
    const plataforma = 'Netflix';
    component.onChangePlataforma(plataforma);
    expect(component.searchedPlataforma).toEqual(plataforma);
  });

  it('should set anioMenor and anioSuperior on onChangeAnio', () => {
    const anio1 = 2000;
    const anio2 = 2020;
    component.onChangeAnio(anio1, anio2);
    expect(component.anioMenor).toEqual(anio1);
    expect(component.anioSuperior).toEqual(anio2);
  });

  it('should set selectedSerie and selected on onSelected', () => {
    const serie = new SerieDetail(1, 1, 'Nombre', 'Resumen', 'Imagen', 'Trailer', [], [], [], [], []);
    component.onSelected(serie);
    expect(component.selectedSerie).toEqual(serie);
    expect(component.selected).toBeTruthy();
  });it('should shorten sinopsis if its length is greater than 90 characters', () => {
    const longSinopsis = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.';
    const result = component.acortarSinopsis(longSinopsis);
    expect(result.length).toBe(93); 
    expect(result.endsWith('...')).toBe(true);
  });
  
  it('should not change sinopsis if its length is 200 characters or less', () => {
    const shortSinopsis = 'Short sinopsis'; 
    const result = component.acortarSinopsis(shortSinopsis);
    expect(result).toEqual(shortSinopsis);
  });

  it('should load carrousel with series', fakeAsync(() => {
    const mockSeries = [];
    for(let i = 1; i<=100; i++) {
      mockSeries[i] = new SerieDetail(i, i, 'Nombre'+'i', 'Resumen'+'i', 'Imagen'+'i', 'Trailer'+'i', [], [], [], [], [])
    }
  
    spyOn(serieService, 'getSeries').and.returnValue(of(mockSeries));

    component.cargarCarrousel();

    tick();


    expect(component.serieCarrousel1).not.toEqual(component.serieCarrousel2);
    expect(component.serieCarrousel2).not.toEqual(component.serieCarrousel3);
    expect(component.serieCarrousel3).not.toEqual(component.serieCarrousel1);

    const serieCarrousel1 = component.serieCarrousel1 ?? new SerieDetail(0, 0, '', '', '', '', [], [], [], [], []);
    const serieCarrousel2 = component.serieCarrousel2 ?? new SerieDetail(0, 0, '', '', '', '', [], [], [], [], []);
    const serieCarrousel3 = component.serieCarrousel3 ?? new SerieDetail(0, 0, '', '', '', '', [], [], [], [], []);

    expect(mockSeries).toContain(serieCarrousel1);
    expect(mockSeries).toContain(serieCarrousel2);
    expect(mockSeries).toContain(serieCarrousel3)
  }));

  it('should fetch series and assign them to component', fakeAsync(() => {
    const mockSeries = [
      new SerieDetail(1, 1, 'Nombre1', 'Resumen1', 'Imagen1', 'Trailer1', [], [], [], [], []),
      new SerieDetail(2, 2, 'Nombre2', 'Resumen2', 'Imagen2', 'Trailer2', [], [], [], [], [])
    ];
  
    spyOn(serieService, 'getSeries').and.returnValue(of(mockSeries));
  
    component.getSeries();
  
    tick(); 
  
    expect(component.series).toEqual(mockSeries);
  }));

  it('should fetch platforms and assign them to component', fakeAsync(() => {
    const mockPlatforms = [
      new PlataformaDetail(1, 'Plataforma1', 'sitio', 'logo',[],[]),
      new PlataformaDetail(2, 'Plataforma2', 'sitio', 'logo',[],[])
    ];
  
    spyOn(plataformaService, 'getPlataformas').and.returnValue(of(mockPlatforms));
  
    component.getPlataformas();
  
    tick();
  
    expect(component.plataformas).toEqual(mockPlatforms);
  }));

  it('should load series alphabetically', () => {
    const mockSeries = [
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[],[],[],[],[]),
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[],[],[],[],[])

    ]
    
    spyOn(serieService, 'getSeriesAlfabeticamente').and.returnValue(of(mockSeries));

    component.getSeriesAlfabeticamente();

    expect(component.series).toEqual(mockSeries);
  });

  it('should load series in reverse alphabetical order', () => {
    const mockSeries = [
      new SerieDetail(3,2003,'c','c.png','c es una letra', 'cWallpaper-png',[],[],[],[],[]),
      new SerieDetail(2,2002,'b','b.png','b es una letra', 'bWallpaper-png',[],[],[],[],[]),
      new SerieDetail(1,2001,'a','a.png','a es una letra', 'aWallpaper-png',[],[],[],[],[])

    ]
    spyOn(serieService, 'getSeriesAntiAlfabeticamente').and.returnValue(of(mockSeries));

    component.getSeriesAntiAlfabeticamente();

    expect(component.series).toEqual(mockSeries);
  });
});

describe('Episodio', () => {
  it('should create an instance', () => {
    const episodio = new Episodio(1, 2, 'Nombre', 'Resumen', 30, 'imagen.jpg');
    expect(episodio).toBeTruthy();
  });

  it('should set properties correctly through the constructor', () => {
    const episodio = new Episodio(1, 2, 'Nombre', 'Resumen', 30, 'imagen.jpg');
    expect(episodio.id).toEqual(1);
    expect(episodio.numeroEpisodio).toEqual(2);
    expect(episodio.nombre).toEqual('Nombre');
    expect(episodio.resumen).toEqual('Resumen');
    expect(episodio.duracionMinutos).toEqual(30);
    expect(episodio.imagen).toEqual('imagen.jpg');
  });
});







