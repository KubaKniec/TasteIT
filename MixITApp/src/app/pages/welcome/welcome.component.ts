import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {BodyScrollService} from "../../service/body-scroll.service";

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit, OnDestroy{
  constructor(private router: Router,
              private bodyScrollService: BodyScrollService,


  ) {}

  goto(url: string){
    this.router.navigateByUrl(url).then();
  }

  ngOnInit(): void {
    this.bodyScrollService.disableScroll();
  }

  ngOnDestroy(): void {
    this.bodyScrollService.enableScroll();
  }
}
