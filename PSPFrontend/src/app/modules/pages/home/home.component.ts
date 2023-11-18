import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  price: string | null = "0"
  displayedAmount: number = 0;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {

  }

}
