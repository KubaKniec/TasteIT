import {Component, OnInit, ViewContainerRef} from '@angular/core';
import {GlobalConfiguration} from "./config/GlobalConfiguration";
import {BodyScrollService} from "./service/body-scroll.service";
import {InstallAppModalFactoryService} from "./service/factories/install-app-modal-factory.service";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit{
  title = 'MixIT';
  showNav: boolean = true;
  constructor(
              private viewContainerRef: ViewContainerRef,
              private bodyScrollService: BodyScrollService,
              private installAppModalFactoryService: InstallAppModalFactoryService,
              private router: Router
  ) {
    this.installAppModalFactoryService.setRootViewContainerRef(this.viewContainerRef);
  }
  isAppRunningAsPWA: boolean = window.matchMedia('(display-mode: standalone)').matches;

  noNavUrls: string[] = [
    '/welcome',
    '/login',
    '/register',
    '/drink'
  ]
  ngOnInit(): void {
    // hide navBar depending on current route
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.showNav = !this.noNavUrls.some(url => event.url.includes(url));
      }
    })

    // show install modal if app isn't running as PWA
    if(!GlobalConfiguration.ALLOW_ALL_DEVICES && !this.isAppRunningAsPWA){
      this.bodyScrollService.disableScroll();
      const componentRef = this.installAppModalFactoryService.addDynamicComponent();
      componentRef.instance.close.subscribe(() => {
        this.installAppModalFactoryService.removeDynamicComponent(componentRef)
        this.bodyScrollService.enableScroll();
      });
    }
  }


}
