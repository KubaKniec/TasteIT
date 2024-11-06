import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FoodlistService} from "../../service/foodlist.service";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {FoodList} from "../../model/FoodList";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-add-to-food-list',
  templateUrl: './add-to-food-list.component.html',
  styleUrls: ['./add-to-food-list.component.css'],
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
export class AddToFoodListComponent implements OnInit{
  @Output() close = new EventEmitter<void>();
  @Input() userId: string = '';
  @Input() postId: string = '';
  state = 'enter'
  foodLists: FoodList[] = [];
  isCreatingNewFoodlist = false;
  newFoodlistName = '';

  constructor(
    private foodListService: FoodlistService,
    private toast: HotToastService
  ) {}
  async ngOnInit(): Promise<void> {
    this.foodLists = await this.foodListService.getAllFoodLists();
  }
  onSubmitNewFoodlist(){
    if(this.newFoodlistName.length < 3){
      this.toast.error('Food list name must be at least 3 characters long');
      return;
    }
    this.foodListService.createFoodList(this.newFoodlistName).then((res) => {
      this.toast.success('Food list created');
      this.foodLists.push(res);
      this.isCreatingNewFoodlist = false;
    }).catch((error) => {
      this.toast.error('Error creating food list');
    });
  }
  onCreateNewFoodlist() {
    this.isCreatingNewFoodlist = true;
  }
  onClose(){
    this.state = 'void';
    this.close.emit();
  }

  addPostToFoodList(fl: FoodList) {
    this.foodListService.addPostToFoodList(fl.foodListId, this.postId).then(() => {
      this.toast.success('Post added to food list');
      this.onClose();
    }).catch((error) => {
      this.toast.error('Error adding post to food list');
    });
  }
}
