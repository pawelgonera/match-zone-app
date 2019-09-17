import {Eyes} from "../eyes";
import {HairColour} from "../hair-colour";
import {Physique} from "./physique";
import {User} from "./user";

export class Appearance{
    id: number;
    eyes: Eyes;
    hairColour: HairColour;
    height: number;
    physique: Physique;
    about: string;
    hobbies: string;
    user: User;
}
