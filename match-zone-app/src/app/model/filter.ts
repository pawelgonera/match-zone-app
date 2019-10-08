import {Gender} from "./enum/gender";

export class Filter{
  name: string = '';
  gender: Gender = 2;
  ageMin: number = 0;
  ageMax: number = 0;
  city: string = '';

  constructor(name: string, gender: Gender, ageMin: number, ageMax: number, city: string) {
    this.name = name;
    this.gender = gender;
    this.ageMin = ageMin;
    this.ageMax = ageMax;
    this.city = city;
  }
}
