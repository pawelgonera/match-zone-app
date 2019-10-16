import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { CreateUserComponent } from './components/create-user/create-user.component';
import { UserDetailsComponent } from './components/user-details/user-details.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { HttpClientModule } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './components/login/login.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import {UrlPermission} from "./urlPermission/url.permissions";
import {AppRoutingModule} from "./app-routing.module";
import {httpInterceptorProviders} from "./interceptor/authentication-interceptor";
import {RouterModule} from "@angular/router";
import {UserService} from "./service/user.service";
import {AuthenticationService} from "./service/authentication.service";
import {TokenService} from "./service/token.service";
import {OtherService} from "./service/other.service";
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { HomeComponent } from './components/home/home.component';

@NgModule({
  declarations: [
    AppComponent,
    CreateUserComponent,
    UserDetailsComponent,
    UserListComponent,
    LoginComponent,
    NotFoundComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
    RouterModule,
    AppRoutingModule,
    AngularFontAwesomeModule,
  ],
  providers: [
    UserService,
    AuthenticationService,
    TokenService,
    OtherService,
    UserDetailsComponent,
    httpInterceptorProviders,
    UrlPermission
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
