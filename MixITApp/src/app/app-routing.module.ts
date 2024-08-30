import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./pages/home/home.component";
import {DrinkBuilderComponent} from "./pages/drink-builder/drink-builder.component";
import {ProfileComponent} from "./pages/profile/profile.component";
import {SearchComponent} from "./pages/search/search.component";
import {DrinkViewComponent} from "./pages/drink-view/drink-view.component";
import {CategoryViewComponent} from "./pages/category-view/category-view.component";
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {WelcomeComponent} from "./pages/welcome/welcome.component";

const routes: Routes = [
  {path: '', redirectTo:'/welcome', pathMatch: 'full'},
  {path: 'welcome', component: WelcomeComponent, data: {showNav: false}},
  {path: 'home', component: HomeComponent},
  {path: 'drinkBuilder', component: DrinkBuilderComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'search', component: SearchComponent},
  {path: "drink/:id", component: DrinkViewComponent, data: {showNav: false}},
  {path: "category/:category", component: CategoryViewComponent},
  {path: 'login', component: LoginComponent, data: {showNav: false}},
  {path: 'register', component: RegisterComponent, data: {showNav: false}},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
