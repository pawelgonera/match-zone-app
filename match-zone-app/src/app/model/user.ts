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
    vote: Vote;

    /*constructor(userResponse: any){
      this.id = userResponse.id;
      this.username = userResponse.username;
      this.email = userResponse.email;
      this.password = userResponse.password;
      this.repeatedPassword = userResponse.repeatedPassword;
      this.timeZoneId = userResponse.timeZoneId;
      this.personalDetails = userResponse.personalDetails;
      this.appearance = userResponse.appearance;
    }*/

}
