import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CanActivate } from '@angular/router/src/utils/preactivation';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthguardGuard implements CanActivate  {
  constructor(private router: Router,
    private authService:AuthService){}
  path: ActivatedRouteSnapshot[];  route: ActivatedRouteSnapshot;
  canActivate(){

    if(this.authService.isLoggedIn()){
      return true;
    }
    this.router.navigate(['/login']);
    return false;
  }
}