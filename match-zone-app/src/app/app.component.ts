import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Router} from "@angular/router";
import {TokenService} from "./service/token.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'match-zone-app';

  info: any;
  isLoggedIn = false;

  constructor(private tokenService: TokenService, public router: Router) {
  }

  ngOnInit() {
    this.info = {
      token: this.tokenService.getToken(),
      username: this.tokenService.getUsername(),
      authorities: this.tokenService.getAuthorities()
    };

    console.log('isLoggedIn in onInit()', this.isLoggedIn);
    if(this.info.token){
      this.isLoggedIn = true;
    }
  }

  logOut() {
    console.log('isLoggedIn in logOut()', this.isLoggedIn);
    this.isLoggedIn = false;
    this.tokenService.signOut();
    this.router.navigate(['/login']);
  }

}
