import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, map, Observable, of, tap} from "rxjs";
import {BuyRequest} from "../dtos/buyRequest";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private pspBE = 'http://localhost:8080/api/payment/validateCard';
  private psp = 'http://localhost:8080/';
  private primaryBank = 'http://localhost:8081/';
  private apiGateway = 'http://localhost:8084/';
  private crypto = 'http://localhost:8085/'
  headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });


  constructor(private http: HttpClient) {}

  validateCard(card: any): Observable<any>  {
    return this.http.post(this.psp + `api/payment/validate`, card)
  }

  payment(buyReq: any): Observable<any>  {
    return this.http.post(this.apiGateway + `api/proxy/buy`, buyReq)
  }

  pay(transaction: any): Observable<any> {
    return this.http.post(this.apiGateway + `api/proxy/pay`, transaction)
  }

  completePayment(token: any): Observable<any> {
    return this.http.post(this.apiGateway + 'api/proxy/completePaypalPayment', token)
  }

}
