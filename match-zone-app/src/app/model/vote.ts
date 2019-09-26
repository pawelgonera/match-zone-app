import {User} from "./user";

export class Vote {
  id: number;
  sumOfVotes: number;
  countedVotes: number;
  rating: number = 0;
  user: User;
}
