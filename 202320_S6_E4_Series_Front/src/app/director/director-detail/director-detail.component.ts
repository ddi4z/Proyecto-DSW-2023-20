import { Component, OnInit } from '@angular/core';
import { DirectorService } from '../director.service';
import { ActivatedRoute } from '@angular/router';
import { DirectorDetail } from '../director-detail';


@Component({
  selector: 'app-director-detail',
  templateUrl: './director-detail.component.html',
  styleUrls: ['./director-detail.component.css']
})
export class DirectorDetailComponent implements OnInit {
  directorId!: string;
  directorDetail: DirectorDetail | null = null;
  showDetail = false;

  constructor(
    private route: ActivatedRoute,
    private directorService: DirectorService
  ) { }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.directorId = params['id'];
      if (this.directorId) {
        this.directorService.getDirector(this.directorId).subscribe(
          (apiData) => {
            this.directorDetail = apiData;
            this.showDetail = true; 
          },
        );
      }
    });
  }
}
