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
import {Image} from "../../model/image";
import { ImageCroppedEvent } from 'ngx-image-cropper';

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

  isReadyToDisplay = false;

  personalDetails: PersonalDetails = new PersonalDetails();
  appearance: Appearance = new Appearance();
  vote: Vote = new Vote();

  comment: Comment = new Comment();
  comments: Comment[];
  commentAvatar: any;
  authorDetails: PersonalDetails = new PersonalDetails();
  postId: number = 0;

  selectedFiles: FileList;
  imageChangedEvent: any = '';
  croppedImage: any = '0';
  photoToSend: File = null;
  avatarUploadProgress: string = null;
  photoUploadProgress: string = null;
  avatarErrorMessage: string;
  photoId: number = 0;
  photoErrorMessage: string;
  typeOfFile: string;

  images: Image[];
  title: string;

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

    this.loadUser(this.username);
    this.loadPersonalDetails(this.username);
    this.loadAppearance(this.username);
    this.loadComments(this.username);
    this.loadImages(this.username);

    window.scroll(0,0);

    //this.reloadData(this.username);
  }

  loadUser(username: string){
    return this.userService.getUser(username)
      .subscribe(data => {
        console.log('user: ', data);
        this.user = data;
      }, error => console.log(error));
  }

  loadPersonalDetails(username: string){
    return this.otherService.getPersonalDetails(username)
      .subscribe(data => {
          console.log('personalDetails: ', data);
          this.personalDetails = data;
          this.isReadyToDisplay = true;
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

   loadImages(username: string){
    return this.otherService.getImages(username)
      .subscribe(data => {
          console.log('images: ', data);
          this.images = data;
          this.photoUploadProgress = '';
          this.title = '';
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
    this.croppedImage = '';
    this.imageChangedEvent = event;
    this.selectedFiles = event.target.files;
    this.personalDetails.photo = null;
    console.log('event(file): ', event);
    console.log('selectedFiles:', this.selectedFiles);
  }

  onGalleryUpload(){
    const formData: FormData = new FormData();
    formData.append('photo', this.selectedFiles.item(0));
    if(this.title == null){
      this.title = '';
    }
    formData.append('title', this.title);
    this.photoUploadProgress = '0%';

     this.otherService.addImages(this.username, formData)
      .subscribe(events => {
        if(events.type === HttpEventType.UploadProgress){
          this.photoUploadProgress = Math.round(events.loaded / events.total * 100) + '%';
        }
        console.log('File is completely uploaded!');
        console.log('imageAdded: ', events);
        this.loadImages(this.username);
      },error => {
        this.photoErrorMessage = error.error.errorMessage;
        this.photoUploadProgress = '';
        console.log("file error", this.photoErrorMessage);
      });

  }

  editPhoto(id: number){
    this.photoId = id;
  }

  editTitle(image: Image){
    this.otherService.editImage(image.id, image)
      .subscribe(data => {
        console.log('editedImage: ', data);
        this.photoId = 0;
        this.title = '';
      }, err => {
        console.log(err);
      });
  }

  deletePhoto(id: number){
    this.otherService.deleteImage(id)
      .subscribe(data => {
        console.log('deletedImage: ', data);
        this.loadImages(this.username);
      }, err => {
        console.log(err);
      });
  }

  imageCropped(croppedEvent) {
      this.croppedImage = croppedEvent.base64;
      console.log('croppedEvent:', croppedEvent)
    }

  dataURItoBlob(dataURI): Blob {
    const byteString = atob(dataURI.split(',')[1]);
    const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
    this.typeOfFile = mimeString;
    console.log('mimeString:', mimeString)
    const ab = new ArrayBuffer(byteString.length);
    let bytes = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      bytes[i] = byteString.charCodeAt(i);
    }
    return new Blob([bytes], { type: mimeString });
  }

  onUpload(){
    this.photoToSend = new File([this.dataURItoBlob(this.croppedImage)], 'avatar.png', { type: this.typeOfFile });
    console.log('photoToSend:', this.photoToSend)
    const formData: FormData = new FormData();
    formData.append('file', this.photoToSend);
    this.avatarUploadProgress = '0%';

    this.otherService.changeAvatar(this.username, formData)
    .subscribe(events => {
        if(events.type === HttpEventType.UploadProgress){
          this.avatarUploadProgress = Math.round(events.loaded / events.total * 100) + '%';
        }
        console.log(events.body);
        console.log('File is completely uploaded!');
        this.imageChangedEvent = '';
        this.loadPersonalDetails(this.username);
      },error => {
        this.avatarErrorMessage = error.error.errorMessage;
        this.avatarUploadProgress = '';
        console.log("file error", this.avatarErrorMessage);
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
