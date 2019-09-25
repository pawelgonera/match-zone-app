import {Component, ViewEncapsulation} from '@angular/core';
import {Router} from "@angular/router";
import {User} from "./model/user";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent {
  title = 'match-zone-app';

  currentUser: User;

  constructor( public router: Router) {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
  }

  logOut() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/logout']);
  }

}
