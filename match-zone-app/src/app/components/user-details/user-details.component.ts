import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from "../../model/user";
import {UserService} from "../../service/user.service";
import {HttpClient, HttpRequest, HttpResponse} from "@angular/common/http";
import {PersonalDetails} from "../../model/personal-details";
import {Appearance} from "../../model/appearance";
import {Vote} from "../../model/vote";
import {OtherService} from "../../service/other.service";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css'],
})
export class UserDetailsComponent implements OnInit {

  id: number;
  user: User = new User();
  updated = false;
  isChangeDataFailed = false;
  errorMessage = '';

  personalDetails: PersonalDetails = new PersonalDetails();
  appearance: Appearance = new Appearance();
  vote: Vote = new Vote();

  timeDifference: number;
  age: number;

  selectedFiles: FileList;
  selectedFile: File = null;

  currentRate = 0;

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private http: HttpClient, private otherService: OtherService) {
  }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];

    this.getUser();

    this.loadPersonalDetails(this.id);
    this.loadAppearance(this.id);
    this.loadVote(this.id);

    this.reloadData(this.id);
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

  update() {
    this.id = this.route.snapshot.params['id'];
    this.userService.getUser(this.id)
      .subscribe(data => {
        console.log(data);
        this.user = data;
      }, error => console.log(error));


    this.userService.updateUser(this.id, this.user)
      .subscribe(data => {
        console.log(data);
        this.isChangeDataFailed = false;
      }, error => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.isChangeDataFailed = true;
      });

    this.otherService.updatePersonalDetails(this.id, this.personalDetails)
      .subscribe(data => console.log(data), error => console.log(error));

    this.otherService.updateAppearance(this.id, this.appearance)
      .subscribe(data => console.log(data), error => console.log(error));

    this.reloadData(this.id);
  }

  onSubmit() {
    this.updated = true;
    this.update();
  }

  onFileSelected(event){
    this.selectedFiles = event.target.files;
    console.log(event);
    console.log(this.selectedFile);
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

  public calculateAge(personalDetails): number {
    this.timeDifference = Math.abs(Date.now() - new Date(personalDetails.dateOfBirth).getTime());
    this.age = Math.floor(this.timeDifference / (1000 * 3600 * 24) / 365.25);

    return this.age;
  }

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
        console.log('user from data', data);
        this.user = data;
      }, error => console.log(error));

  }

  onVote(vote: Vote){
    let votes = vote.countedVotes;
    let sum = vote.sumOfVotes;
    votes++;
    sum += this.currentRate;
    console.log(sum);
    vote.rating = sum / votes;
    vote.countedVotes = votes;
    vote.sumOfVotes = sum;

    this.otherService.updateVote(this.id, vote)
      .subscribe(data => console.log(data), error => console.log(error));

  }

}
