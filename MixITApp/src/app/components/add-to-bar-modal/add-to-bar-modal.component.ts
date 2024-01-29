import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {UserService} from "../../service/UserService";
import {User} from "../../model/User";
import {Bar} from "../../model/Bar";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-add-to-bar-modal',
  templateUrl: './add-to-bar-modal.component.html',
  styleUrls: ['./add-to-bar-modal.component.css'],
  animations: [
    trigger('dialog', [
      state('void', style({
        transform: 'translateY(20px)',
        opacity: 0
      })),
      state('enter', style({
        transform: 'translateY(0)',
        opacity: 1
      })),
      transition('void => enter', animate('300ms cubic-bezier(0.25, 0.8, 0.25, 1)')),
      transition('enter => void', animate('300ms cubic-bezier(0.25, 0.8, 0.25, 1)'))
    ])
  ]
})
export class AddToBarModalComponent implements OnInit{
  @Output() close = new EventEmitter<void>();
  @Input() drinkId!: number;
  state = 'enter'
  user: User = {}
  userBars: Bar[] = []
  constructor(private userSerice: UserService, private toast: HotToastService) {
  }
  ngOnInit(): void {
    this.userSerice.getUser().then((user) => {
      this.user = user;
      this.user.bars?.forEach((bar) => {
        this.userBars.push(bar);
      })
    }).catch((e) => {
      console.log(e);
    })
  }
  onClose(){
    this.close.emit();
  }

  addToBar(barId: number){
    this.userSerice.addDrinkToBar(barId, this.drinkId).then((r) => {
      this.toast.success('Drink added to bar');
      this.close.emit();
    })
  }
}
