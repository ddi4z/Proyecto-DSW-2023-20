import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PlataformaListComponent } from './plataforma-list/plataforma-list.component';
import { PlataformaDetailComponent } from './plataforma-detail/plataforma-detail.component';
import { PlataformaRoutingModule } from './plataforma-routing.module';
import { NgxPaginationModule } from 'ngx-pagination';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    NgxPaginationModule,
    ReactiveFormsModule, 
    Ng2SearchPipeModule, 
    FormsModule,
    PlataformaRoutingModule
  ],
  declarations: [PlataformaListComponent, PlataformaDetailComponent],
  exports: [PlataformaListComponent, PlataformaDetailComponent]
})
export class PlataformaModule { }





