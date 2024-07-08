import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./pages/home/home.component";
import {DrinkBuilderComponent} from "./pages/drink-builder/drink-builder.component";
import {YourBarComponent} from "./pages/your-bar/your-bar.component";
import {ProfileComponent} from "./pages/profile/profile.component";
import {SearchComponent} from "./pages/search/search.component";
import {DrinkViewComponent} from "./pages/drink-view/drink-view.component";
import {CategoryViewComponent} from "./pages/category-view/category-view.component";
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {FavouritesComponent} from "./pages/favourites/favourites.component";

const routes: Routes = [
  {path: '', redirectTo:'/home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: 'drinkBuilder', component: DrinkBuilderComponent},
  {path: 'yourBar', component: YourBarComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'search', component: SearchComponent},
  {path: "drink/:id", component: DrinkViewComponent},
  {path: "category/:category", component: CategoryViewComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'favourites', component: FavouritesComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
