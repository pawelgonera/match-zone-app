import {PersonalDetails} from "./personal-details";
import {Appearance} from "./appearance";

export class User{
    id: number;
    username: string;
    email: string;
    password: string;
    repeatedPassword: string;
    timeZoneId: string;
    personalDetails: PersonalDetails;
    appearance: Appearance;
}
