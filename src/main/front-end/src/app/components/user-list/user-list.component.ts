import { Observable } from "rxjs";
import { UserService} from "../../service/user.service";
import { User} from "../../model/user";
import {Component, OnInit} from "@angular/core";
import {Router, RouterStateSnapshot, ActivatedRoute} from '@angular/router';
import {NgForm} from "@angular/forms";
import {Filter} from "../../model/filter";
import {FilterParams} from "../../model/filter-params";
import {PageUser} from "../../model/page-user";
import {TokenService} from "../../service/token.service";
import {HttpParams} from "@angular/common/http";
import {Gender} from "../../model/enum/gender";

@Component({
  selector: "app-user-list",
  templateUrl: "./user-list.component.html",
  styleUrls: ["./user-list.component.css"],
})
export class UserListComponent implements OnInit {

  users: Observable<User[]>;
  pageUser: PageUser = new PageUser();
  selectedPage: number = 0;
  filter: Filter;

  user: User = new User();
  usernameFromToken: string;
  isLogged = false;

  isReadyToDisplay = false;

  index: number = 0;
  sortParam: string;

  constructor(private route: ActivatedRoute, private userService: UserService, private router: Router, private tokenService: TokenService) {

  }

  ngOnInit() {
    this.usernameFromToken = this.tokenService.getUsername();
    if(this.usernameFromToken){
      this.isLogged = true;
    }

    console.log('isLogged', this.isLogged);
    this.displayUsers();
  }

  reloadData() {
    this.users = this.userService.getUsersList();
  }

  reloadPage(){
    this.router.navigate(['users']);
  }

  displayUsers(){
    let name;
    let gender;
    let ageMin;
    let ageMax;
    let city;
    let ratingMin;
    let ratingMax;
    this.pageUser.page;
    this.pageUser.size = 12;
    this.pageUser.direction;
    this.pageUser.sort;

   this.route.queryParams.subscribe(params => {
        name = params['name'];
        gender = params['gender'];
        ageMin = params['ageMin'];
        ageMax = params['ageMax'];
        city = params['city'];
        ratingMin = params['ratingMin'];
        ratingMax = params['ratingMax'];
        this.pageUser.page = params['page'];
        this.index = this.pageUser.page;
        if(params['sort'] != null){
          this.pageUser.direction = params['sort'].split('_')[1];
          this.pageUser.sort = params['sort'].split('_')[0];
        }
        this.sortParam = params['sort'];
        console.log('params subscribe: ', params);
    });

    if(name == null){
      name = '';
    }

    if(ageMin == '' || ageMin == null || ageMin == 0){
      ageMin = 18;
    }

    if(ageMax == '' || ageMax == null || ageMax == 0){
      ageMax = 75;
    }

    if(ratingMin == '' || ratingMin == null || ratingMin == 0){
      ratingMin = 1;
    }

    if(ratingMax == '' || ratingMax == null || ratingMax == 0){
      ratingMax = 6;
    }

    if(gender == null || gender == ''){
      gender = Gender.UNDEFINED;
    }

    if(city == null){
      city = '';
    }

    if(this.pageUser.direction == null || this.pageUser.direction == ''){
      this.pageUser.direction = 'ASC';
    }
    if(this.pageUser.sort == null || this.pageUser.sort == ''){
      this.pageUser.sort = 'firstName';
    }

    if(this.pageUser.sort != 'firstName'){
      this.pageUser.sort = 'personalDetails.' + this.pageUser.sort;
    }

    this.filter = new Filter(
      name,
      gender,
      ageMin,
      ageMax,
      city,
      ratingMin,
      ratingMax,
      this.pageUser
    );

    console.log('filter: ', this.filter);

    this.userService.getFilteredUserList(this.filter).subscribe(response => {
      console.log('filtered pageUser', response);
      this.pageUser.content = response.pageList;
      this.pageUser.totalPages = response.pageCount;
      this.isReadyToDisplay = true;
    }, error => {
      console.log('filtered pageable error: ', error);
    });

  }

