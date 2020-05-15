import {User} from "./user";

export class Message {
id:number;
content: string;
sender: string;
recipient: string;
avatar: Blob;
postDate: Date;
user: User;
}
