import {MaritalStatus} from "./marital-status";
import {Religion} from "./religion";

export class PersonalDetails {
    id: number;
    firstName: string;
    country: string;
    city: string;
    ccupation: string;
    maritalStatus: MaritalStatus;
    education: string;
    religion: Religion;
}
