import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SerieListComponent } from './serie-list/serie-list.component';
import { SerieDetailComponent } from './serie-detail/serie-detail.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FilterByPlataformaPipe, FilterByAnioPipe } from './filter.pipe';
import { CategoriaModule } from '../categoria/categoria.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    NgxPaginationModule,
    ReactiveFormsModule,
    Ng2SearchPipeModule,
    FormsModule,
  ],
  declarations: [SerieListComponent, SerieDetailComponent, FilterByAnioPipe,FilterByPlataformaPipe],
  exports: [SerieListComponent, SerieDetailComponent,FilterByAnioPipe,FilterByPlataformaPipe]
})
export class SerieModule { }



