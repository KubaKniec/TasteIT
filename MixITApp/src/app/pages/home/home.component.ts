import {Component, OnInit} from '@angular/core';
import {Post} from "../../model/Post";
import {Router} from "@angular/router";
import {Subject} from "rxjs";
import {PostService} from "../../service/post.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  allDrinks: Post[] = [];
  dailyDrink!: Post;
  popularDrinks: Post[] = [];
  nonAlkDrinks: Post[] = [];
  selectedChip: string = 'popular'
  greeting: string = ''
  targetElement!: Element;
  posts: Post[] = [];
  constructor(private router: Router,
              private postService: PostService) {
  }
  selectChip(chip: string): void {
    this.selectedChip = chip;
    if(this.selectedChip == undefined){
      this.selectedChip = 'popular';
    }
  }
  async ngOnInit(): Promise<void> {

    this.targetElement = document.querySelector('html') as Element;
    this.greeting = this.getGreetingDependingOnTime();
    this.postService.getFeed().then((posts) => {
      this.posts = posts;
    }).catch((error) => {
      return
    }).finally(() => {

    })
  }
  refreshEvent(event: Subject<any>, message: string): void {
    setTimeout(() => {
      // handle refreshing feed here
      event.next(event);
    }, 500);
  }
  gotoDrink(id: string){
    this.router.navigate([`/drink/${id}`]).then();
  }
  getGreetingDependingOnTime(): string {
    const currentHour = new Date().getHours();
    if (currentHour >= 5 && currentHour < 12) {
      return "Good Morning!";
    } else if (currentHour >= 12 && currentHour < 18) {
      return "Good Afternoon!";
    } else {
      return "Good Evening!";
    }
  }

    getIterableDrinkBasedOnSelectedChip(): Post[] {
    if(this.selectedChip == 'noalc'){
      return this.nonAlkDrinks;
    }
    if(this.selectedChip == 'popular'){
      return this.popularDrinks;
    }
    if(this.selectedChip =='All'){
      return this.allDrinks;
    }
    return this.allDrinks;
   }
}
