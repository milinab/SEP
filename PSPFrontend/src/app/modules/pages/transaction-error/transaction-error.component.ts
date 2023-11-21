import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-transaction-error',
  templateUrl: './transaction-error.component.html',
  styleUrls: ['./transaction-error.component.css']
})
export class TransactionErrorComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private formBuilder: FormBuilder) { }

  ngOnInit(): void {

  }

  goBack() {
    window.location.href = `http://localhost:4201/`;
  }
}





