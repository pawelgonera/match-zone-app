import {Component, OnInit} from '@angular/core';
import { Router } from "@angular/router";
import { UserService } from "../../service/user.service";
import { User } from "../../model/user";
import {Register} from "../../model/register";
import {UserDetailsComponent} from "../user-details/user-details.component";


@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {

  user: User = new User();
  submitted = false;
  form: any = {};
  register: Register;
  isSignedUp = false;
  isSignUpFailed = false;
  errorMessage = '';
  age: number;

  constructor(private userService: UserService, private router: Router, private userDetailsComponent: UserDetailsComponent) { }

  ngOnInit() {

  }

  save() {
    console.log(this.form);

    this.register = new Register(
      this.form.username,
      this.form.name,
      this.form.email,
      this.form.password,
      this.form.repeatedPassword,
      this.form.dateOfBirth,
      this.age = this.calculateAge(this.form.dateOfBirth)
      );

    this.userService.createUser(this.register)
      .subscribe(data => {
        console.log(data);
        this.router.navigate(['./login']);
        this.isSignUpFailed = false;
      },
      error => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.isSignUpFailed = true;
      }
    );
  }

  public calculateAge(dateOfBirth): number {

    let timeDifference = Math.abs(Date.now() - new Date(dateOfBirth).getTime());
    return Math.floor(timeDifference / (1000 * 3600 * 24) / 365.25);
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

}
