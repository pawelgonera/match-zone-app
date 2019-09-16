import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { User } from "../user";
import { UserService } from "../user.service";
import {HttpClient, HttpResponse, HttpEventType, HttpRequest} from "@angular/common/http";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  id: number;
  user: User;
  updated = false;

  selectedFiles: FileList;
  selectedFile: File = null;


  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private http: HttpClient) { }

  ngOnInit() {
    this.user = new User();

    this.id = this.route.snapshot.params['id'];

    this.userService.getUser(this.id)
      .subscribe(data => {
        console.log(data);
        this.user = data;
      }, error => console.log(error));
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

  reloadData(id: number) {
    this.router.navigate(['profile', id]);
  }

  list(){
    this.router.navigate(['users']);
  }

}
