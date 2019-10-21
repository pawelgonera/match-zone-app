import {Gender} from "./enum/gender";

export class Register{
  username: string;
  name: string;
  email: string;
  role: string[];
  password: string;
  repeatedPassword: string;
  dateOfBirth: Date;
  age: number;
  gender: Gender;

  constructor(username: string, name: string, email: string, password: string, repeatedPassword: string, dateOfBirth: Date, age: number) {
    this.username = username;
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = ['user'];
    this.repeatedPassword = repeatedPassword;
    this.dateOfBirth = dateOfBirth;
    this.age = age;
    this.gender = 2;
  }
}
