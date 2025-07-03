import { ApplicationRef, Injectable, createComponent, EnvironmentInjector } from '@angular/core';
import { NotificationToastComponent } from '../components/notification-toast/notification-toast.component';
import { Notification } from '../model/Notification';

@Injectable({
  providedIn: 'root'
})
export class NotificationToastService {
  private toasts: NotificationToastComponent[] = [];

  constructor(
    private appRef: ApplicationRef,
    private environmentInjector: EnvironmentInjector
  ) {}

  show(notification: Notification) {
    const componentRef = createComponent(NotificationToastComponent, {
      environmentInjector: this.environmentInjector,
      hostElement: document.body
    });

    componentRef.instance.notification = notification;
    componentRef.instance.onClose.subscribe(() => {
      this.removeToast(componentRef);
    });

    componentRef.changeDetectorRef.detectChanges();

    this.toasts.push(componentRef.instance);
    setTimeout(() => {
      this.removeToast(componentRef);
    }, 5000);
  }

  private removeToast(componentRef: any) {
    const index = this.toasts.indexOf(componentRef.instance);
    if (index > -1) {
      componentRef.destroy();
      this.toasts.splice(index, 1);
    }
  }
}
