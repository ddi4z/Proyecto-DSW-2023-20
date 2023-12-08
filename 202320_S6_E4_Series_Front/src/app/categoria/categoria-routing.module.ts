import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CategoriaListComponent } from './categoria-list/categoria-list.component';
import { CategoriaDetailComponent } from './categoria-detail/categoria-detail.component';


const routes: Routes = [{
  path: 'categorias',
  children: [
    {
      path: '',
      component: CategoriaListComponent
    },
    {
      path: ':id/series',
      component: CategoriaDetailComponent
    },
  ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CategoriaRoutingModule { }