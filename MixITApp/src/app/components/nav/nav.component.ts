import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit{
  constructor(private router: Router) {
  }
  ngOnInit(): void {
  }
  goto(url: string){
    this.router.navigate([url]).then();
  }

  activeRoute(url: string) {
    if (this.router.url === url) {
      return 'nav-item active';
    }
    return 'nav-item';
  }
}
