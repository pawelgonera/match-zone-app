import { Component, OnInit } from '@angular/core';
import {User} from "../../model/user";
import {ChangePasswordForm} from "../../model/change-password-form";
import {ChangeEmailForm} from "../../model/change-email-form";
import {PersonalDetails} from "../../model/personal-details";
import {Appearance} from "../../model/appearance";
import {OtherService} from "../../service/other.service";
import {UserService} from "../../service/user.service";
import {TokenService} from "../../service/token.service";
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-setting',
  templateUrl: './setting.component.html',
  styleUrls: ['./setting.component.css']
})
export class SettingComponent implements OnInit {

  user: User = new User();
  username: string;
  success = '0';
  isChangeDataFailed = true;
  errorMessage = '';
  submitted = false;

  minDate = new Date(new Date().getFullYear() - 75, new Date().getMonth(), new Date().getDay());
  maxDate = new Date(new Date().getFullYear() - 18, new Date().getMonth(), new Date().getDay());

  isReadyToDisplay = false;

  personalDetails: PersonalDetails = new PersonalDetails();
  appearance: Appearance = new Appearance();

  passwordForm: ChangePasswordForm = new ChangePasswordForm();
  emailForm: ChangeEmailForm = new ChangeEmailForm();

  constructor(private userService: UserService, private otherService: OtherService, private tokenService: TokenService,
              private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {

    this.username = this.tokenService.getUsername();

    this.loadUser(this.username);
    this.loadPersonalDetails(this.user);
    this.loadAppearance(this.user);

  }

  loadUser(username: string){
    return this.userService.getUser(username)
      .subscribe(data => {
        console.log('user: ', data);
        this.user = data;
        this.isReadyToDisplay = true;
      }, error => {
          console.log(error);
          this.isReadyToDisplay = false;
      });
  }

  loadPersonalDetails(user: User){
    return this.otherService.getPersonalDetails(user.id)
      .subscribe(data => {
          console.log('personalDetails: ', data);
          this.personalDetails = data;
          this.isReadyToDisplay = true;
        }, error => {
          console.log(error);
          this.isReadyToDisplay = false;
        });
  }

  loadAppearance(user: User){
    return this.otherService.getAppearance(user.id)
      .subscribe(data => {
          console.log('appearance: ', data);
          this.appearance = data;
          this.isReadyToDisplay = true;
        }, error => {
          console.log(error);
          this.isReadyToDisplay = false;
        });
  }

  update() {
    window.scroll(0,0);
    this.submitted = true;
    console.log('user: ', this.user);
    this.success = '';
    this.userService.updateUser(this.username, this.user)
      .subscribe(data => {
        console.log('updatedUser: ', data);
        this.isChangeDataFailed = false;
        this.success = 'Data is successfully changed';
      }, error => {
        console.log(error);
        this.errorMessage = error.error.errorMessage;
        this.isChangeDataFailed = true;
        this.success = '0';
      });

    this.personalDetails.age = this.calculateAge(this.personalDetails.dateOfBirth);
    console.log('dateOfBirth: ', this.personalDetails.dateOfBirth);
    console.log('age: ', this.personalDetails.age);
    console.log('personalDetailsUpdate: ', this.personalDetails);

    this.otherService.updatePersonalDetails(this.user.id, this.personalDetails)
      .subscribe(data => console.log('updatedPersonalDetails: ', data), error => console.log(error));

    this.otherService.updateAppearance(this.user.id, this.appearance)
      .subscribe(data => console.log('updatedAppearance: ', data), error => console.log(error));


    //this.reloadData();
  }

  public calculateAge(dateOfBirth): number {
    let timeDifference = Math.abs(Date.now() - new Date(dateOfBirth).getTime());
    return Math.floor(timeDifference / (1000 * 3600 * 24) / 365.25);
  }

  changePassword(){
    this.submitted = true;
    this.passwordForm.username = this.username;
    this.userService.changePassword(this.passwordForm)
    .subscribe(data => {
      this.isChangeDataFailed = false;
      this.success = 'Password is successfully changed';
      //window.location.reload();
      //this.ngOnInit();
    }, error => {
      console.log(error);
      this.errorMessage = error.error.errorMessage;
      this.isChangeDataFailed = true;
      this.success = '0';
    });
  }

   reloadData() {
    this.router.navigate(['/setting']);
  }

  changeEmail(){
    this.submitted = true;
    this.emailForm.username = this.username;
    this.userService.changeEmail(this.emailForm)
    .subscribe(data => {
      this.isChangeDataFailed = false;
      this.success = 'Email is successfully changed';
    }, error => {
      console.log(error);
      this.errorMessage = error.error.errorMessage;
      this.isChangeDataFailed = true;
      this.success = '0';
    });
  }

}
