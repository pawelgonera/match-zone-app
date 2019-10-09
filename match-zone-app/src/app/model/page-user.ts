import {User} from "./user";

export class PageUser{
  content: User[];
  totalPages: number;
  totalElements: number;
  last: boolean;
  page: number;
  size: number;
  first: boolean;
  sort: string;
  direction: string;
  numberOfElements: number;
}
