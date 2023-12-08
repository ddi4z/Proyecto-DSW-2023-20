import { Component, OnInit } from '@angular/core';
import { ActorService } from '../actor.service';
import { ActivatedRoute } from '@angular/router';
import { ActorDetail } from '../actor-detail';

@Component({
  selector: 'app-actor-detail',
  templateUrl: './actor-detail.component.html',
  styleUrls: ['./actor-detail.component.css']
})
export class ActorDetailComponent implements OnInit {
  actorId!: string;
  actorDetail: ActorDetail | null = null; // Initialize as null
  showDetail = false; // Use a boolean flag

  constructor(
    private route: ActivatedRoute,
    private actorService: ActorService
  ) { }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.actorId = params['id'];
      if (this.actorId) {
        this.actorService.getActor(this.actorId).subscribe(
          (apiData) => {
            this.actorDetail = apiData;
            this.showDetail = true; 
          },
        );
      }
    });
  }
}
