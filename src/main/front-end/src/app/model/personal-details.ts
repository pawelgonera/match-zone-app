import {MaritalStatus} from "./enum/marital-status";
import {Religion} from "./enum/religion";
import {User} from "./user";
import {Gender} from "./enum/gender";

export class PersonalDetails {
    id: number;
    dateOfBirth: string;
    age: number;
    gender: Gender;
    rating:number;
    photo: Blob;
    country: string;
    city: string;
    occupation: string;
    maritalStatus: MaritalStatus;
    education: string;
    religion: Religion;
    user: User;
}
