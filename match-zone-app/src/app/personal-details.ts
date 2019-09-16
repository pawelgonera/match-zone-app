import {MaritalStatus} from "./marital-status";
import {Religion} from "./religion";

export class PersonalDetails {
    id: number;
    firstName: string;
    age: number;
    photo: Blob;
    country: string;
    city: string;
    occupation: string;
    maritalStatus: MaritalStatus;
    education: string;
    religion: Religion;
}
