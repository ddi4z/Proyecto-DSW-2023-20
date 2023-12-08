import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActorListComponent } from './actor-list/actor-list.component';
import { ActorDetailComponent } from './actor-detail/actor-detail.component';
import { RouterModule } from '@angular/router';
import { ActorRoutingModule } from './actor-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import {NgxPaginationModule} from 'ngx-pagination';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [CommonModule, RouterModule, ReactiveFormsModule, NgxPaginationModule, FormsModule, Ng2SearchPipeModule, ActorRoutingModule],
  declarations: [ActorListComponent, ActorDetailComponent],
  exports: [ActorListComponent, ActorDetailComponent]
})
export class ActorModule { }


