import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { ActorDetailComponent } from './actor-detail.component';
import { ActorService } from '../actor.service';
import { ActorDetail } from '../actor-detail';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { NgxPaginationModule } from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FormsModule } from '@angular/forms';

describe('ActorDetailComponent', () => {
  let component: ActorDetailComponent;
  let fixture: ComponentFixture<ActorDetailComponent>;
  let mockActorService: jasmine.SpyObj<ActorService>;
  let mockActivatedRoute: Partial<ActivatedRoute>;

  beforeEach(() => {
    mockActorService = jasmine.createSpyObj('ActorService', ['getActor']);
    mockActivatedRoute = { params: of({ id: '1' }) };

    TestBed.configureTestingModule({
      declarations: [ActorDetailComponent],
      imports:[HttpClientModule, RouterTestingModule, NgxPaginationModule, Ng2SearchPipeModule, FormsModule],
      providers: [
        { provide: ActorService, useValue: mockActorService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ActorDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  

  it('should fetch actor details on init', () => {
    const mockActorDetail = new ActorDetail(1, 'Nombre', 'Biografia', 'Imagen', [],[])
    mockActorService.getActor.and.returnValue(of(mockActorDetail));

    fixture.detectChanges();

    expect(mockActorService.getActor).toHaveBeenCalledWith('1');
    expect(component.actorDetail).toEqual(mockActorDetail);
    expect(component.showDetail).toBe(true);
  });
});
