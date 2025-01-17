import {Component, OnInit, ViewContainerRef} from '@angular/core';
import {GlobalConfiguration} from "./config/GlobalConfiguration";
import {BodyScrollService} from "./service/body-scroll.service";
import {InstallAppModalFactoryService} from "./service/factories/install-app-modal-factory.service";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {filter} from "rxjs";
import {NotificationsService} from "./service/notifications.service";

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
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private notificationService: NotificationsService
  ) {
    this.installAppModalFactoryService.setRootViewContainerRef(this.viewContainerRef);
  }
  isAppRunningAsPWA: boolean = window.matchMedia('(display-mode: standalone)').matches;
  ngOnInit(): void {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(()=>{
        const currentRoute = this.activatedRoute.snapshot.firstChild;
        if(currentRoute){
          this.showNav = currentRoute.data['showNav'] !== false;
        }else{
          this.showNav = true;
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

    // setting up notifications
  }


}
