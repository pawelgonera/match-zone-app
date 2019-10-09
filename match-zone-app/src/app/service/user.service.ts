import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Register} from "../model/register";
import {Filter} from "../model/filter";
import {PageUser} from "../model/page-user";
import {map} from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/match-zone/api/v1/users';

  constructor(private http: HttpClient) { }

  getUser(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createUser(user: Register): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/register`, user, httpOptions);
  }

  updateUser(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  getUsersList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }

  getFilteredUserList(filter: Filter): Observable<PageUser> {
    return this.http.post<PageUser>(`${this.baseUrl}/filter`, filter, httpOptions);
  }

  getPageUser(page: number): Observable<PageUser>{
    return this.http.get<PageUser>(`${this.baseUrl}/list?page=${page}&size=6`);
  }
}
