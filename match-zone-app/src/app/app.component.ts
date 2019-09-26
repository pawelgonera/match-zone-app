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

  constructor(private tokenService: TokenService, public router: Router) {
    //this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
  }

  ngOnInit() {
    this.info = {
      token: this.tokenService.getToken(),
      username: this.tokenService.getUsername(),
      authorities: this.tokenService.getAuthorities()
    };
  }


  logOut() {
    //localStorage.removeItem('currentUser');
    this.tokenService.signOut();
    this.router.navigate(['/login']);
  }

}
