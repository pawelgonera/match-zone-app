import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest, HttpEvent} from '@angular/common/http';
import { TokenService } from "../service/token.service";
import { Observable } from 'rxjs';
import {AppComponent} from "../app.component";
import {LoginComponent} from "../components/login/login.component";
import {Router} from "@angular/router";

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {

  constructor(private tokenService: TokenService, private router: Router, private appComponent: AppComponent, private loginComponent: LoginComponent) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('INTERCEPT');
    let authRequest = request;
    const token = this.tokenService.getToken();
    if (token != null) {
      if(this.tokenService.isTokenExpired(token)){
        console.log('Token expired!');
        this.appComponent.logOut();
      }
      authRequest = request.clone({ headers: request.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token) });
    }else{
      this.router.navigate(['/login']);
    }
    return next.handle(authRequest);
  }

}

export const httpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptor, multi: true }
];
