import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from "../../model/user";
import {UserService} from "../../service/user.service";
import {HttpClient, HttpEventType, HttpResponse} from "@angular/common/http";
import {PersonalDetails} from "../../model/personal-details";
import {Appearance} from "../../model/appearance";
import {Comment} from "../../model/comment";
import {Vote} from "../../model/vote";
import {OtherService} from "../../service/other.service";
import {TokenService} from "../../service/token.service";
import {Rating} from "../../model/rating";
import {NgForm} from "@angular/forms";

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

  comment: Comment = new Comment();
  comments: Comment[];
  commentAvatar: any;
  authorDetails: PersonalDetails = new PersonalDetails();
  postId: number = 0;

  selectedFiles: FileList;
  selectedFile: File = null;
  fileUploadProgress: string = null;
  fileErrorMessage: string;

  currentRate = 0;

  isVoted = true;
  rating: Rating = new Rating();

  isLogged = false;

  username: string;

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService,
              private http: HttpClient, private otherService: OtherService, private tokenService: TokenService) {
  }

  ngOnInit() {

    this.username = this.route.snapshot.params['username'];

    console.log('=== Jestem w UserDetailsComponent.ngOnInit przed pobraniem username z tokenService ==='), new Date();

    this.usernameFromToken = this.tokenService.getUsername();

    this.otherService.checkIfLoggedUserVoted(this.username, this.usernameFromToken).subscribe((isVoted: boolean) => {
      this.isVoted = isVoted;
      console.log('isVotedIn', this.isVoted);
    });
    console.log('isVotedOut', this.isVoted);

    if(this.usernameFromToken) {
      this.isLogged = true;
    }

    this.getAccess();

    this.loadPersonalDetails(this.username);
    this.loadAppearance(this.username);
    this.loadComments(this.username);

    window.scroll(0,0);

    this.reloadData(this.username);
  }

  loadPersonalDetails(username: string){

    console.log('=== Jestem w UserDetailsComponent.loadPersonalDetails przed pobraniem personalDetails ==='), new Date();

    return this.otherService.getPersonalDetails(username)
      .subscribe(data => {
          console.log('personalDetails: ', data);
          this.personalDetails = data;
        },
        error => console.log(error));
  }

  loadAppearance(username: string){
    return this.otherService.getAppearance(username)
      .subscribe(data => {
          console.log('appearance: ', data);
          this.appearance = data;
        },
        error => console.log(error));
  }

  loadComments(username: string){
    return this.otherService.getComments(username)
      .subscribe(data => {
          console.log('comments: ', data);
          this.comments = data;
          this.comments.sort((a, b)=> {return new Date(a.postDate).getTime() - new Date(b.postDate).getTime()});
        },
        error => console.log(error));
  }

  update() {
    this.userService.getUser(this.usernameFromToken)
      .subscribe(data => {
        console.log('userToUpdate: ', data);
        this.user = data;
      }, error => console.log(error));

    this.userService.updateUser(this.username, this.user)
      .subscribe(data => {
        console.log('updatedUser: ', data);
        this.isChangeDataFailed = false;
      }, error => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.isChangeDataFailed = true;
      });

    this.otherService.updatePersonalDetails(this.username, this.personalDetails)
      .subscribe(data => console.log('updatedPersonalDetails: ', data), error => console.log(error));

    this.otherService.updateAppearance(this.username, this.appearance)
      .subscribe(data => console.log('updatedAppearance: ', data), error => console.log(error));

    this.reloadData(this.username);
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
    this.selectedFile = this.selectedFiles.item(0);

    const formData: FormData = new FormData();
    formData.append('file', this.selectedFile);
    this.fileUploadProgress = '0%';

    this.http.post('http://localhost:8080/match-zone/api/v1/users/' + this.username + '/change-avatar', formData, {
        reportProgress: true,
        observe: 'events'
      }
    ).subscribe(events => {
        if(events.type === HttpEventType.UploadProgress){
          this.fileUploadProgress = Math.round(events.loaded / events.total * 100) + '%';

        }else if(events.type === HttpEventType.Response){
          this.fileUploadProgress = '';
          console.log(events.body);
          console.log('File is completely uploaded!');
          window.location.reload();
        }
    },error => {
      this.fileErrorMessage = error.error.errorMessage;
      console.log("file error", this.fileErrorMessage);
    });

  }

  reloadData(username: string) {
    this.router.navigate(['/profile', username]);
  }

  list(){
    this.router.navigate(['users']);
  }

  getAccess(){
    return this.userService.getUser(this.username)
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

  addComment(form: NgForm){
    console.log('ppostForm: ', form);
    this.comment.content = form.value.content;
    console.log('comment.content: ', this.comment.content);
    this.comment.author = this.usernameFromToken;
    this.comment.postDate = new Date();

    this.otherService.addComment(this.username, this.comment)
      .subscribe(data => {
        console.log('addedComment: ', data);
        this.loadComments(this.username);
        this.comment.content = '';
      }, err => {
        console.log(err);
      });
  }

  editPost(id: number){
    this.postId = id;
  }

  editComment(commentUpdated: Comment){
    let comment: Comment = new Comment();
    comment.content = commentUpdated.content;
    comment.postDate = commentUpdated.postDate;

    this.otherService.editComment(commentUpdated.id, comment)
      .subscribe(data => {
        console.log('editedComment: ', data);
        this.postId = 0;
      }, err => {
        console.log(err);
      });
  }

  deleteComment(id: number){
    this.otherService.deleteComment(id)
      .subscribe(data => {
        console.log('deletedComment: ', data);
        this.loadComments(this.username);
      }, err => {
        console.log(err);
      });
  }

  onVote(vote: Vote){

    vote.value = this.currentRate;
    vote.author = this.usernameFromToken;

    this.otherService.addVote(this.username, vote)
      .subscribe(data => {
        console.log('addedVote: ', data);
        this.isVoted = true;
      }, err => {
        console.log(err);
      });

    this.otherService.getRatingInfo(this.username).subscribe( rating => {
      this.rating = rating;
      console.log('ratingInfo: ', rating);

      this.rating.countedVotes++;
      this.rating.sumOfVotes += this.currentRate;
      this.personalDetails.rating = this.rating.sumOfVotes / this.rating.countedVotes;

      this.otherService.updatePersonalDetails(this.username, this.personalDetails)
        .subscribe(data => {
            console.log('updatedPersonalDetailsForRating: ', data)
          }, error => console.log(error));
    }, error => console.log(error));

    this.reloadData(this.username);

  }

}
