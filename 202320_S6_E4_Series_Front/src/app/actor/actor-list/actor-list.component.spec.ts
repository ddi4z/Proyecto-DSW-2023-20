/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ActorListComponent } from './actor-list.component';
import { of } from 'rxjs';
import { fakeAsync, tick } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { ActorDetail } from '../actor-detail';
import { RouterTestingModule } from '@angular/router/testing';
import {NgxPaginationModule} from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { ActorService } from '../actor.service';
import { FormsModule } from '@angular/forms';

describe('ActorListComponent', () => {
  let component: ActorListComponent;
  let fixture: ComponentFixture<ActorListComponent>;
  let debug: DebugElement;
  let actorService: ActorService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientModule, RouterTestingModule, NgxPaginationModule, Ng2SearchPipeModule, FormsModule],
      declarations: [ ActorListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActorListComponent);
    component = fixture.componentInstance;
    actorService = TestBed.inject(ActorService);
    let testActores: Array<ActorDetail>= [];

    component.actores = testActores;

    fixture.detectChanges();
    debug = fixture.debugElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set selectedActor and selected on onSelected', () => {
    const actor = new ActorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[]);
    component.onSelected(actor);
    expect(component.selectedActor).toEqual(actor);
    expect(component.selected).toBeTruthy();
  });

  it('should fetch actores and assign them to component', fakeAsync(() => {
    const mockActores = [
      new ActorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[]),
      new ActorDetail(2, 'Nombre2', 'Biografia2', 'Imagen2', [],[])
    ];
    spyOn(actorService, 'getActores').and.returnValue(of(mockActores));
  
    component.getActores();
  
    tick(); 
  
    expect(component.actores).toEqual(mockActores);
  }));

  it('should fetch actores and assign them to component', fakeAsync(() => {
    const mockActores = [
      new ActorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[]),
      new ActorDetail(2, 'Nombre2', 'Biografia2', 'Imagen2', [],[])
    ];
    spyOn(actorService, 'getActores').and.returnValue(of(mockActores));
  
    component.getActores();
  
    tick(); 
  
    expect(component.actores).toEqual(mockActores);
  }));

  it('should call getActoresAlfabeticamente and update actores', () => {
    const mockActores = [
      new ActorDetail(1,"pepe","pepe.png","pepe es el mejor actor",[],[]),
      new ActorDetail(2,"pepa","pepa.png","pepa es la mejor actriz",[],[])
    ]
    spyOn(actorService, 'getActoresAlfabeticamente').and.returnValue(of(mockActores));

    component.getActoresAlfabeticamente();

    expect(actorService.getActoresAlfabeticamente).toHaveBeenCalled();
    expect(component.actores).toEqual(mockActores);
  });

  it('should call getActoresAntiAlfabeticamente and update actores', () => {
    const mockActores = [
      new ActorDetail(2,"pepa","pepa.png","pepa es la mejor actriz",[],[]),
      new ActorDetail(1,"pepe","pepe.png","pepe es el mejor actor",[],[])
    ]
    spyOn(actorService, 'getActoresAntiAlfabeticamente').and.returnValue(of(mockActores));

    component.getActoresAntiAlfabeticamente();

    expect(actorService.getActoresAntiAlfabeticamente).toHaveBeenCalled();
    expect(component.actores).toEqual(mockActores);
  });
});