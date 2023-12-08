import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoriaListComponent } from './categoria-list/categoria-list.component';
import { CategoriaDetailComponent } from './categoria-detail/categoria-detail.component';
import { RouterModule } from '@angular/router';
import { CategoriaRoutingModule } from './categoria-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxPaginationModule} from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FormsModule } from '@angular/forms';


@NgModule({
  imports: [CommonModule, RouterModule, ReactiveFormsModule, NgxPaginationModule, FormsModule, Ng2SearchPipeModule, CategoriaRoutingModule],
  declarations: [CategoriaListComponent, CategoriaDetailComponent],
  exports: [CategoriaListComponent, CategoriaDetailComponent]
})
export class CategoriaModule { }



