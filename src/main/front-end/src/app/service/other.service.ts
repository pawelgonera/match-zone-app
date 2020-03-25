import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Vote} from "../model/vote";
import {Comment} from "../model/comment";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class OtherService {

  private baseUrl = 'http://localhost:8080/match-zone/api/v1';

  constructor(private http: HttpClient) { }

  getPersonalDetails(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/personal/${username}`);
  }

  updatePersonalDetails(username: string, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/personal/${username}`, value);
  }

  getAppearance(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/appearance/${username}`);
  }

  updateAppearance(username: string, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/appearance/${username}`, value);
  }

  getRatingInfo(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/votes/rating-info/${username}`);
  }

  addVote(username: string, vote: Vote): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/votes/${username}`, vote, httpOptions);
  }

  addComment(username: string, comment: Comment): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/comments/${username}`, comment, httpOptions);
  }

  editComment(id: number, content: string): Observable<Object> {
    return this.http.put(`${this.baseUrl}/comments/${id}`, content);
  }

  deleteComment(id: number): Observable<Object> {
    return this.http.delete(`${this.baseUrl}/comments/${id}`);
  }

  getComments(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/comments/${username}`);
  }

  checkIfLoggedUserVoted(username: string, usernameLogged: string){
    return this.http.get(`${this.baseUrl}/votes/is-voted/${username}/${usernameLogged}`);
  }

  resetPassword(formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/reset-pass`, formData, {reportProgress: true, responseType: 'text'});
  }

}
