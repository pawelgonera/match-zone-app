import {Eyes} from "./enum/eyes";
import {HairColour} from "./enum/hair-colour";
import {Physique} from "./enum/physique";
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

}
