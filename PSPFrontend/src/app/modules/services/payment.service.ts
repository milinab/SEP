import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = 'http://localhost:8080/api/payment'; 

  constructor(private http: HttpClient) {}

  calculateAmount(agencyAmount: number) {
    console.log('Calculating and processing amount on Payment Service:', agencyAmount);
    return agencyAmount;
  }
}
