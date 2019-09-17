import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from "../user";
import {UserService} from "../user.service";
import {HttpClient, HttpRequest, HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  id: number;
  user: User;
  updated = false;

  birthDate: Date;
  timeDifference: number;
  age: number;

  selectedFiles: FileList;
  selectedFile: File = null;


  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private http: HttpClient) { }

  ngOnInit() {
    this.getUser();
  }

  update() {
    this.id = this.route.snapshot.params['id'];
    this.userService.getUser(this.id)
      .subscribe(data => {
        console.log(data);
        this.user = data;
      }, error => console.log(error));

    this.userService.updateUser(this.id, this.user)
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
    const req = new HttpRequest('POST', 'http://localhost:8080/match-zone/api/v1/users/' + this.id + '/changeavatar', formData, {
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

  parseDate(dateString: string): Date {
    if (dateString) {
      return new Date(dateString);
    }
    return null;
  }

  public calculateAge(user: User): number {
    this.birthDate = user.personalDetails.dateOfBirth;
    if (this.birthDate) {
      this.timeDifference = Math.abs(Date.now() - new Date(this.birthDate).getTime());
      this.age = Math.floor(this.timeDifference / (1000 * 3600 * 24) / 365.25);
    }
    return this.age;
  }

  reloadData(id: number) {
    this.router.navigate(['profile', id]);
  }

  list(){
    this.router.navigate(['users']);
  }

  getUser(): User{
    this.user = new User();

    this.id = this.route.snapshot.params['id'];

    this.userService.getUser(this.id)
      .subscribe(data => {
        console.log(data);
        this.user = data;
      }, error => console.log(error));

    return this.user;
  }

}
