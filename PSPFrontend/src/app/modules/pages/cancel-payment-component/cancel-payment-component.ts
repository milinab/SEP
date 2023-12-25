import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-cancel-payment',
  templateUrl: './cancel-payment-component.html',
  styleUrls: ['./cancel-payment-component.css']
})
export class CancelPaymentComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private formBuilder: FormBuilder) { }

  ngOnInit(): void {

  }

  goBack() {
    window.location.href = `http://localhost:4201/`;
  }
}





