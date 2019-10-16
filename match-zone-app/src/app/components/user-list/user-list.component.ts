import { Observable } from "rxjs";
import { UserService} from "../../service/user.service";
import { User} from "../../model/user";
import {Component, OnInit} from "@angular/core";
import {Router, RouterStateSnapshot} from '@angular/router';
import {NgForm} from "@angular/forms";
import {Filter} from "../../model/filter";
import {PageUser} from "../../model/page-user";
import {TokenService} from "../../service/token.service";

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
  isSubmitted = false;
  isLogged = false;

  constructor(private userService: UserService, private router: Router, private tokenService: TokenService) {

  }

  ngOnInit() {

    console.log('isSubmitted', this.isSubmitted);

    this.usernameFromToken = this.tokenService.getUsername();
    if(this.usernameFromToken){
      this.isLogged = true;
      this.getPageUser(0);
    }

    console.log('isLogged', this.isLogged);
  }

  reloadData() {
    this.users = this.userService.getUsersList();
  }

  onFilter(form: NgForm, page: number, sortOpt: NgForm){

    console.log(form.value);

    let name = '';
    let ageMin = 0;
    let ageMax = 0;
    let gender = 0;
    let city = '';
    let ratingMin = 0;
    let ratingMax = 0;
    this.pageUser.page = page;
    this.pageUser.size = 6;
    this.pageUser.direction = 'ASC';
    this.pageUser.sort = 'firstName';

    console.log('sortForm: ', sortOpt.value.select);

    if(sortOpt.value.select === 'Asc-name'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'ASC';
      this.pageUser.sort = 'firstName';
    }
    if(sortOpt.value.select === 'Desc-name'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'DESC';
      this.pageUser.sort = 'firstName';
    }
    if(sortOpt.value.select === 'Asc-age'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'ASC';
      this.pageUser.sort = 'personalDetails.age';
    }
    if(sortOpt.value.select === 'Desc-age'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'DESC';
      this.pageUser.sort = 'personalDetails.age';
    }
    if(sortOpt.value.select === 'Asc-rating'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'ASC';
      this.pageUser.sort = 'personalDetails.rating';
    }
    if(sortOpt.value.select === 'Desc-rating'){
      console.log('sortOpt: ', sortOpt.value);
      this.pageUser.direction = 'DESC';
      this.pageUser.sort = 'personalDetails.rating';
    }

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
      gender = 0;
    }

    if(form.value.city === null){
      city = '';
    }
    else {
      city = form.value.city;
    }

    if(form.value.ratingMin === '' || form.value.ratingMin === null){
      ratingMin = 0;
    }
    else {
      ratingMin = form.value.ratingMin;
    }

    if(form.value.ratingMax === '' || form.value.ratingMax === null){
      ratingMax = 0;
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
    }, error => {
      console.log('filtered pageable error: ', error);
    });
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
      this.isSubmitted = true;
    }, error => {
      console.log('filtered pageable error: ', error);
    });
  }

  getPageUser(page: number): void{
    this.userService.getPageUser(page).subscribe(data => {
      console.log("pageUser data: ", data);
      this.pageUser = data;
    }, error => {
      console.log('pageable error: ', error);
    })
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

  userDetails(username: string){
    this.router.navigate(['profile', username]);
  }


}
