import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DirectorListComponent } from './director-list/director-list.component';
import { DirectorDetailComponent } from './director-detail/director-detail.component';
import { RouterModule } from '@angular/router';
import { DirectorRoutingModule } from './director-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxPaginationModule} from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FormsModule } from '@angular/forms';


@NgModule({
  imports: [CommonModule, RouterModule, ReactiveFormsModule, NgxPaginationModule, FormsModule, Ng2SearchPipeModule, DirectorRoutingModule],
  declarations: [DirectorListComponent, DirectorDetailComponent],
  exports: [DirectorListComponent, DirectorDetailComponent]
})
export class DirectorModule { }


