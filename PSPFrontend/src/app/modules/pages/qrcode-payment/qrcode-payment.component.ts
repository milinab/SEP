import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Transaction } from '../../model/transaction.model';
import {PaymentResponse} from "../../dtos/paymentResponse";
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../../services/payment.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { PaymentType } from '../../enums/paymentType.enum';

@Component({
  selector: 'app-qrcode-payment',
  templateUrl: './qrcode-payment.component.html',
  styleUrls: ['./qrcode-payment.component.css']
})
export class QRcodePaymentComponent implements OnInit {
  paymentForm!: FormGroup;
  submitted = false;
  displayedAmount: number = 0;
  paymentId: number = 0;
  public transaction: Transaction = new Transaction()
  payResponse: PaymentResponse = {
    merchantOrderId: 0,
    acquirerOrderId: 0,
    acquirerTimestamp: new Date(),
    paymentStatus: ''
  }
  qrCodeImageUrl: any;

  constructor(private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private paymentService: PaymentService,
              private router: Router,
              private sanitizer: DomSanitizer) { }
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const myData = params['myData'];
      if (myData) {
        const data = JSON.parse(decodeURIComponent(myData));
        console.log('Dohvaćeni podaci:', data);
        this.paymentId = data.paymentId;
        this.displayedAmount = data.amount;
        
        this.qrCodeImageUrl = "assets/" + data.qrCode;
        console.log(this.qrCodeImageUrl)
      } else {
        console.log('Podaci nisu pronađeni u query parametrima.');
      }

    });

    this.paymentForm = this.formBuilder.group({
      accountNumber: ['', [Validators.required]], //letters
    });
  }

  onSubmit() {
    this.submitted = true;

    if (this.paymentForm.valid) {
      const formValue = this.paymentForm.value;
      
      this.paymentService.pay({paymentId: this.paymentId,
        pan: '',
        expDate: '',
        cvv: '',
        cardHolderName: '',
        accountNumber: formValue.accountNumber,
        paymentType: PaymentType.QR_CODE}).subscribe(
        (response) => {
          this.payResponse = response
          if(this.payResponse.paymentStatus == "SUCCESS") {
            this.router.navigate(['/transaction-success'])
          } else if(this.payResponse.paymentStatus == "FAILED") {
            this.router.navigate(['/transaction-failed'])
          } else {
            this.router.navigate(['/transaction-error'])
          }
        }
      )
    }
  }

}
