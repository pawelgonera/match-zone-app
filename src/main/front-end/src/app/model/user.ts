import {PersonalDetails} from "./personal-details";
import {Vote} from "./vote";
import {Appearance} from "./appearance";
import {Message} from "./message";

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
    messages: Message[];
    lastMessage: string;
    lastTime: number;
    lastPostDate: Date;
    timeFormat: string;
    notification: number = 0;
}
