import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-payment-type',
  templateUrl: './payment-type.component.html',
  styleUrls: ['./payment-type.component.css']
})
export class PaymentTypeComponent implements OnInit {
  paymentMethods: string[] = ['creditCard', 'qrCode', 'paypal', 'crypto'];
  selectedPaymentMethods: string[] = [];
  constructor() { }

  ngOnInit(): void {
  }

  submitForm(form: any): void {
    // Handle the form submission based on the selected payment method
    console.log('Form submitted:', form.value);
  }
}
