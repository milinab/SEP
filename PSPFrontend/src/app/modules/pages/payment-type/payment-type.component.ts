import { Component, OnInit } from '@angular/core';
import { RegistrationService } from '../../services/registration.service';

@Component({
  selector: 'app-payment-type',
  templateUrl: './payment-type.component.html',
  styleUrls: ['./payment-type.component.css']
})
export class PaymentTypeComponent implements OnInit {
  paymentMethods: string[] = ['creditCard', 'qrCode', 'paypal', 'crypto'];
  selectedPaymentMethods: string[] = [];
  creditCardEnabled: boolean = false;
  qrCodeEnabled: boolean = false;
  paypalEnabled: boolean = false;
  cryptoEnabled: boolean = false;
  merchantId: string = "";
  password: string = "";
  constructor(private registrationService: RegistrationService) { }

  ngOnInit(): void {
  }

  submitForm(form: any): void {
    this.registrationService.registrate({merchantId: this.merchantId, password: this.password, creditCardEnabled: this.creditCardEnabled, qrCodeEnabled: this.qrCodeEnabled, paypalEnabled: this.paypalEnabled, cryptoEnabled: this.cryptoEnabled}).subscribe(
      (response) => {
        console.log('Form submitted:', form.value);
      },
      (error) => {
        console.log('Failed submmition.');
      }
    );
    
  }
}
