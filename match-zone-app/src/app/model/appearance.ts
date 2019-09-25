import {Eyes} from "../../eyes";
import {HairColour} from "../../hair-colour";
import {Physique} from "./physique";
import {User} from "./user";

export class Appearance {
  id: number;
  eyes: Eyes;
  hairColour: HairColour;
  height: number;
  physique: Physique;
  about: string;
  hobbies: string;
  user: User;

  /*constructor(appearanceResponse: any) {
    this.id = appearanceResponse.id;
    this.eyes = appearanceResponse.eyes;
    this.hairColour = appearanceResponse.hairColour;
    this.height = appearanceResponse.height;
    this.physique = appearanceResponse.physique;
    this.about = appearanceResponse.about;
    this.hobbies = appearanceResponse.hobbies;
    this.user = appearanceResponse.user;
  }*/
}
