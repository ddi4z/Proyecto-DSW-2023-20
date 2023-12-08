/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DebugElement } from '@angular/core';

import { PlataformaListComponent } from './plataforma-list.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { PlataformaService } from '../plataforma.service';
import { NgxPaginationModule } from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { Plan } from '../plan';
import { fakeAsync, tick } from '@angular/core/testing';
import { PlataformaDetail } from '../plataforma-detail';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';


describe('PlataformaListComponent', () => {
  let component: PlataformaListComponent;
  let fixture: ComponentFixture<PlataformaListComponent>;
  let debug: DebugElement;
  let plataformaService: PlataformaService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientModule, RouterTestingModule, NgxPaginationModule, Ng2SearchPipeModule,FormsModule],
      declarations: [ PlataformaListComponent],
      providers: [PlataformaService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlataformaListComponent);
    component = fixture.componentInstance;
    plataformaService = TestBed.inject(PlataformaService);
    fixture.detectChanges();
    debug = fixture.debugElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set selectedPlataforma and selected on onSelected', () => {
    const plataforma = new PlataformaDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[])
    component.onSelected(plataforma);
    expect(component.selectedPlataforma).toEqual(plataforma);
    expect(component.selected).toBeTruthy();
  });
  it('should fetch plataformas and assign them to component', fakeAsync(() => {
    const mockPlataformas = [
      new PlataformaDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[]),
      new PlataformaDetail(2, 'Nombre2', 'Biografia2', 'Imagen2', [],[])
    ];
    spyOn(plataformaService, 'getPlataformas').and.returnValue(of(mockPlataformas));
  
    component.getPlataformas();
  
    tick(); 
  
    expect(component.plataformas).toEqual(mockPlataformas);
  }));

  it('should get plataformas', () => {
    const plataformas: PlataformaDetail[] = [
      new PlataformaDetail(1,"net","net.com","net.png",[],[]),
      new PlataformaDetail(2,"ama","ama.com","ama.png",[],[])
    ]
    spyOn(plataformaService, 'getPlataformas').and.returnValue(of(plataformas));

    component.getPlataformas();

    expect(component.plataformas).toEqual(plataformas);
  });

  it('should get plataformas alfabeticamente', () => {
    const plataformasAlfabeticamente: PlataformaDetail[] = [
      new PlataformaDetail(2,"ama","ama.com","ama.png",[],[]),
      new PlataformaDetail(1,"net","net.com","net.png",[],[])
    ];
    spyOn(plataformaService, 'getPlataformasAlfabeticamente').and.returnValue(of(plataformasAlfabeticamente));

    component.getPlataformasAlfabeticamente();

    expect(component.plataformas).toEqual(plataformasAlfabeticamente);
  });

  it('should get plataformas anti-alfabeticamente', () => {
    const plataformasAntiAlfabeticamente: PlataformaDetail[] = [      
      new PlataformaDetail(1,"net","net.com","net.png",[],[]),
      new PlataformaDetail(2,"ama","ama.com","ama.png",[],[])];
    spyOn(plataformaService, 'getPlataformasAntiAlfabeticamente').and.returnValue(of(plataformasAntiAlfabeticamente));

    component.getPlataformasAntiAlfabeticamente();

    expect(component.plataformas).toEqual(plataformasAntiAlfabeticamente);
  });
});


describe('Plan', () => {
  it('should create an instance', () => {
    const plan = new Plan(1, 'Nombre', 50000, 10);
    expect(plan).toBeTruthy();
  });

  it('should set properties correctly through the constructor', () => {
    const plan = new Plan(1, 'Nombre', 50000, 10);
    expect(plan.id).toEqual(1);
    expect(plan.nombre).toEqual('Nombre');
    expect(plan.precio).toEqual(50000);
    expect(plan.puntaje).toEqual(10);
  });
});