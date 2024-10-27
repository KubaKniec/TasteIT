import {Component, EventEmitter, Input, Output} from '@angular/core';
import {User} from "../../model/user/User";
import {Router} from "@angular/router";

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent {
  @Input() users: User[] = [];
  @Output() loadMore = new EventEmitter<void>();
  constructor(private router: Router) {
  }

  gotoUser(id: string) {
    this.router.navigate(['/user-profile', id]);
  }

  onScroll() {
    this.loadMore.emit();
  }
}
