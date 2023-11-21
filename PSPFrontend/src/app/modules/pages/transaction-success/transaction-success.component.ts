import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-transaction-success',
  templateUrl: './transaction-success.component.html',
  styleUrls: ['./transaction-success.component.css']
})
export class TransactionSuccessComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private formBuilder: FormBuilder) { }

  ngOnInit(): void {

  }

}





