import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SerieListComponent } from './serie-list/serie-list.component';
import { SerieDetailComponent } from './serie-detail/serie-detail.component';


const routes: Routes = [{
  path: 'series',
  children: [
    {
      path: '',
      component: SerieListComponent
    },
    {
      path: ':id',
      component: SerieDetailComponent
    },
  ]
}];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' })],
  exports: [RouterModule],
})
export class SerieRoutingModule { }