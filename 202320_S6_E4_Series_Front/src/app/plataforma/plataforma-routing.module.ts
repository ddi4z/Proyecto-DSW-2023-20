import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PlataformaListComponent } from './plataforma-list/plataforma-list.component';
import { PlataformaDetailComponent } from './plataforma-detail/plataforma-detail.component';


const routes: Routes = [{
  path: 'plataformas',
  children: [
    {
      path: '',
      component: PlataformaListComponent
    },
    {
      path: ':id',
      component: PlataformaDetailComponent
    },
  ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PlataformaRoutingModule { }