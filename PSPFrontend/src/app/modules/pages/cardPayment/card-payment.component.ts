import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-home',
  templateUrl: './card-payment.component.html',
  styleUrls: ['./card-payment.component.css']
})
export class CardPaymentComponent implements OnInit {
  paymentForm!: FormGroup;
  submitted = false;

  constructor(private route: ActivatedRoute, private formBuilder: FormBuilder) { }

  displayedAmount: number = 748.56;

  ngOnInit(): void {
    this.paymentForm = this.formBuilder.group({
      cardHolderName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z\s]*$/)]], //letters
      cardNumber: ['', [Validators.required, Validators.pattern(/^\d{16}$/)]], //16-digit
      expiryDate: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/\d{2}$/)]], //MM/YY
      cvv: ['', [Validators.required, Validators.pattern(/^\d{3}$/)]] //3-digit
    });
  }

  get formControls() {
    return this.paymentForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.paymentForm.valid) {
      // Ovde možete izvršiti stvarnu transakciju
      console.log('Forma je validna. Izvršite transakciju.');
    } else {
      // Ovde možete prikazati poruke o greškama ili dodatne radnje za nevalidnu formu
      console.log('Forma nije validna. Proverite unos.');
    }
  }

}
