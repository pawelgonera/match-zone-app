import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { UserService } from "../user.service";
import { User } from "../user";
import {PersonalDetails} from "../personal-details";
import {Appearance} from "../appearance";


@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {

  user: User = new User();
  personalDetails: PersonalDetails = new PersonalDetails();
  appearance: Appearance = new Appearance();
  submitted = false;

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
    this.user.personalDetails = this.personalDetails;
    this.user.appearance = this.appearance;
  }

  save() {
    this.userService.createUser(this.user)
      .subscribe(data => console.log(data), error => console.log(error));
    //this.user = new User();
    this.gotoList();
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

  gotoList() {
    this.router.navigate(['/users']);
  }

  parseDate(dateString: string): Date {
    if (dateString) {
      return new Date(dateString);
    }
    return null;
  }
}
