import { Component, OnInit, ElementRef, ViewChild, AfterContentInit, AfterViewInit, OnDestroy } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Message} from "../../model/message";
import { User} from "../../model/user";
import {UserService} from "../../service/user.service";
import {OtherService} from "../../service/other.service";
import {TokenService} from "../../service/token.service";
import {NgForm} from "@angular/forms";
import { interval, Subscription } from 'rxjs';
import { DatePipe } from '@angular/common';

@Component({
selector: 'app-private-chat',
templateUrl: './private-chat.component.html',
styleUrls: ['./private-chat.component.css'],
providers: [DatePipe]
})
export class PrivateChatComponent implements OnInit, OnDestroy {

selected :any;

messages: Message[];
message: Message = new Message();
username: string;
members: User[] = new Array<User>();
usernameFromToken: string;
currentMember: string;
isNewMessageAll: boolean;
lastMessageFromRecipient: Message = new Message();
lastMessageFromAll: Message = new Message();

//lastMessageDate: string;

isReadyToDisplay = false;

subscription: Subscription;

//notification: number;

@ViewChild('scrollMe', {static: false}) scrollMe: ElementRef;
scrollTop: number;

constructor(private route: ActivatedRoute, private userService: UserService, private otherService: OtherService, private tokenService: TokenService, private datePipe: DatePipe) { }

  ngOnInit() {

    this.username = this.route.snapshot.params['username'];

    this.usernameFromToken = this.tokenService.getUsername();

    this.currentMember = this.username;

    //this.loadMembers(this.usernameFromToken);

    //this.loadMessages(this.currentMember, this.usernameFromToken, this.members);

    this.selected = this.currentMember;

    this.setLastMessageFromAll(this.usernameFromToken);

    const source = interval(2000);
    this.subscription = source.subscribe(val => {
          console.log('this.lastMessageFromRecipient onInterval ', this.lastMessageFromRecipient);
          if(this.lastMessageFromRecipient != null){
              this.newMessageNotifyChannel(this.lastMessageFromRecipient);
          }
          if(this.lastMessageFromAll != null){
              this.newMessageNotifyAll(this.lastMessageFromAll, this.usernameFromToken)
          }
          for (let i = 0; i < this.members.length; i++){
                  let postDate = this.members[i].lastPostDate;
                  this.formatLastTime(postDate, i, this.members);
              }
      });

    window.scroll(0,150);

  }

  ngOnDestroy() {
        this.subscription.unsubscribe();
  }

  select(member) {
      this.selected = member;
  }

  isActive(member) {
      return this.selected === member;
  }

  newMessageNotifyChannel(lastMessage: Message){
    console.log('postDate: ', lastMessage.postDate)
    return this.otherService.isNewMessageFromRecipient(lastMessage)
      .toPromise().then(data => {
          console.log('isNewMessageChannel: ', data);
          if(data){
              this.loadMembers(this.usernameFromToken);
          }
          this.scrollTop = this.scrollMe.nativeElement.scrollHeight;
        },
        error => console.log(error));
  }

   newMessageNotifyAll(lastMessage: Message, username: string){
    console.log('postDateAll: ', lastMessage.postDate)
    return this.otherService.isNewMessageFromSender(lastMessage, username)
      .toPromise().then(data => {
          console.log('isNewMessageAll: ', data);
          if(data){
              //this.loadMembers(this.usernameFromToken);
              this.setLastMessageFromAll(this.usernameFromToken);
          }
          this.scrollTop = this.scrollMe.nativeElement.scrollHeight;
        },
        error => console.log(error));
  }

  loadMessages(recipient: string, sender: string, members: User[]){
    return this.otherService.getMessagesBySenderAndRecipient(recipient, sender)
      .subscribe(data => {
          console.log('messages: ', data);
          this.messages = data;
          this.scrollTop = this.scrollMe.nativeElement.scrollHeight;
          this.currentMember = recipient;
          const recipientMessages = this.messages.filter(m => m.sender === this.currentMember);
           for (let i = 0; i < recipientMessages.length; i++) {
              if(recipientMessages[i].unread  == true){
                  recipientMessages[i].unread = false;
                  this.editMessage(recipientMessages[i]);
              }
          }
          //console.log('recipientMessages: ', recipientMessages);
          //if(recipientMessages.length !== 0){
          if(this.messages.length !== 0){
              //this.lastMessageFromRecipient = recipientMessages[recipientMessages.length-1];
              this.lastMessageFromRecipient = this.messages[this.messages.length-1];
              console.log('lastMessage(if): ', this.lastMessageFromRecipient);
          }else{
            this.lastMessageFromRecipient = new Message();
            this.lastMessageFromRecipient.content = 'No messages';
            this.lastMessageFromRecipient.postDate = new Date();
            this.lastMessageFromRecipient.postDate.setTime(this.lastMessageFromRecipient.postDate.getTime() + (300 * 60 * 1000));
            console.log('this.lastMessageFromRecipient.postDate: ', this.lastMessageFromRecipient.postDate);
            this.lastMessageFromRecipient.sender = sender;
            this.lastMessageFromRecipient.recipient = recipient;
          }
          //console.log('lastMessageFromRecipient: ', this.lastMessageFromRecipient);

          if(members.length !== 0){
              console.log('members in messages: ', members);
              let memberFound = members.find(m => m.username === recipient);
              if(memberFound !== undefined){
                  memberFound.notification = 0;
              }else{
                  this.loadRecipient(recipient);
              }
          }
          //this.members = members;
          console.log('this.members in loadMessages: ', this.members);
          this.members = members.sort((m1: User, m2: User) => {
              return this.getTime(m2.lastPostDate) - this.getTime(m1.lastPostDate);
          });

          this.isReadyToDisplay = true;
        },
        error => console.log(error));
  }

