import { Injectable } from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";
@Injectable({
  providedIn: 'root'
})
export class NavigationService {
  private history: string[] = [];
  constructor(private router: Router) {
    this.router.events.subscribe((event) => {
      if(event instanceof NavigationEnd){
        this.history.push(event.urlAfterRedirects);
      }
    })
  }

  public getHisory(): string[] {
    return this.history;
  }
  public goBack(): void {
    this.history.pop();
    this.history.length > 0 ? this.router.navigateByUrl(this.history[this.history.length - 1]) : this.router.navigateByUrl('/');
  }
  public getPreviousUrl(): string {
    return this.history.length > 0 ? this.history[this.history.length - 2] : '/';
  }
}
