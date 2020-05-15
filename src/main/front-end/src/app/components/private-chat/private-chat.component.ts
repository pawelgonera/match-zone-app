import { Component, OnInit, ElementRef, ViewChild, AfterContentInit, AfterViewInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Message} from "../../model/message";
import { User} from "../../model/user";
import {OtherService} from "../../service/other.service";
import {TokenService} from "../../service/token.service";
import {NgForm} from "@angular/forms";

@Component({
selector: 'app-private-chat',
templateUrl: './private-chat.component.html',
styleUrls: ['./private-chat.component.css']
})
export class PrivateChatComponent implements OnInit {

messages: Message[];
message: Message = new Message();
username: string;
members: User[];
usernameFromToken: string;
currentMember: string;

@ViewChild('scrollMe', {static: false}) scrollMe: ElementRef;
scrollTop: number;

constructor(private route: ActivatedRoute, private otherService: OtherService, private tokenService: TokenService) { }

  ngOnInit() {

    this.username = this.route.snapshot.params['username'];

    this.usernameFromToken = this.tokenService.getUsername();

    this.currentMember = this.username;

    this.loadMembers(this.usernameFromToken);

    this.loadMessages(this.username, this.usernameFromToken);

    window.scroll(0,150);

  }

  loadMessages(recipient: string, sender: string){
    return this.otherService.getMessages(recipient, sender)
      .subscribe(data => {
          console.log('messages: ', data);
          this.messages = data;
          this.scrollTop = this.scrollMe.nativeElement.scrollHeight;
          this.currentMember = recipient;
          console.log('scrollTop: ', this.scrollTop);
        },
        error => console.log(error));
  }

  loadMembers(sender: string){
    return this.otherService.getMembers(sender)
          .subscribe(data => {
              console.log('members: ', data);
              this.members = data;
              this.scrollTop = this.scrollMe.nativeElement.scrollHeight;
            },
            error => console.log(error));
  }

  addMessage(form: NgForm){
    console.log('messageForm: ', form);
    this.message.content = form.value.content;
    console.log('message.content: ', this.message.content);
    this.message.sender = this.usernameFromToken;
    this.message.recipient = this.currentMember;
    this.message.postDate = new Date();

    console.log("username before add message:", this.username);

    this.otherService.addMessage(this.usernameFromToken, this.message)
      .subscribe(data => {
        console.log('addedMessage: ', data);
        this.loadMessages(this.currentMember, this.usernameFromToken);
        this.loadMembers(this.usernameFromToken);
        this.message.content = '';
      }, err => {
        console.log(err);
      });
  }

}
