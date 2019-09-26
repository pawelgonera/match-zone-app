import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Login} from "../model/login";
import {Observable} from "rxjs";
import {JwtResponse} from "../model/jwt-response";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private loginUrl = 'http://localhost:8080/match-zone/api/v1/users/login';

  constructor(public http: HttpClient) {
  }

  authenticate(credentials: Login): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(this.loginUrl, credentials, httpOptions);
  }

  /*authenticate(credentials, callback) {
    console.log(credentials);
    const headers = new HttpHeaders(credentials ? {
      authorization : 'Basic ' + btoa(credentials.username + ':' + credentials.password)
    } : {});
    console.log(headers);
    this.http.get(this.baseUrl + '/login', {headers: headers})
      .subscribe((response) => {
        let data: any ;
        data = response;
        const u = data.principal;
        return callback && callback(data);
      });

  }*/

  /*public logIn(user: User, callback) {
    console.log(user);
    const headers = new HttpHeaders();
    headers.set('Accept', 'application/json');
    // creating base64 encoded String from user name and password
    const base64Credential: string = btoa( user.username + ':' + user.password);
    headers.set( 'Authorization', 'Basic ' + base64Credential);
    const pass = bcrypt.hash(user.password, 10);
    console.log(pass);
    const headers = new HttpHeaders(user ? {
      authorization : 'Basic ' + btoa(user.username + ':' + pass)
    } : {});
    console.log(headers);
    // const options = new RequestOptions();

    return this.http.get(this.baseUrl + '/login', {headers: headers})
      .subscribe((response) => {
        console.log(response);
        let data: any ;
        data = response;
        const u = data.principal;
        return callback && callback(data);
      });
  }*/

  logOut() {
    // remove user from local storage to log user out
    // return this.http.post(AppComponent.API_URL + 'logout', {})
    // .subscribe((response) => {
    // console.log('Response_logout : ' + response);
    // let u = localStorage.getItem('currentUser');
    // console.log(u);
    // localStorage.removeItem('currentUser');
    // this.router.navigate(['/logout']);
    // },
    // error => {

    // });

  }

}
