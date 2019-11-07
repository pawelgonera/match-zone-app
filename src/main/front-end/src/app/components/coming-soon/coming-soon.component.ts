import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-comming-soon',
  templateUrl: './coming-soon.component.html',
  styleUrls: ['./coming-soon.component.css']
})
export class ComingSoonComponent implements OnInit {

  constructor() { }

  comingSoonPath: any = "../assets/images/coming-soon.png";

  ngOnInit() {
  }

}
