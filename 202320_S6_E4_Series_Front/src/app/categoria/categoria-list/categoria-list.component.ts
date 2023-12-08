import { Component, OnInit } from '@angular/core';
import { CategoriaDetail } from '../categoria-detail';
import { CategoriaService } from '../categoria.service';

@Component({
  selector: 'app-categoria-list',
  templateUrl: './categoria-list.component.html',
  styleUrls: ['./categoria-list.component.css']
})
export class CategoriaListComponent implements OnInit {
  categorias: Array<CategoriaDetail> = [];
  selectedCategoria!: CategoriaDetail;
  selected: boolean = false;
  p: number = 1; 
  itemsPerPage: number = 18;

  constructor(private categoriaService: CategoriaService) { }

  getCategorias(){
    this.categoriaService.getCategorias().subscribe(apiData=>{
      this.categorias = apiData;
    })
  }

  selectCategoria(categoria: CategoriaDetail){
    this.selectedCategoria = categoria;
    this.selected = true;
  }

  ngOnInit() {
    this.getCategorias();
  }

}
