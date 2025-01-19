import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {NotificationsService} from "../../service/notifications.service";
import {Notification} from "../../model/Notification";

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit{
  notifications$: Observable<Notification[]>;
  unreadCount$: Observable<number>;
  constructor(private notificationsService: NotificationsService) {
    this.notifications$ = this.notificationsService.getNotifications();
    this.unreadCount$ = this.notificationsService.getUnreadCount();
  }
  markAsRead(notificationId: string): void {
    this.notificationsService.markAsRead(notificationId).then();
  }
  ngOnInit(): void {
  }

}
