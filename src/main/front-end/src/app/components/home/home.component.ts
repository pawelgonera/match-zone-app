import { Component, OnInit } from '@angular/core';
import {HttpParams} from '@angular/common/http';
import {NgForm} from "@angular/forms";
import {Filter} from "../../model/filter";
import {FilterParams} from "../../model/filter-params";
import {UserService} from "../../service/user.service";
import {Router, Params} from "@angular/router";
import {PageUser} from "../../model/page-user";
import {Gender} from "../../model/enum/gender";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  filter: Filter;
  pageUser: PageUser = new PageUser();

  mainPhoto: any = "../assets/images/main-photo.jpg";

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
  }

  onSubmit(form: NgForm){
    console.log(form.value);

    let name = '';
    let ageMin = 0;
    let ageMax = 0;
    let gender: Gender;
    let city = '';
    let ratingMin = 0;
    let ratingMax = 0;
    this.pageUser.page = 0;
    this.pageUser.size = 12;
    //this.pageUser.direction = 'ASC';
    //this.pageUser.sort = 'firstName';

    if(form.value.ageMin == '' || form.value.ageMin == null || form.value.ageMin == 0){
      ageMin = 0;
    }
    else {
      ageMin = form.value.ageMin;
    }

    if(form.value.ageMax == '' || form.value.ageMax == null || form.value.ageMax == 0){
      ageMax = 0;
    }
    else {
      ageMax = form.value.ageMax;
    }

    if(form.value.ratingMin === '' || form.value.ratingMin === null || form.value.ratingMin === 0){
      ratingMin = 0;
    }
    else {
      ratingMin = form.value.ratingMin;
    }

    if(form.value.ratingMax === '' || form.value.ratingMax === null || form.value.ratingMax === 0){
      ratingMax = 0;
    }
    else {
      ratingMax = form.value.ratingMax;
    }

    if(form.value.gender != ''){
      gender = form.value.gender;
    }
    if(form.value.gender == null){
      gender = Gender.UNDEFINED;
    }

    if(form.value.city == null){
      city = '';
    }
    else {
      city = form.value.city;
    }

    //let sort = this.pageUser.sort + '_' + this.pageUser.direction;

    let filterParams: FilterParams = new FilterParams();

    console.log('params: ', 'name:' + name + ' gender: ' + gender + ' ageMin: ' + ageMin + ' ageMax: ' + ageMax + ' ratingMin: ' + ratingMin + ' ratingMax: ' + ratingMax + ' city: ' + city);

    if(name != ''){
      filterParams.setName(name);
    }
    if(gender != Gender.UNDEFINED){
      filterParams.setGender(gender);
    }
    if(ageMin >= 18){
      filterParams.setAgeMin(ageMin);
    }
    if(ageMax >= 18 && ageMax <= 75){
      filterParams.setAgeMax(ageMax);
    }
    if(ratingMin >= 1){
      filterParams.setRatingMin(ratingMin);
    }
    if(ratingMax >=1 && ratingMax <= 6){
      filterParams.setRatingMax(ratingMax);
    }
    if(city != ''){
      filterParams.setCity(city);
    }
    //filterParams.setSort(sort);

    filterParams.setPage(this.pageUser.page);

    console.log('filterParams: ', filterParams);

    this.router.navigate(['users'], { queryParams: filterParams });

  }


}