  loadRecipient(recipient: string){
      return this.userService.getUser(recipient)
          .subscribe(data => {
            console.log('recipient: ', data);
            let array: Array<User> = [];
            array.push(data);
            if(this.members.length === 0){
              this.members = array;
              this.members[0].lastMessage = 'No messages';
              //this.members[0].lastPostDate = new Date();
            }else{
              this.members.push(data);
              this.members[this.members.length - 1].lastMessage = 'No messages';
              //this.members[this.members.length - 1].lastPostDate = new Date();
            }
          },
            error => console.log(error));
  }

  private setLastMessageFromAll(sender: string){
      return this.otherService.getLastMessageBySender(sender)
          .subscribe(message => {
          console.log('lastMessagesBySender: ', message);
          this.lastMessageFromAll = message;
          console.log('lastMessageFromAll(if): ', this.lastMessageFromAll);
          this.loadMembers(this.usernameFromToken);
          },
          error => {
              this.lastMessageFromAll = new Message();
              this.lastMessageFromAll.content = 'No messages';
              this.lastMessageFromAll.postDate = new Date();
              this.lastMessageFromAll.sender = this.usernameFromToken;
              this.lastMessageFromAll.recipient = this.currentMember;

              this.loadMembers(this.usernameFromToken);
              console.log(error);
          });
  }

  loadMembers(sender: string){
    return this.otherService.getMembers(sender)
          .subscribe(members => {
              //this.members = members;
              this.scrollTop = this.scrollMe.nativeElement.scrollHeight;
              console.log('members: ', members);
              let memberMessages: any[];
              for (let i = 0; i < members.length; i++) {
                  members[i].notification = 0;
                  this.otherService.getMessagesBySenderAndRecipient(members[i].username, this.usernameFromToken)
                        .subscribe(messages =>{
                            memberMessages = messages;
                            //console.log('memberMessages: ', memberMessages);
                            let recipientMessages: Message[];
                            recipientMessages = memberMessages.filter(m => m.sender === members[i].username);
                            if(recipientMessages.length !== 0){
                                members[i].lastMessage = recipientMessages[recipientMessages.length-1].content;
                                members[i].lastPostDate = recipientMessages[recipientMessages.length-1].postDate;
                                if(members[i].lastMessage.length > 23){
                                    members[i].lastMessage = members[i].lastMessage.slice(0, 23);
                                    members[i].lastMessage = members[i].lastMessage.concat('...');
                                }
                                for (let j = recipientMessages.length-1; j >= 0; j--) {
                                    //console.log('members.notification in loop: ', members[i].notification);
                                    if(recipientMessages[j].unread === true){
                                        members[i].notification = members[i].notification + 1;
                                        //console.log('members.notification in if: ', members[i].notification);
                                    }else{
                                        break;
                                    }
                                }
                                //console.log('members.notification after loop: ', members[i].notification);

                                let postDate = recipientMessages[recipientMessages.length-1].postDate;
                                this.formatLastTime(postDate, i, members);
                            }
                            //console.log('members.notification in end: ', members[i].notification);
                  });
              }
              this.isReadyToDisplay = true;
              //this.members = members;
              /*this.members = this.members.sort((m1: User, m2: User) => {
                  return this.getTime(m1.lastPostDate) - this.getTime(m2.lastPostDate);
              });*/

              this.loadMessages(this.currentMember, this.usernameFromToken, members);
              //console.log('this.members: ', this.members);
            },
            error => {
              this.loadRecipient(this.currentMember);
              this.loadMessages(this.currentMember, this.usernameFromToken, this.members);
              this.isReadyToDisplay = true;
              console.log(error);
            });
  }

  private getTime(date: Date) {
    //console.log('date? (getTime): ', date);
    return date != null ? new Date(date).getTime() : 0;
  }

  editMessage(message: Message){
    this.otherService.editMessage(message.id, message)
      .subscribe(data => {
        console.log('editedMessage: ', data);
      }, err => {
        console.log(err);
      });
  }

  private formatLastTime(postDate: Date, i: number, members: User[]){
      let last = new Date(postDate).getTime();
      let now = new Date().getTime();
      //console.log('now: ', new Date().getTime());
      let diff = now - last;
      //console.log('time diff: ', diff);
      let seconds = diff/1000;
      let minutes = seconds/60;
      let hours = minutes/60;
      let days = hours/24;
      let time = Math.floor(minutes);
      if(time < 1){
        //console.log('time: ', time);
        members[i].lastTime = Math.floor(minutes);
        members[i].timeFormat = 'now';
      }
      if(time < 60){
        members[i].lastTime = Math.floor(minutes);
        members[i].timeFormat = 'min ago';
      }
      if(time >= 60){
        members[i].lastTime = Math.floor(hours);
        members[i].timeFormat = 'hours ago';
      }
      if(time >= 1440){
        members[i].lastTime = Math.floor(days);
        members[i].timeFormat = 'days ago';
      }
  }

  addMessage(form: NgForm){
    //console.log('messageForm: ', form);
    this.message.content = form.value.content;
    //console.log('message.content: ', this.message.content);
    this.message.sender = this.usernameFromToken;
    this.message.recipient = this.currentMember;
    this.message.postDate = new Date();
    this.message.unread = true;

    //console.log("message.postDate:", this.message.postDate);
    //console.log("username before add message:", this.username);

    this.otherService.addMessage(this.usernameFromToken, this.message)
      .subscribe(data => {
        //console.log('addedMessage: ', data);
        this.loadMessages(this.currentMember, this.usernameFromToken, this.members);
        this.message.content = '';
        this.scrollTop = this.scrollMe.nativeElement.scrollHeight;
      }, err => {
        console.log(err);
      });
  }

}
