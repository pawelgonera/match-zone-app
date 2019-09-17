import {MaritalStatus} from "./marital-status";
import {Religion} from "./religion";
import {User} from "./user";

export class PersonalDetails {
    id: number;
    firstName: string;
    dateOfBirth: Date;
    photo: Blob;
    country: string;
    city: string;
    occupation: string;
    maritalStatus: MaritalStatus;
    education: string;
    religion: Religion;
    user: User;
}
