import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-transaction-failed',
  templateUrl: './transaction-failed.component.html',
  styleUrls: ['./transaction-failed.component.css']
})
export class TransactionFailedComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private formBuilder: FormBuilder) { }

  ngOnInit(): void {

  }

  goBack() {
    window.location.href = `http://localhost:4201/`;
  }
}





