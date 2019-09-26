export class Register{
  username: string;
  name: string;
  email: string;
  role: string[];
  password: string;
  repeatedPassword: string;
  dateOfBirth: string;

  constructor(username: string, name: string, email: string, password: string, repeatedPassword: string, dateOfBirth: string) {
    this.username = username;
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = ['user'];
    this.repeatedPassword = repeatedPassword;
    this.dateOfBirth = dateOfBirth;
  }
}
