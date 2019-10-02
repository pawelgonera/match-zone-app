import {Component, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {AuthenticationService} from "../../service/authentication.service";
import {Router} from "@angular/router";
import {Login} from "../../model/login";
import {TokenService} from "../../service/token.service";
import {AppComponent} from "../../app.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: User = new User();
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  private login: Login;


  constructor(private authService: AuthenticationService, private router: Router, private tokenService: TokenService) { }

  ngOnInit() {
    if (this.tokenService.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenService.getAuthorities();
      this.router.navigate(['/users']);
    }
  }

  logIn() {
    console.log(this.form);

    this.login = new Login(
      this.form.username,
      this.form.password);

    this.authService.authenticate(this.login)
      .subscribe(data => {
        console.log(data);
        this.tokenService.saveToken(data.token);
        this.tokenService.saveUsername(data.username);
        this.tokenService.saveAuthorities(data.authorities);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenService.getAuthorities();
        this.reloadData();

      },
      error => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.isLoginFailed = true;
      }
    );

  }

  reloadData() {
    window.location.reload();
  }
}
