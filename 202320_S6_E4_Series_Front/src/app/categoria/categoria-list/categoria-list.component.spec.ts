import { HttpClientModule } from '@angular/common/http'; 
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CategoriaListComponent } from './categoria-list.component';
import { CategoriaService } from '../categoria.service';
import { CategoriaDetail } from '../categoria-detail';
import { DebugElement } from '@angular/core';
import { faker } from '@faker-js/faker';
import { SerieDetail } from 'src/app/serie/serie-detail';
import { of } from 'rxjs';
import { fakeAsync, tick } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { NgxPaginationModule } from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FormsModule } from '@angular/forms';

describe('CategoriaListComponent', () => {
  let component: CategoriaListComponent;
  let fixture: ComponentFixture<CategoriaListComponent>;
  let debug: DebugElement;
  let categoriaService: CategoriaService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule, RouterTestingModule, NgxPaginationModule, Ng2SearchPipeModule,FormsModule], 
      declarations: [CategoriaListComponent],
      providers: [CategoriaService], 
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CategoriaListComponent);
    component = fixture.componentInstance;
    categoriaService = TestBed.inject(CategoriaService);

    let testCategorias: Array<CategoriaDetail> = [];
    let testSeries: Array<SerieDetail> = [];

    for(let i = 0; i<2; i++) {
      testSeries[i] = new SerieDetail(
        faker.number.int(),
        faker.number.int(),
        faker.lorem.sentence(),
        faker.lorem.sentence(),
        faker.lorem.sentence(),
        faker.lorem.sentence(),
        [],
        [],
        [],
        [],
        []
      );
    }

    for(let i = 0; i<10; i++) {
      testCategorias[i] = new CategoriaDetail(
        faker.number.int(),
        faker.lorem.sentence(),
        faker.lorem.sentence(),
        testSeries
      );
    }
    component.categorias = testCategorias;

    fixture.detectChanges();
    debug = fixture.debugElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should set selectedCategoria and selected on selectCategoria', () => {
    const categoria = new CategoriaDetail(1, 'Nombre', 'Imagen', []);
    component.selectCategoria(categoria);
    expect(component.selectedCategoria ).toEqual(categoria);
    expect(component.selected).toBeTruthy();
  });
  it('should fetch categorias and assign them to component', fakeAsync(() => {
    const mockCategorias = [
      new CategoriaDetail(1, 'Nombre', 'Imagen', []),
      new CategoriaDetail(2, 'Nombre', 'Imagen', []),
      new CategoriaDetail(3, 'Nombre', 'Imagen', [])
    ];
    spyOn(categoriaService, 'getCategorias').and.returnValue(of(mockCategorias));
  
    component.getCategorias();
  
    tick(); 
  
    expect(component.categorias).toEqual(mockCategorias);
  }));
});
