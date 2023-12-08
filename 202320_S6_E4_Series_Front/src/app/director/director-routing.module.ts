import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DirectorListComponent } from './director-list/director-list.component';
import { DirectorDetailComponent } from './director-detail/director-detail.component';


const routes: Routes = [{
  path: 'directores',
  children: [
    {
      path: '',
      component: DirectorListComponent
    },
    {
      path: ':id',
      component: DirectorDetailComponent
    },
  ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DirectorRoutingModule { }