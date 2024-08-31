import {inject, Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from "@angular/router";
import {AuthService} from "../service/auth.service";


export const AuthGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) =>{
  const authService = inject(AuthService)
  const router = inject(Router)

  return authService.isAuthenticated() ? true : router.createUrlTree(['/login'])
}
