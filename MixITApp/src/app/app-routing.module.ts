import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {DrinkBuilderComponent} from "./components/drink-builder/drink-builder.component";
import {YourBarComponent} from "./components/your-bar/your-bar.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {SearchComponent} from "./components/search/search.component";

const routes: Routes = [
  {path: '', redirectTo:'/home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: 'drinkBuilder', component: DrinkBuilderComponent},
  {path: 'yourBar', component: YourBarComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'search', component: SearchComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
