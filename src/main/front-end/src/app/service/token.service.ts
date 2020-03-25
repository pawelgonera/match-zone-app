import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

const TOKEN_KEY = 'AuthToken';
const USERNAME_KEY = 'AuthUsername';
const AUTHORITIES_KEY = 'AuthAuthorities';
const LOGGED_OUT = 'LoggedOut';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private roles: Array<string> = [];
  helper = new JwtHelperService();
  constructor() { }

  signOut() {
    window.sessionStorage.clear();
  }

  public saveToken(token: string) {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUsername(username: string) {
    window.sessionStorage.removeItem(USERNAME_KEY);
    window.sessionStorage.setItem(USERNAME_KEY, username);
  }

  public getUsername(): string {
    console.log('getUsername', sessionStorage.getItem(USERNAME_KEY));
    return sessionStorage.getItem(USERNAME_KEY);
  }

  public saveAuthorities(authorities: string[]) {
    window.sessionStorage.removeItem(AUTHORITIES_KEY);
    window.sessionStorage.setItem(AUTHORITIES_KEY, JSON.stringify(authorities));
  }

  public getAuthorities(): string[] {
    this.roles = [];
    if (sessionStorage.getItem(TOKEN_KEY)) {
      JSON.parse(sessionStorage.getItem(AUTHORITIES_KEY)).forEach(authority => {
        this.roles.push(authority.authority);
      });
    }

    return this.roles;
  }

  public getExpirationDate(token: string): Date{
    const expirationDate = this.helper.getTokenExpirationDate(token);
    console.log('expirationDate: ', expirationDate);
    if(expirationDate == null){
      return new Date;
    }
    return expirationDate;
  }

  public isTokenExpired(token: string){
    const expirationDate = this.getExpirationDate(token);
    return expirationDate.valueOf() < new Date().valueOf();
  }

 public setLoggedOut(logged: string) {
    window.sessionStorage.removeItem(LOGGED_OUT);
    window.sessionStorage.setItem(LOGGED_OUT, logged);
  }

  public getLoggedOut(): string {
    console.log('getLoggedOut', sessionStorage.getItem(LOGGED_OUT));
    return sessionStorage.getItem(LOGGED_OUT);
  }

}
