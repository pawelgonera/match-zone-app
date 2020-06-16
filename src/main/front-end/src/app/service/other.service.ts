import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpEvent} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Vote} from "../model/vote";
import {Comment} from "../model/comment";
import {Image} from "../model/image";
import {Message} from "../model/message";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': 'http://localhost:4200' })
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

  getRatingInfo(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/votes/rating-info/${username}`);
  }

  addVote(username: string, vote: Vote): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/votes/${username}`, vote, httpOptions);
  }

  addComment(id: number, comment: Comment): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/comments/${id}`, comment, httpOptions);
  }

  editComment(id: number, comment: Comment): Observable<Object> {
    return this.http.put(`${this.baseUrl}/comments/${id}`, comment);
  }

  deleteComment(id: number): Observable<Object> {
    return this.http.delete(`${this.baseUrl}/comments/${id}`);
  }

  getComments(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/comments/${id}`, httpOptions);
  }

  getAvatar(username: string){
    return this.http.get(`${this.baseUrl}/personal/avatar/${username}`);
  }

  addImages(username: string, image: FormData): Observable<HttpEvent<any>> {
    return this.http.post<string>(`${this.baseUrl}/images/${username}`, image, { reportProgress: true, observe: 'events' });
  }

  editImage(id: number, image: Image): Observable<Object> {
    return this.http.put(`${this.baseUrl}/images/${id}`, image);
  }

  deleteImage(id: number): Observable<Object> {
    return this.http.delete(`${this.baseUrl}/images/${id}`);
  }

  getImages(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/images/${id}`, httpOptions);
  }

  changeAvatar(username: string, avatar: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/${username}/change-avatar`, avatar, { reportProgress: true, observe: 'events' });
  }

  checkIfLoggedUserVoted(username: string, usernameLogged: string){
    return this.http.get(`${this.baseUrl}/votes/is-voted/${username}/${usernameLogged}`);
  }

  resetPassword(formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/reset-pass`, formData, {reportProgress: true, responseType: 'text'});
  }

  addMessage(username: string, message: Message): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/messages/${username}`, message, httpOptions);
  }

  getMessagesBySenderAndRecipient(recipient: string, sender: string): Observable<Message[]> {
      return this.http.get<Message[]>(`${this.baseUrl}/messages/${recipient}/${sender}`, httpOptions);
  }

  getMessagesBySender(sender: string): Observable<Message[]> {
      return this.http.get<Message[]>(`${this.baseUrl}/messages/${sender}`, httpOptions);
  }

  getLastMessageBySender(sender: string): Observable<any> {
         return this.http.get(`${this.baseUrl}/messages/last-message/${sender}`, httpOptions);
  }

  getMembers(sender: string): Observable<any> {
         return this.http.get(`${this.baseUrl}/messages/members/${sender}`, httpOptions);
  }

  isNewMessageFromRecipient(lastMessage: Message): Observable<any> {
         return this.http.post<any>(`${this.baseUrl}/messages/is-new-message-from-recipient/`, lastMessage, httpOptions);
  }

  isNewMessageFromSender(lastMessage: Message, username: string): Observable<any> {
         return this.http.post<any>(`${this.baseUrl}/messages/is-new-message-from-sender/${username}`, lastMessage, httpOptions);
  }

  editMessage(id: number, message: Message): Observable<Object> {
    return this.http.put(`${this.baseUrl}/messages/${id}`, message);
  }
}
