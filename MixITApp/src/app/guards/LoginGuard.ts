import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from "@angular/router";
import {inject} from "@angular/core";
import {AuthService} from "../service/auth.service";

export const LoginGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) =>{
  const authService = inject(AuthService)
  const router = inject(Router)

  return authService.isAuthenticated() ? router.createUrlTree(['/home']) : true
}
