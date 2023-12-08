import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SerieModule } from './serie/serie.module';
import { DirectorModule } from './director/director.module';
import { ActorModule } from './actor/actor.module';
import { PlataformaModule } from './plataforma/plataforma.module';
import { HttpClientModule } from '@angular/common/http';
import { SerieRoutingModule } from './serie/serie-routing.module';
import { CategoriaModule } from './categoria/categoria.module';

@NgModule({
  declarations: [	
    AppComponent
   ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SerieModule,
    ActorModule,
    DirectorModule,
    PlataformaModule,
    CategoriaModule,
    HttpClientModule,
    SerieRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
