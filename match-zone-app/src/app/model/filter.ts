import {Gender} from "./enum/gender";;
import {PageUser} from "./page-user";

export class Filter{
  name: string = '';
  gender: Gender = 2;
  ageMin: number = 0;
  ageMax: number = 0;
  city: string = '';
  ratingMin: number = 0;
  ratingMax: number = 0;
  pageUser: PageUser;

  constructor(name: string, gender: Gender, ageMin: number, ageMax: number, city: string, ratingMin: number, ratingMax: number, pageUser: PageUser){
    this.name = name;
    this.gender = gender;
    this.ageMin = ageMin;
    this.ageMax = ageMax;
    this.city = city;
    this.ratingMin = ratingMin;
    this.ratingMax = ratingMax;
    this.pageUser = pageUser;
  }
}
