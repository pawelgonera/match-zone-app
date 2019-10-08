import { Observable } from "rxjs";
import { UserService} from "../../service/user.service";
import { User} from "../../model/user";
import {Component, OnInit} from "@angular/core";
import { Router } from '@angular/router';
import {NgForm} from "@angular/forms";
import {Filter} from "../../model/filter";

@Component({
  selector: "app-user-list",
  templateUrl: "./user-list.component.html",
  styleUrls: ["./user-list.component.css"],
})
export class UserListComponent implements OnInit {

  users: Observable<User[]>;
  filter: Filter;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit() {


    this.reloadData();
  }

  reloadData() {
    this.users = this.userService.getUsersList();
  }

  onSubmit(form: NgForm){

    console.log(form.value);

    let name = '';
    let ageMin = 0;
    let ageMax = 0;
    let gender = 2;
    let city = '';

    if(form.value.name === null){
      name = '';
    }
    else {
      name = form.value.name;
    }

    if(form.value.ageMin === '' || form.value.ageMin === null){
      ageMin = 0;
    }
    else {
      ageMin = form.value.ageMin;
    }

    if(form.value.ageMax === '' || form.value.ageMax === null){
      ageMax = 0;
    }
    else {
      ageMax = form.value.ageMax;
    }

    if(form.value.gender !== ''){
      gender = form.value.gender;
    }
    if(form.value.gender === null){
      gender = 2;
    }

    if(form.value.city === null){
      city = '';
    }
    else {
      city = form.value.city;
    }

    this.filter = new Filter(
      name,
      gender,
      ageMin,
      ageMax,
      city
    );

    this.users = this.userService.getFilteredUserList(this.filter);
      /*.subscribe( data => {
        console.log('filter: ', data);
        this.users = data;
      }, error => {
        console.log(error);
      });*/
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

  userDetails(id: number){
    this.router.navigate(['profile', id]);
  }

}
