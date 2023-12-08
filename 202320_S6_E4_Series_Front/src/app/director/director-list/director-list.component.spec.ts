/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DirectorListComponent } from './director-list.component';
import { fakeAsync, tick } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { DirectorDetail } from '../director-detail';
import { RouterTestingModule } from '@angular/router/testing';
import {NgxPaginationModule} from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { DirectorService } from '../director.service';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';

describe('DirectorListComponent', () => {
  let component: DirectorListComponent;
  let fixture: ComponentFixture<DirectorListComponent>;
  let debug: DebugElement;
  let directorService: DirectorService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientModule, RouterTestingModule, NgxPaginationModule, Ng2SearchPipeModule,FormsModule],
      declarations: [ DirectorListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DirectorListComponent);
    component = fixture.componentInstance;
    directorService = TestBed.inject(DirectorService);
    let testDirectores: Array<DirectorDetail>= [];

    component.directores = testDirectores;

    fixture.detectChanges();
    debug = fixture.debugElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set selectedDirector and selected on onSelected', () => {
    const director = new DirectorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[]);
    component.onSelected(director);
    expect(component.selectedDirector).toEqual(director);
    expect(component.selected).toBeTruthy();
  });
  it('should fetch directores and assign them to component', fakeAsync(() => {
    const mockDirectores = [
      new DirectorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[]),
      new DirectorDetail(2, 'Nombre2', 'Biografia2', 'Imagen2', [],[])
    ];
    spyOn(directorService, 'getDirectores').and.returnValue(of(mockDirectores));
  
    component.getDirectores();
  
    tick(); 
  
    expect(component.directores).toEqual(mockDirectores);
  }));

  it('should call getDirectoresAlfabeticamente and update actores', () => {
    const mockDirectores = [
      new DirectorDetail(1,"pepe","pepe.png","pepe es el mejor director",[],[]),
      new DirectorDetail(2,"pepa","pepa.png","pepa es la mejor directora",[],[])
    ]
    spyOn(directorService, 'getDirectoresAlfabeticamente').and.returnValue(of(mockDirectores));

    component.getDirectoresAlfabeticamente();

    expect(directorService.getDirectoresAlfabeticamente).toHaveBeenCalled();
    expect(component.directores).toEqual(mockDirectores);
  });

  it('should call getDirectoresAntiAlfabeticamente and update actores', () => {
    const mockDirectores = [
      new DirectorDetail(2,"pepa","pepa.png","pepa es la mejor directora",[],[]),
      new DirectorDetail(1,"pepe","pepe.png","pepe es el mejor director",[],[])
    ]
    spyOn(directorService, 'getDirectoresAntiAlfabeticamente').and.returnValue(of(mockDirectores));

    component.getDirectoresAntiAlfabeticamente();

    expect(directorService.getDirectoresAntiAlfabeticamente).toHaveBeenCalled();
    expect(component.directores).toEqual(mockDirectores);
  });
});