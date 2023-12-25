import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-transaction-error',
  templateUrl: './transaction-error.component.html',
  styleUrls: ['./transaction-error.component.css']
})
export class TransactionErrorComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  goBack() {
    window.location.href = `http://localhost:4201/`;
  }
}





