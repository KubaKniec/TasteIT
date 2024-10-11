import {Component, OnInit} from '@angular/core';
import {CameraService} from "../../service/camera.service";
import {Router} from "@angular/router";


@Component({
  selector: 'app-post-builder',
  templateUrl: './post-builder.component.html',
  styleUrls: ['./post-builder.component.css']
})
export class PostBuilderComponent implements OnInit{

  constructor(
    private cameraService: CameraService,
    private router: Router
  ) {
  }

  async ngOnInit(): Promise<void> {
    await this.cameraService.addNewToGallery().catch(err => {
      this.router.navigate(['/home']);
    });
  }

  async addPhoto() {
    await this.cameraService.addNewToGallery();
  }
}
