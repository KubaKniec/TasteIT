import {Component, OnInit} from '@angular/core';
import {FoodlistService} from "../../service/foodlist.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FoodList} from "../../model/FoodList";
import {DateFormatter} from "../../helpers/DateFormatter";
import {Post} from "../../model/post/Post";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../../components/dialogs/confirm-dialog/confirm-dialog.component";
import {HotToastService} from "@ngneat/hot-toast";
import {ChangeFlNameDialogComponent} from "../../components/dialogs/change-fl-name-dialog/change-fl-name-dialog.component";
import {NavigationService} from "../../service/navigation.service";

@Component({
  selector: 'app-foodlist-view',
  templateUrl: './foodlist-view.component.html',
  styleUrls: ['./foodlist-view.component.css']
})
export class FoodlistViewComponent implements OnInit {

  constructor(
    private foodlistService: FoodlistService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private toast: HotToastService,
    private router: Router,
    public navigationService: NavigationService
  ) { }
  foodListId: string = '';
  foodList!: FoodList;
  selectionMode = false;
  selectedPosts: Post[] = [];

  async ngOnInit(): Promise<void> {
    this.foodListId = this.route.snapshot.params['id'];
    this.foodList = await this.foodlistService.getFoodListById(this.foodListId);
  }
  toggleSelectionMode() {
    this.selectionMode = !this.selectionMode;
    if (!this.selectionMode) {
      this.selectedPosts = [];
    }
  }

  handleSelectionChange(posts: Post[]) {
    this.selectedPosts = posts;
  }
  async changeFoodListName() {
    const dialogRef = this.dialog.open(ChangeFlNameDialogComponent, {
      width: '300px',
      data: { currentName: this.foodList.name }
    });

    dialogRef.afterClosed().subscribe(async (result: string) => {
      if (result) {
        await this.foodlistService.updateFoodListName(this.foodListId, result);
        this.foodList = await this.foodlistService.getFoodListById(this.foodListId);
      }
    })

  }
  async deleteFoodList() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent);

    dialogRef.afterClosed().subscribe(async result => {
      if (result) {
        await this.foodlistService.deleteFoodList(this.foodListId);
        this.toast.info('Food list deleted');
        this.router.navigate(['/foodlists']);
      } else {

      }
    });
  }

  async removeSelectedPosts() {
    for (const post of this.selectedPosts) {
      await this.foodlistService.removePostFromFoodList(this.foodListId, post.postId!)
    }
    this.foodList = await this.foodlistService.getFoodListById(this.foodListId);
    this.selectionMode = false;
  }

  protected readonly DateFormatter = DateFormatter;
}
