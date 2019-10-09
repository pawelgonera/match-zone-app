import {Gender} from "./enum/gender";;
import {PageUser} from "./page-user";

export class Filter{
  name: string = '';
  gender: Gender = 2;
  ageMin: number = 0;
  ageMax: number = 0;
  city: string = '';
  pageUser: PageUser;

  constructor(name: string, gender: Gender, ageMin: number, ageMax: number, city: string, pageUser: PageUser){
    this.name = name;
    this.gender = gender;
    this.ageMin = ageMin;
    this.ageMax = ageMax;
    this.city = city;
    this.pageUser = pageUser;
  }
}
