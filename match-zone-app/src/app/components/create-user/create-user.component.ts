import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import { Router } from "@angular/router";
import { UserService } from "../../service/user.service";
import { User } from "../../model/user";
import {PersonalDetails} from "../../model/personal-details";
import {Appearance} from "../../model/appearance";
import {Vote} from "../../model/vote";


@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CreateUserComponent implements OnInit {

  user: User = new User();
  errorMessage: string;
  personalDetails: PersonalDetails = new PersonalDetails();
  appearance: Appearance = new Appearance();
  vote: Vote = new Vote();
  submitted = false;

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
    this.user.personalDetails = this.personalDetails;
    this.user.appearance = this.appearance;
    this.user.vote = this.vote;
  }

  save() {
    this.userService.createUser(this.user)
      .subscribe(data =>{
        console.log(data)
        },error => {
        console.log(error);
        this.errorMessage = 'Username already exist';
      });

    this.gotoLogin();
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

  gotoLogin() {
    this.router.navigate(['login']);
  }

  parseDate(dateString: string): Date {
    if (dateString) {
      return new Date(dateString);
    }
    return null;
  }
}
