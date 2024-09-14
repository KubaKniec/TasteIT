import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavComponent } from './components/nav/nav.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatChipsModule} from "@angular/material/chips";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatIconModule} from "@angular/material/icon";
import {HotToastService, provideHotToastConfig} from "@ngneat/hot-toast";
import { HomeComponent } from './pages/home/home.component';
import { DrinkBuilderComponent } from './pages/drink-builder/drink-builder.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { SearchComponent } from './pages/search/search.component';
import {MatToolbarModule} from "@angular/material/toolbar";
import { DrinkViewComponent } from './pages/drink-view/drink-view.component';
import {MatButtonModule} from "@angular/material/button";
import {MatRippleModule} from "@angular/material/core";
import { ServiceWorkerModule } from '@angular/service-worker';
import { InstructionsViewComponent } from './components/instructions-view/instructions-view.component';
import {InstructionsFactoryService} from "./service/factories/instructions-factory.service";
import {BodyScrollService} from "./service/body-scroll.service";
import { DailyDrinkComponent } from './components/daily-drink/daily-drink.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { DrinksGridComponent } from './components/drinks-grid/drinks-grid.component';
import {MatBadgeModule} from "@angular/material/badge";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatInputModule} from "@angular/material/input";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import { InstallAppModalComponent } from './components/install-app-modal/install-app-modal.component';
import {NgOptimizedImage} from "@angular/common";
import { BetaBadgeComponent } from './components/beta-badge/beta-badge.component';
import {MatSelectModule} from "@angular/material/select";
import { CategoryViewComponent } from './pages/category-view/category-view.component';
import {MatRadioModule} from "@angular/material/radio";
import {MatTabsModule} from "@angular/material/tabs";
import { IngredientViewComponent } from './components/ingredient-view/ingredient-view.component';
import {IngredientViewFactoryService} from "./service/factories/ingredient-view-factory.service";
import {MatListModule} from "@angular/material/list";
import {AuthService} from "./service/auth.service";
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import {CookieService} from "ngx-cookie-service";
import { BarViewComponent } from './components/bar-view/bar-view.component';
import { WelcomeComponent } from './pages/welcome/welcome.component';
import { FeedItemComponent } from './components/feed-item/feed-item.component';
import {NgxPullToRefreshModule} from "ngx-pull-to-refresh";
import {NavigationService} from "./service/navigation.service";
import {PostService} from "./service/post.service";
import {InfiniteScrollModule} from "ngx-infinite-scroll";
import { SplashScreenComponent } from './components/splash-screen/splash-screen.component';
import {SplashScreenFactoryService} from "./service/factories/splash-screen-factory.service";
import { SetupProfileComponent } from './pages/setup-profile/setup-profile.component';
import {MatStepperModule} from "@angular/material/stepper";
import {UserService} from "./service/user.service";

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    HomeComponent,
    DrinkBuilderComponent,
    ProfileComponent,
    SearchComponent,
    DrinkViewComponent,
    InstructionsViewComponent,
    DailyDrinkComponent,
    DrinksGridComponent,
    InstallAppModalComponent,
    BetaBadgeComponent,
    CategoryViewComponent,
    IngredientViewComponent,
    LoginComponent,
    RegisterComponent,
    BarViewComponent,
    WelcomeComponent,
    FeedItemComponent,
    SplashScreenComponent,
    SetupProfileComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatChipsModule,
    MatSlideToggleModule,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule,
    MatRippleModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: !isDevMode(),
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    }),
    FormsModule,
    MatBadgeModule,
    MatAutocompleteModule,
    MatInputModule,
    MatProgressSpinnerModule,
    NgOptimizedImage,
    MatSelectModule,
    MatRadioModule,
    MatTabsModule,
    MatListModule,
    ReactiveFormsModule,
    NgxPullToRefreshModule,
    InfiniteScrollModule,
    MatStepperModule
  ],
  providers: [
    HotToastService,
    InstructionsFactoryService,
    BodyScrollService,
    IngredientViewFactoryService,
    AuthService,
    provideHotToastConfig(),
    CookieService,
    NavigationService,
    PostService,
    SplashScreenFactoryService,
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
