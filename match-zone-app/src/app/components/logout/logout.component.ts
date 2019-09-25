import { Component, OnInit } from '@angular/core';
import {User} from "../../model/user";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../service/authentication.service";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  user: User = new User();
  errorMessage: string;

  constructor(private authService: AuthenticationService, private router: Router) { }

  ngOnInit() {
  }

  login(){
    this.authService.authenticate(this.user, (e) =>{
      this.router.navigateByUrl('/profile');
      console.log(e);
      let response: any;
      response = e.principal;
      if(response){
        localStorage.setItem('currentUser', JSON.stringify(response));
      }
    });
  }
}
