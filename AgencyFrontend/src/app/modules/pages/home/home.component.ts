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
  merchantId: string = 'qR2pL7sF9oZvX1jM6cH8gT4wY5aD3eB';

  constructor(private router: Router, private offerService: OfferService) { }

  ngOnInit(): void {
  }

  // calculatePayment() {
  //   this.transactionRequest.merchantOrderId = this.generateMerchantOrderId();
  //   this.transactionRequest.amount = this.agencyAmount;
  //   this.transactionRequest.merchantId = this.merchantId;
  //   this.offerService.calculateAmount(this.transactionRequest).subscribe(
  //     (response) => {
  //       console.log('Response:', response);
  //     },
  //     (error) => {
  //       console.error('Error:', error);
  //     }
  //   )
  // }
  //
  // calculatePayment() {
  //   this.offerService.calculateAmount(this.agencyAmount).subscribe(
  //     (response) => {
  //       console.log('Response:', response);
  //     },
  //     (error) => {
  //       console.error('Error:', error);
  //     }
  //   )
  // }

  calculatePayment() {
    const data = { amount: this.agencyAmount, merchantId: this.merchantId, merchantOrderId: this.generateMerchantOrderId() };
    const queryParams = `?myData=${encodeURIComponent(JSON.stringify(data))}`;
    window.location.href = `http://localhost:4200/${queryParams}`;
  }

  generateMerchantOrderId(): number {
    return Math.floor(1000000000 + Math.random() * 9000000000);
  }


}
