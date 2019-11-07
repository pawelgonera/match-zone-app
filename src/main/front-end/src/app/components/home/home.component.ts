import { Component, OnInit } from '@angular/core';
import {NgForm} from "@angular/forms";
import {Filter} from "../../model/filter";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {PageUser} from "../../model/page-user";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  filter: Filter;
  pageUser: PageUser = new PageUser();

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
  }

  onSubmit(form: NgForm){

    console.log(form.value);

    let ageMin = 0;
    let ageMax = 0;
    let gender = 0;
    let city = '';
    this.pageUser.page = 0;
    this.pageUser.size = 6;
    this.pageUser.direction = 'ASC';
    this.pageUser.sort = 'firstName';

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
      gender = 0;
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
      city,
      1,
      6,
      this.pageUser
    );

    this.userService.getFilteredUserList(this.filter).subscribe(response => {
      console.log('filtered pageUser', response);
      this.pageUser.content = response.pageList;
      this.pageUser.totalPages = response.pageCount;
      this.router.navigate(['/users']);
    }, error => {
      console.log('filtered pageable error: ', error);
    });
  }


}
