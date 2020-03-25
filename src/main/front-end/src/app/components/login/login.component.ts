import {Component, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {AuthenticationService} from "../../service/authentication.service";
import {Router} from "@angular/router";
import {Login} from "../../model/login";
import {TokenService} from "../../service/token.service";


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

  token: string;
  message: string = null;
  expired: boolean = false;

  pinkHeartPath: any = "../assets/images/pink-heart.png";

  constructor(private authService: AuthenticationService, private router: Router, private tokenService: TokenService) { }

  ngOnInit() {

    this.token = this.tokenService.getToken();
    if (this.token) {
      this.message = null;
      console.log('=== Jestem w LoginComponent.ngOnInit po sprawdzeniu tokenu ===', new Date());

      this.isLoggedIn = true;
      this.roles = this.tokenService.getAuthorities();
      this.router.navigate(['/home']);
    }

    if(this.tokenService.getLoggedOut() == 'true'){
      console.log('loggedOut', this.tokenService.getLoggedOut());
      this.message = 'User has been logged out';
      console.log('message expired', this.message);
      this.tokenService.setLoggedOut('false');
      window.location.reload();
    }
  }

  logIn() {
    console.log(this.form);

    this.login = new Login(
      this.form.username,
      this.form.password);

    console.log('=== Jestem w LoginComponent.logIn przed wysłaniem zapytania do /users/login/ ===', new Date());

    this.authService.authenticate(this.login)
      .subscribe(data => {
        console.log(data);

        console.log('=== Jestem w LoginComponent.logIn przed zapisaniem tokena, username i authorities ===', new Date());

        this.tokenService.saveToken(data.token);
        this.tokenService.saveUsername(data.username);
        this.tokenService.saveAuthorities(data.authorities);
        this.tokenService.setLoggedOut('false');
        this.isLoginFailed = false;
        this.isLoggedIn = true;

        console.log('=== Jestem w LoginComponent.logIn przed pobraniem uprawnień ==='), new Date();

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
