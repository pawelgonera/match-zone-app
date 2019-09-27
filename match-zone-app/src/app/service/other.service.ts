import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';

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
    return this.http.get(`${this.baseUrl}/vote/${id}`);
  }

  updateVote(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/vote/${id}`, value);
  }

}
