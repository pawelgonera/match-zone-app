import { Observable } from "rxjs";
import { UserService} from "../user.service";
import { User} from "../user";
import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';

@Component({
  selector: "app-user-list",
  templateUrl: "./user-list.component.html",
  styleUrls: ["./user-list.component.css"]
})
export class UserListComponent implements OnInit {

  users: Observable<User[]>;

  birthDate: Date;
  timeDifference: number;
  age: number;

  constructor(private userService: UserService,
              private router: Router) {}

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    this.users = this.userService.getUsersList();
  }

  deleteUser(id: number) {
    this.userService.deleteUser(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  updateUser(id: number){
    this.router.navigate(['edit', id]);
  }

  userDetails(id: number){
    this.router.navigate(['profile', id]);
  }

  public calculateAge(user: User): number {
    this.birthDate = user.personalDetails.dateOfBirth;
    if (this.birthDate) {
      this.timeDifference = Math.abs(Date.now() - new Date(this.birthDate).getTime());
      this.age = Math.floor(this.timeDifference / (1000 * 3600 * 24) / 365.25);
    }
    return this.age;
  }

}
