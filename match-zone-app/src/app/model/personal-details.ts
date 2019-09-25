import {MaritalStatus} from "./marital-status";
import {Religion} from "./religion";
import {User} from "./user";

export class PersonalDetails {
    id: number;
    firstName: string = '';
    dateOfBirth: Date;
    photo: Blob;
    country: string;
    city: string;
    occupation: string;
    maritalStatus: MaritalStatus;
    education: string;
    religion: Religion;
    user: User;

  /*constructor(personalDetailsResponse: any) {
    this.id = personalDetailsResponse.id;
    this.firstName = personalDetailsResponse.firstName;
    this.dateOfBirth = personalDetailsResponse.dateOfBirth;
    this.photo = personalDetailsResponse.photo;
    this.rating = personalDetailsResponse.rating;
    this.country = personalDetailsResponse.country;
    this.city = personalDetailsResponse.city;
    this.occupation = personalDetailsResponse.occupation;
    this.maritalStatus = personalDetailsResponse.maritalStatus;
    this.education = personalDetailsResponse.education;
    this.religion = personalDetailsResponse.religion;
    this.user = personalDetailsResponse.user;
  }*/

}
