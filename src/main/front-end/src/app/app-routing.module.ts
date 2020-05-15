import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserDetailsComponent } from "./components/user-details/user-details.component";
import { CreateUserComponent } from "./components/create-user/create-user.component";
import { UserListComponent } from "./components/user-list/user-list.component";
import {LoginComponent} from "./components/login/login.component";
import {UrlPermission} from "./urlPermission/url.permissions";
import {NotFoundComponent} from "./components/not-found/not-found.component";
import {ComingSoonComponent} from "./components/coming-soon/coming-soon.component";
import {ResetPassComponent} from "./reset-pass/reset-pass.component";
import {HomeComponent} from "./components/home/home.component";
import {SettingComponent} from "./components/setting/setting.component";
import { PrivateChatComponent } from './components/private-chat/private-chat.component';

const routes: Routes = [
  { path: 'users', component: UserListComponent},
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },//, canActivate: [UrlPermission] },
  { path: 'register', component: CreateUserComponent },
  { path: 'login', component: LoginComponent },
  { path: 'profile/:username', component: UserDetailsComponent },//, canActivate: [UrlPermission]},
  { path: 'setting', component: SettingComponent },
  { path: 'chat/:username', component: PrivateChatComponent },
  { path: 'reset-pass', component: ResetPassComponent},
  { path: 'not-found', component: NotFoundComponent },
  { path: 'coming-soon', component: ComingSoonComponent},
  { path: '**', redirectTo: '/not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

