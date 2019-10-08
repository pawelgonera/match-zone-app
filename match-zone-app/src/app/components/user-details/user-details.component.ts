import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from "../../model/user";
import {UserService} from "../../service/user.service";
import {HttpClient, HttpRequest, HttpResponse} from "@angular/common/http";
import {PersonalDetails} from "../../model/personal-details";
import {Appearance} from "../../model/appearance";
import {Vote} from "../../model/vote";
import {OtherService} from "../../service/other.service";
import {TokenService} from "../../service/token.service";
import { filter } from 'rxjs/operators';
import {Observable} from 'rxjs/'
import {Rating} from "../../model/rating";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css'],
})
export class UserDetailsComponent implements OnInit{

  id: number;
  user: User = new User();
  updated = false;
  isChangeDataFailed = false;
  errorMessage = '';

  usernameFromToken: string;
  usernameFromSnapshot: string;
  allowAccess = false;

  personalDetails: PersonalDetails = new PersonalDetails();
  appearance: Appearance = new Appearance();
  vote: Vote = new Vote();

  selectedFiles: FileList;
  selectedFile: File = null;

  currentRate = 0;

  isVoted = false;
  rating: Rating = new Rating();

  username: string;

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService,
              private http: HttpClient, private otherService: OtherService, private tokenService: TokenService) {
  }

  ngOnInit() {

    this.id = this.route.snapshot.params['id'];

    this.getUser();

    this.usernameFromToken = this.tokenService.getUsername();

    this.loadPersonalDetails(this.id);
    this.loadAppearance(this.id);

    this.otherService.checkIfLoggedUserVoted(this.id, this.usernameFromToken).subscribe((isvoted: boolean) => {
      this.isVoted = isvoted;
      console.log('isVotedIn', this.isVoted);
    });
    console.log('isVotedOut', this.isVoted);

    this.reloadData(this.id);
  }

  loadPersonalDetails(id){
    return this.otherService.getPersonalDetails(id)
      .subscribe(data => {
          console.log('personalDetails: ', data);
          this.personalDetails = data;
        },
        error => console.log(error));
  }

  loadAppearance(id){
    return this.otherService.getAppearance(id)
      .subscribe(data => {
          console.log('appearance: ', data);
          this.appearance = data;
        },
        error => console.log(error));
  }

  update() {
    this.id = this.route.snapshot.params['id'];
    this.userService.getUser(this.id)
      .subscribe(data => {
        console.log('userToUpdate: ', data);
        this.user = data;
      }, error => console.log(error));


    this.userService.updateUser(this.id, this.user)
      .subscribe(data => {
        console.log('updatedUser: ', data);
        this.isChangeDataFailed = false;
      }, error => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.isChangeDataFailed = true;
      });

    this.otherService.updatePersonalDetails(this.id, this.personalDetails)
      .subscribe(data => console.log('updatedPersonalDetails: ', data), error => console.log(error));

    this.otherService.updateAppearance(this.id, this.appearance)
      .subscribe(data => console.log('updatedAppearance: ', data), error => console.log(error));

    this.reloadData(this.id);
  }

  onSubmit() {
    this.updated = true;
    this.update();
  }

  onFileSelected(event){
    this.selectedFiles = event.target.files;
    console.log('event(file): ', event);
    console.log('selectedFiles:', this.selectedFile);
  }

  onUpload(){
    this.id = this.route.snapshot.params['id'];
    this.selectedFile = this.selectedFiles.item(0);

    const formData: FormData = new FormData();
    formData.append('file', this.selectedFile);
    const req = new HttpRequest('POST', 'http://localhost:8080/match-zone/api/v1/users/' + this.id + '/change-avatar', formData, {
        reportProgress: true,
        responseType: 'text'
      }
    );

    this.http.request(req).subscribe(
      event=>{
        if(event instanceof HttpResponse){
          console.log('File is completely uploaded!')
        }
      }
    );

    this.reloadData(this.id);
  }

  /*public calculateAge(personalDetails): number {
    this.timeDifference = Math.abs(Date.now() - new Date(personalDetails.dateOfBirth).getTime());
    this.age = Math.floor(this.timeDifference / (1000 * 3600 * 24) / 365.25);

    return this.age;
  }*/

  reloadData(id: number) {
    this.router.navigate(['profile', id]);
  }

  list(){
    this.router.navigate(['users']);
  }

  getUser(){
    this.id = this.route.snapshot.params['id'];
    return this.userService.getUser(this.id)
      .subscribe(data => {
        this.user = data;
        this.usernameFromSnapshot = this.user.username;
        if(this.usernameFromToken){
          if(this.usernameFromToken === this.usernameFromSnapshot){
            this.allowAccess = true;
            console.log('ACCESS: ', this.allowAccess);
          }
        }
      }, error => console.log(error));

  }

  onVote(vote: Vote){

    vote.value = this.currentRate;
    vote.author = this.usernameFromToken;

    this.otherService.addVote(this.id, vote)
      .subscribe(data => {
        console.log('addedVote: ', data);
      }, err => {
        console.log(err);
      });

    this.otherService.getRatingInfo(this.id).subscribe( rating => {
      this.rating = rating;
      console.log('ratingInfo: ', rating);

      this.rating.countedVotes++;
      this.rating.sumOfVotes += this.currentRate;
      this.personalDetails.rating = this.rating.sumOfVotes / this.rating.countedVotes;

      this.otherService.updatePersonalDetails(this.id, this.personalDetails)
        .subscribe(data => console.log('updatedPersonalDetailsForRating: ', data), error => console.log(error));
    }, error => console.log(error));

    this.reloadData(this.id);

  }

}
