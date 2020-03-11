import {Gender} from "./enum/gender";

export class Register{
  username: string;
  name: string;
  email: string;
  role: string[];
  password: string;
  repeatedPassword: string;
  city: string;
  gender: Gender;
  dateOfBirth: Date;
  age: number;

  constructor(username: string, name: string, email: string, password: string, repeatedPassword: string, city: string, gender: Gender, dateOfBirth: Date, age: number) {
    this.username = username;
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = ['user'];
    this.repeatedPassword = repeatedPassword;
    this.city = city;
    this.gender = gender;
    this.dateOfBirth = dateOfBirth;
    this.age = age;
  }
}
