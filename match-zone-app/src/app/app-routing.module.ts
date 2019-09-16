import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserDetailsComponent } from "./user-details/user-details.component";
import { CreateUserComponent } from "./create-user/create-user.component";
import { UserListComponent } from "./user-list/user-list.component";
import { UserUpdateComponent } from "./user-update/user-update.component";

const routes: Routes = [
  { path: '', redirectTo: 'users', pathMatch: 'full' },
  { path: 'users', component: UserListComponent },
  { path: 'register', component: CreateUserComponent },
  { path: 'profile/:id', component: UserDetailsComponent },
  { path: 'edit/:id', component: UserUpdateComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
