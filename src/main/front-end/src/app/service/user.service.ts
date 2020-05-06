import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Register} from "../model/register";
import {Filter} from "../model/filter";
import {PageUser} from "../model/page-user";
import {PagedListHolder} from "../model/paged-list-holder";
import {Gender} from "../model/enum/gender";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/match-zone/api/v1/users';

  constructor(private http: HttpClient) { }

  getUser(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/${username}`);
  }

  createUser(user: Register): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/register`, user, httpOptions);
  }

  updateUser(username: string, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${username}`, value);
  }

  changePassword(value: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/change-password`, value);
  }

  changeEmail(value: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/change-email`, value);
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  getUsersList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }

  getFilteredUserList(filter: Filter): Observable<PagedListHolder> {
    return this.http.post<PagedListHolder>(`${this.baseUrl}/filter`, filter, httpOptions);
  }

}
