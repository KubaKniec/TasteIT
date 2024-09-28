import {Component, HostListener, OnInit, ViewContainerRef} from '@angular/core';
import {Post} from "../../model/Post";
import {Router} from "@angular/router";
import {Subject} from "rxjs";
import {PostService} from "../../service/post.service";
import {SplashScreenFactoryService} from "../../service/factories/splash-screen-factory.service";
import {BodyScrollService} from "../../service/body-scroll.service";
import {AuthService} from "../../service/auth.service";
import {UserService} from "../../service/user.service";
import {User} from "../../model/User";
import {ScrollPositionService} from "../../service/scroll-position.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  greeting: string = ''
  targetElement!: Element;
  posts: Post[] = [];
  page: number = 0;
  size: number = 20;
  loading: boolean = false;
  user!: User;
  constructor(private router: Router,
              private postService: PostService,
              private splashScreenFactoryService: SplashScreenFactoryService,
              private viewContainerRef: ViewContainerRef,
              private bodyScrollService: BodyScrollService,
              private authService: AuthService,
              private userService: UserService,
              private scrollPositionService: ScrollPositionService
              ) {
    this.splashScreenFactoryService.setRootViewContainerRef(this.viewContainerRef);
  }
  async ngOnInit(): Promise<void> {
    this.targetElement = document.querySelector('html') as Element;
    this.greeting = this.getGreetingDependingOnTime();
    this.user = await this.userService.getUserByToken();

    if(this.user.firstLogin){
      this.initializeCompleteAccountSplashScreen();
    }

    const cachedPosts = this.postService.getFeedState();
    if(cachedPosts.length > 0){
      this.posts = cachedPosts;
      setTimeout(() => window.scrollTo(0, this.scrollPositionService.getScrollPosition()), 0);
    }else{
      await this.refreshPosts();
    }
  }
  @HostListener('window:scroll', ['$event'])
  onScroll(): void {
    this.scrollPositionService.setScrollPosition(window.scrollY);
  }

  async refreshPosts(): Promise<void> {
    this.postService.clearFeedCache();
    this.page = 0;
    this.posts = [];
    await this.loadPost();
    this.scrollPositionService.setScrollPosition(0);
    window.scrollTo(0, 0);
  }
  handleRefreshEvent(event: Subject<any>, message: string): void {
    setTimeout(() => {
      this.refreshPosts().then();
      event.next(event);
    }, 500);
  }

  gotoPost(id: string){
    this.scrollPositionService.setScrollPosition(window.scrollY);
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
  async loadPost(){
    if (this.loading) return
    this.loading = true;
    try{
      const newPosts = await this.postService.getFeed(this.page, this.size);
      this.posts = [...this.posts, ...newPosts];
      this.page++;
    }catch (e){
      console.error(e)
    }finally {
      this.loading = false;
    }
  }
  initializeCompleteAccountSplashScreen(): void {
    const splashScreenData = {
        title: 'Complete your profile',
        content: [
            {icon: 'assets/wand.svg', subtitle: 'Personalize your experience', text: 'Tailor content to your tastes and discover recipes you\'ll love.'},
            {icon: 'assets/users.svg', subtitle: 'Connect with like-minded foodies', text: 'Share your passion and build a community around your favorite dishes.'},
            {icon: 'assets/unlock.svg', subtitle: 'Unlock exclusive features', text: 'Get access to personalized recommendations and community-driven culinary insights.'}
        ],
        actionButtonLabel: 'Continue',
        closeButtonLabel: 'Maybe later'
    }
    this.bodyScrollService.disableScroll();

    const componentRef = this.splashScreenFactoryService.addDynamicComponent(
      splashScreenData.title,
      splashScreenData.content,
      splashScreenData.actionButtonLabel,
      splashScreenData.closeButtonLabel);

    componentRef.instance.actionButton.subscribe(() => {
      this.router.navigate(['/setup-profile']).then();
    })
    componentRef.instance.close.subscribe(() => {
      this.splashScreenFactoryService.removeDynamicComponent(componentRef);
      this.bodyScrollService.enableScroll();
    });

  }

}
