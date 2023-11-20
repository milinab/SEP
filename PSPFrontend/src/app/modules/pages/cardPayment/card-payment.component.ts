import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PaymentService} from "../../services/payment.service";
import {Transaction} from "../../model/transaction.model";
import {PaymentType} from "../../enums/paymentType.enum";

@Component({
  selector: 'app-home',
  templateUrl: './card-payment.component.html',
  styleUrls: ['./card-payment.component.css']
})
export class CardPaymentComponent implements OnInit {
  paymentForm!: FormGroup;
  submitted = false;
  displayedAmount: number = 748.56;
  public transaction: Transaction = new Transaction()

  constructor(private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private paymentService: PaymentService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const myData = params['myData'];
      if (myData) {
        const data = JSON.parse(decodeURIComponent(myData));
        console.log('Dohvaćeni podaci:', data);
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
      this.paymentService.validateCard(this.paymentForm.value).subscribe(
        (response) => {

          console.log('Form is valid.');
        },
        (error) => {
          console.log('Form is not valid.');
        }
      );
    }
  }

}
