import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {OfferService} from "../../services/offer.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  agencyAmount: number = 250 ;

  constructor(private router: Router, private offerService: OfferService) { }

  ngOnInit(): void {
  }

  calculatePayment() {
    this.offerService.calculateAmount(this.agencyAmount).subscribe(
      (response) => {
        console.log('Response:', response);
      },
      (error) => {
        console.error('Error:', error);
      }
    )
  }
}
