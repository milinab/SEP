import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PaymentService} from "../../services/payment.service";
import {BuyRequest} from "../../dtos/buyRequest";
import {PaymentType} from "../../enums/paymentType.enum";
import {AuthResponse} from "../../dtos/AuthResponse";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  amount: number = 0;
  merchantId: string = '';
  merchantOrderId: number = 0;
  buyRequest: BuyRequest = {
    merchantId: '',
    amount: 0,
    merchantOrderId: 0,
    paymentType: PaymentType.CREDIT_CARD
  }
  authResponse: AuthResponse = {
    paymentId: 0,
    paymentURL: '',
    amount: 0,
    qrCode: ''
  }

  constructor(private router: Router, private route: ActivatedRoute, private paymentService: PaymentService) { }

  // ngOnInit(): void {
  //   const myData = localStorage.getItem('myData');
  //   if (myData) {
  //     const data = JSON.parse(myData);
  //     console.log('Dohvaćeni podaci:', data);
  //   } else {
  //     console.log('Podaci nisu pronađeni u LocalStorage.');
  //   }
  // }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const myData = params['myData'];
      if (myData) {
        const data = JSON.parse(decodeURIComponent(myData));
        console.log('Dohvaćeni podaci:', data);
        this.merchantId = data.merchantId;
        this.amount = data.amount;
        this.merchantOrderId = data.merchantOrderId;
      } else {
        console.log('Podaci nisu pronađeni u query parametrima.');
      }
    });

  }

  cardPayment() {
    this.buyRequest.merchantId = this.merchantId
    this.buyRequest.amount = this.amount
    this.buyRequest.merchantOrderId = this.merchantOrderId
    this.buyRequest.paymentType = PaymentType.CREDIT_CARD
    this.paymentService.payment({merchantId: this.merchantId, amount: this.amount, merchantOrderId: this.merchantOrderId, paymentType: PaymentType.CREDIT_CARD}).subscribe(
      (response) => {
        this.authResponse = response;
        if(this.authResponse?.paymentURL == "success") {
          const toCard = { paymentId: this.authResponse.paymentId, amount: this.authResponse.amount};
          this.router.navigate(['/card-payment'], { queryParams: { myData: JSON.stringify(toCard) } });
        }
      },
      (error) => {
        console.log('Account data not valid.');
      }
    );

  }

  qrCodePayment() {
    console.log("AAAAAAAAAAA")
    this.paymentService.payment({merchantId: this.merchantId, amount: this.amount, merchantOrderId: this.merchantOrderId, paymentType: PaymentType.QR_CODE}).subscribe(
      (response) => {
        this.authResponse = response;
        if(this.authResponse?.paymentURL == "success") {
          const toCard = { paymentId: this.authResponse.paymentId, amount: this.authResponse.amount, qrCode:this.authResponse.qrCode};
          this.router.navigate(['/qrcode-payment'], { queryParams: { myData: JSON.stringify(toCard) } });
        }
      },
      (error) => {
        console.log('Account data not valid.');
      }
    );
  }

  payPalPayment() {
    this.paymentService.payment({merchantId: this.merchantId, amount: this.amount, merchantOrderId: this.merchantOrderId, paymentType: PaymentType.PAYPAL}).subscribe(
      (response) => {
        this.authResponse = response;
        window.location.href = this.authResponse?.paymentURL;
      },
      (error) => {
        console.log('Account data not valid.');
      }
    )

  }

  cryptoPayment(){
    this.paymentService.payment({merchantId: this.merchantId, amount: this.amount, merchantOrderId: this.merchantOrderId, paymentType: PaymentType.CRYPTO}).subscribe(
      (response) => {
        this.authResponse = response;
        //const toCard = { paymentId: this.authResponse.paymentId, amount: this.authResponse.amount};
          //this.router.navigate(['/qrcode-payment'], { queryParams: { myData: JSON.stringify(toCard) } });
        //this.router.navigate([this.authResponse?.paymentURL])
        window.location.href = this.authResponse?.paymentURL;
      },
      (error) => {
        console.log('Account data not valid.');
      }
    )
  }

}
