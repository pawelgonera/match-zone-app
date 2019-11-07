import {PersonalDetails} from "./personal-details";
import {Vote} from "./vote";
import {Appearance} from "./appearance";

export class User{
    id: number;
    username: string;
    firstName: string;
    email: string;
    password: string;
    repeatedPassword: string;
    timeZoneId: string;
    personalDetails: PersonalDetails;
    appearance: Appearance;
    votes: Vote[];

}
