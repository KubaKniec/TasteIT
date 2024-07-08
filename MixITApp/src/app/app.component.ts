import {Component, OnInit, ViewContainerRef} from '@angular/core';
import {GlobalConfiguration} from "./config/GlobalConfiguration";
import {BodyScrollService} from "./service/BodyScrollService";
import {InstallAppModalFactoryService} from "./service/factories/InstallAppModalFactoryService";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit{
  title = 'MixIT';
  constructor(
              private viewContainerRef: ViewContainerRef,
              private bodyScrollService: BodyScrollService,
              private installAppModalFactoryService: InstallAppModalFactoryService
  ) {
    this.installAppModalFactoryService.setRootViewContainerRef(this.viewContainerRef);
  }
  isAppRunningAsPWA: boolean = window.matchMedia('(display-mode: standalone)').matches;
  ngOnInit(): void {
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
