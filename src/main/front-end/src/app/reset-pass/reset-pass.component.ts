import { Component, OnInit } from '@angular/core';
import {TokenService} from "../service/token.service";
import {Router} from "@angular/router";
import {OtherService} from "../service/other.service";

@Component({
  selector: 'app-reset-pass',
  templateUrl: './reset-pass.component.html',
  styleUrls: ['./reset-pass.component.css']
})
export class ResetPassComponent implements OnInit {

  constructor(private router: Router, private tokenService: TokenService, private otherService: OtherService) { }
  isLoggedIn = false;
  email: string;
  isResetFailed = true;
  errorMessage = '';

  ngOnInit() {
    if (this.tokenService.getToken()) {
      this.isLoggedIn = true;
    }

    if(this.isLoggedIn)
    {
      this.router.navigate(['/home']);
    }
  }

  resetPass(){
    const formData: FormData = new FormData();
    formData.append('email', this.email);
    this.otherService.resetPassword(formData).subscribe( data => {
      console.log('resetPass', data);
      this.isResetFailed = false;
    },  error => {
        console.log('resetPassErr', error);
        this.isResetFailed = true;
        let message = error.error.split(":")[4];
        this.errorMessage = message.slice(1, message.length);
    })
  }

}
