import { Observable } from "rxjs";
import { UserService} from "../../service/user.service";
import { User} from "../../model/user";
import {Component, OnInit} from "@angular/core";
import { Router } from '@angular/router';
import {UserDetailsComponent} from "../user-details/user-details.component";
import {PersonalDetails} from "../../model/personal-details";
import {OtherService} from "../../service/other.service";
import {Appearance} from "../../model/appearance";
import {Vote} from "../../model/vote";

@Component({
  selector: "app-user-list",
  templateUrl: "./user-list.component.html",
  styleUrls: ["./user-list.component.css"],
})
export class UserListComponent implements OnInit {

  users: Observable<User[]>;
  personalDetails: PersonalDetails;
  appearance: Observable<Appearance>;
  vote: Observable<Vote>;


  constructor(private userService: UserService, private router: Router, private userDetailsComponent: UserDetailsComponent, private otherService: OtherService) {}

  ngOnInit() {





    this.reloadData();



  }

  loadPersonalDetails(id){
    return this.otherService.getPersonalDetails(id)
      .subscribe(data => {
          console.log(data);
          this.personalDetails = data;
        },
        error => console.log(error));
  }

  loadAppearance(id){
    return this.otherService.getAppearance(id)
      .subscribe(data => {
          console.log(data);
          this.appearance = data;
        },
        error => console.log(error));
  }

  loadVote(id){
    return   this.otherService.getVote(id)
      .subscribe(data => {
          console.log(data);
          this.vote = data;
        },
        error => console.log(error));
  }

  reloadData() {
    this.users = this.userService.getUsersList();
  }

  displayPhoto(user){
    //this.loadPersonalDetails(user);
    // this.personalDetails.photo;
  }

  displayCity(id){
    //this.loadPersonalDetails(id);
    return this.personalDetails.city;
  }

  displayRating(user){
    //return this.vote = this.otherService.getVote(user.id);
  }

  displayAbout(user){
    //return this.appearance = this.otherService.getAppearance(user.id);
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

    this.personalDetails = this.otherService.getPersonalDetails(user.id);

    return this.userDetailsComponent.calculateAge(this.personalDetails);
  }

}
