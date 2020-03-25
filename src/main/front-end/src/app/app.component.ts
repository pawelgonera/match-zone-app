import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {TokenService} from "./service/token.service";

import {User} from "./model/user";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'match-zone-app';

  info: any;
  isLoggedIn = false;
  username: string;
  user: User = new User();
  id: number;

  expiration: Date;

  logo: any = "../assets/images/logo.png";

  constructor(private tokenService: TokenService, public router: Router) {
  }

  ngOnInit() {
    this.info = {
      token: this.tokenService.getToken(),
      username: this.tokenService.getUsername(),
      authorities: this.tokenService.getAuthorities(),
    };

    /*this.expiration = this.tokenService.getExpirationDate(this.info.token)

    if(this.tokenService.isTokenExpired(this.info.token)){
      console.log('Token expired!');
      this.logOut();
    }*/

    this.username = this.tokenService.getUsername();

    console.log('isLoggedIn in onInit()', this.isLoggedIn);
    if(this.info.token){
      this.isLoggedIn = true;
    }
  }

  userDetails(username: string){
    console.log(username);
    this.router.navigateByUrl('/profile', {skipLocationChange: true}).then(()=>
    this.router.navigate(['/profile', username]));
  }

  public logOut() {
    console.log('isLoggedIn in logOut()', this.isLoggedIn);
    this.isLoggedIn = false;
    this.tokenService.signOut();
    this.tokenService.setLoggedOut('true');
    this.router.navigate(['/login']);
  }


}
