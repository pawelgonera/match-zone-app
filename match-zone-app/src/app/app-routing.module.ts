import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserDetailsComponent } from "./components/user-details/user-details.component";
import { CreateUserComponent } from "./components/create-user/create-user.component";
import { UserListComponent } from "./components/user-list/user-list.component";
import { UserUpdateComponent } from "./user-update/user-update.component";
import {LoginComponent} from "./components/login/login.component";
import {UrlPermission} from "./urlPermission/url.permissions";
import {NotFoundComponent} from "./components/not-found/not-found.component";

const routes: Routes = [
  //{ path: '', redirectTo: 'users', pathMatch: 'full' },
  { path: '', component: LoginComponent},
  { path: 'users', component: UserListComponent, canActivate: [UrlPermission] },
  { path: 'register', component: CreateUserComponent },
  { path: 'login', component: LoginComponent },
  { path: 'profile/:id', component: UserDetailsComponent, canActivate: [UrlPermission]},
  { path: 'edit/:id', component: UserUpdateComponent },
  { path: 'not-found', component: NotFoundComponent },
  { path: '**', redirectTo: '/not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

