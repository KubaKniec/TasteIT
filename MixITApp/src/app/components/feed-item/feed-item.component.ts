import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-feed-item',
  templateUrl: './feed-item.component.html',
  styleUrls: ['./feed-item.component.css']
})
export class FeedItemComponent {
  @Input() feedItem: any = {};
  @Output() gotoDrink: EventEmitter<any> = new EventEmitter<any>();

  emitGotoDrink(): void {
    this.gotoDrink.emit(this.feedItem.idDrink);
  }

  emitLike(event: Event) {
    event.stopPropagation();
    console.log('Like clicked');
  }

  emitComment(event: Event) {
    event.stopPropagation();
    console.log('Comment clicked');

  }
}
