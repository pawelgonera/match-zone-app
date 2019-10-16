import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Vote} from "../model/vote";

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

  getVote(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/votes/${id}`);
  }

  getAllVotes(): Observable<any>{
    return this.http.get(`${this.baseUrl}/votes`);
  }

  getCountVotes(id: number){
    return this.http.get<number>(`${this.baseUrl}/votes/count/${id}`);
  }

  getSumOfVotes(id: number){
    return this.http.get<number>(`${this.baseUrl}/votes/sum/${id}`);
  }

  getRatingInfo(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/votes/rating-info/${username}`);
  }

  addVote(username: string, vote: Vote): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/votes/${username}`, vote, httpOptions);
  }

  getVotesAuthors(id: number): Observable<any>{
    return this.http.get(`${this.baseUrl}/votes/names/${id}`);
  }

  checkIfLoggedUserVoted(username: string, usernameLogged: string){
    return this.http.get(`${this.baseUrl}/votes/is-voted/${username}/${usernameLogged}`);
  }

  updateVote(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/votes/${id}`, value);
  }

}