  onFilter(form: NgForm, page: number, sortOpt: NgForm){
    this.isReadyToDisplay = false;

    console.log('form', form.value);

    let name = '';
    let ageMin = 18;
    let ageMax = 75;
    let gender: Gender;
    let city = '';
    let ratingMin = 1;
    let ratingMax = 6;
    this.pageUser.page = page;
    this.index = page;
    this.pageUser.size = 12;
    this.pageUser.direction = 'ASC';
    this.pageUser.sort = 'firstName';

    console.log('sortForm: ', sortOpt.value.sortParam);

    if(sortOpt.value.sortParam == 'firstName_ASC'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'ASC';
      this.pageUser.sort = 'firstName';
    }
    if(sortOpt.value.sortParam == 'firstName_DESC'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'DESC';
      this.pageUser.sort = 'firstName';
    }
    if(sortOpt.value.sortParam == 'age_ASC'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'ASC';
      this.pageUser.sort = 'personalDetails.age';
    }
    if(sortOpt.value.sortParam == 'age_DESC'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'DESC';
      this.pageUser.sort = 'personalDetails.age';
    }
    if(sortOpt.value.sortParam == 'rating_ASC'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'ASC';
      this.pageUser.sort = 'personalDetails.rating';
    }
    if(sortOpt.value.sortParam == 'rating_DESC'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'DESC';
      this.pageUser.sort = 'personalDetails.rating';
    }

    if(sortOpt.value.name == null){
      name = '';
    }
    else {
      name = sortOpt.value.name;
    }

    if(form.value.ageMin == '' || form.value.ageMin === null || form.value.ageMin === 0){
      ageMin = 18;
    }
    else {
      ageMin = form.value.ageMin;
    }

    if(form.value.ageMax == '' || form.value.ageMax === null || form.value.ageMax === 0){
      ageMax = 75;
    }
    else {
      ageMax = form.value.ageMax;
    }

    if(form.value.gender !== null){
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

    if(form.value.ratingMin == '' || form.value.ratingMin == null || form.value.ratingMin == 0){
      ratingMin = 1;
    }
    else {
      ratingMin = form.value.ratingMin;
    }

    if(form.value.ratingMax == '' || form.value.ratingMax == null || form.value.ratingMax == 0){
      ratingMax = 6;
    }
    else {
      ratingMax = form.value.ratingMax;
    }

    this.filter = new Filter(
      name,
      gender,
      ageMin,
      ageMax,
      city,
      ratingMin,
      ratingMax,
      this.pageUser
    );

    this.userService.getFilteredUserList(this.filter).subscribe(response => {
      console.log('filtered pageUser', response);
      this.pageUser.content = response.pageList;
      this.pageUser.totalPages = response.pageCount;
      this.isReadyToDisplay = true;
    }, error => {
      console.log('filtered pageable error: ', error);
    });

    let sortValue = this.pageUser.sort;
    if(this.pageUser.sort.split('.')[1] != null){
      sortValue = this.pageUser.sort.split('.')[1];
    }
    let sort = sortValue + '_' + this.pageUser.direction;

    let filterParams: FilterParams = new FilterParams();

    console.log('params: ', 'name:' + name + ' gender: ' + gender + ' ageMin: ' + ageMin + ' ageMax: ' + ageMax + ' ratingMin: ' + ratingMin + ' ratingMax: ' + ratingMax + ' city: ' + city);

    if(name != ''){
      filterParams.setName(name);
    }
    if(gender != 0 || gender != null){
      filterParams.setGender(gender);
    }
    if(ageMin != 0 || ageMin != null){
      filterParams.setAgeMin(ageMin);
    }
    if(ageMax != 0 || ageMax != null){
      filterParams.setAgeMax(ageMax);
    }
    if(ratingMin != 0 || ratingMin != null){
      filterParams.setRatingMin(ratingMin);
    }
    if(ratingMax != 0 || ratingMax != null){
      filterParams.setRatingMax(ratingMax);
    }
    if(city != ''){
      filterParams.setCity(city);
    }
    if(sortOpt.value.sortParam != null){
      filterParams.setSort(sort);
    }

    filterParams.setPage(this.pageUser.page);

    console.log('filterParams: ', filterParams);

    this.router.navigate(['users'], { queryParams: filterParams });

    window.scroll(0,0);
  }
  isFirstPage(page: number){
    if(page < 0){
      this.router.navigate(['not-found']);
    }

    return page <= 0;
  }

  isLastPage(page: number){
    if(page > this.pageUser.totalPages-1){
      this.router.navigate(['not-found']);
    }

    return page >= this.pageUser.totalPages-1;
  }

  incrementPage(){
    if(this.pageUser.page < this.pageUser.totalPages){
      this.index++;
    }
  }

  decrementPage() {
    if (this.index > 0){
      this.index--;
    }
  }

  getPage(page: NgForm){
    console.log("page from form: ", page.value.page);
    this.index = page.value.page;
  }

  userDetails(username: string){
    this.router.navigate(['profile', username]);
  }


}
