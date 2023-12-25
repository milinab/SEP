import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PaymentService} from "../../services/payment.service";
import {Transaction} from "../../model/transaction.model";
import {PaymentResponse} from "../../dtos/paymentResponse";
import { PaymentType } from '../../enums/paymentType.enum';

@Component({
  selector: 'app-home',
  templateUrl: './card-payment.component.html',
  styleUrls: ['./card-payment.component.css']
})
export class CardPaymentComponent implements OnInit {
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

  constructor(private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private paymentService: PaymentService,
              private router: Router) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const myData = params['myData'];
      if (myData) {
        const data = JSON.parse(decodeURIComponent(myData));
        console.log('Dohvaćeni podaci:', data);
        this.paymentId = data.paymentId;
        this.displayedAmount = data.amount
      } else {
        console.log('Podaci nisu pronađeni u query parametrima.');
      }

    });

    this.paymentForm = this.formBuilder.group({
      cardHolderName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z\s]*$/)]], //letters
      cardNumber: ['', [Validators.required, Validators.pattern(/^\d{16}$/)]], //16-digit
      expiryDate: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/\d{2}$/)]], //MM/YY
      cvv: ['', [Validators.required, Validators.pattern(/^\d{3}$/)]] //3-digit
    });
  }

  onSubmit() {
    this.submitted = true;

    if (this.paymentForm.valid) {
      const formValue = this.paymentForm.value;
      this.paymentService.pay({paymentId: this.paymentId,
        pan: formValue.cardNumber,
        expDate: formValue.expiryDate,
        cvv: formValue.cvv,
        cardHolderName: formValue.cardHolderName,
        accountNumber:'',
        paymentType: PaymentType.CREDIT_CARD }).subscribe(
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
