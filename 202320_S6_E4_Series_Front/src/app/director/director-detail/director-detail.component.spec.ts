import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { DirectorDetailComponent } from './director-detail.component';
import { DirectorService } from '../director.service';
import { DirectorDetail } from '../director-detail';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { NgxPaginationModule } from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FormsModule } from '@angular/forms';

describe('DirectorDetailComponent', () => {
  let component: DirectorDetailComponent;
  let fixture: ComponentFixture<DirectorDetailComponent>;
  let mockDirectorService: jasmine.SpyObj<DirectorService>;
  let mockActivatedRoute: Partial<ActivatedRoute>;

  beforeEach(() => {
    mockDirectorService = jasmine.createSpyObj('DirectorService', ['getDirector']);
    mockActivatedRoute = { params: of({ id: '1' }) };

    TestBed.configureTestingModule({
      declarations: [DirectorDetailComponent],
      imports:[HttpClientModule, RouterTestingModule, NgxPaginationModule, Ng2SearchPipeModule, FormsModule],
      providers: [
        { provide: DirectorService, useValue: mockDirectorService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DirectorDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch director details on init', () => {
    const mockDirectorDetail = new DirectorDetail(1, 'Nombre', 'Biografia', 'Imagen', [], []);
    mockDirectorService.getDirector.and.returnValue(of(mockDirectorDetail));

    fixture.detectChanges(); 

    expect(mockDirectorService.getDirector).toHaveBeenCalledWith('1');
    expect(component.directorDetail).toEqual(mockDirectorDetail);
    expect(component.showDetail).toBe(true);
  });
});
