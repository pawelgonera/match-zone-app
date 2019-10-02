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

  getPersonalDetails(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/personal/${id}`);
  }

  updatePersonalDetails(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/personal/${id}`, value);
  }

  getAppearance(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/appearance/${id}`);
  }

  updateAppearance(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/appearance/${id}`, value);
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

  getRatingInfo(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/votes/rating-info/${id}`);
  }

  addVote(id: number, vote: Vote): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/votes/${id}`, vote, httpOptions);
  }

  getVotesAuthors(id: number): Observable<any>{
    return this.http.get(`${this.baseUrl}/votes/names/${id}`);
  }

  checkIfLoggedUserVoted(id: number, username: string){
    return this.http.get(`${this.baseUrl}/votes/is-voted/${id}/${username}`);
  }

  updateVote(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/votes/${id}`, value);
  }

}
