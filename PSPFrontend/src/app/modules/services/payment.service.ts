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
  headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });


  constructor(private http: HttpClient) {}

  validateCard(card: any): Observable<any> {
    return this.http.post<any>(this.pspBE, card).pipe(
      map(response => {
        return response === true;
      }),
      catchError(() => {
        return of(false);
      })
    );
  }

  payment(buyReq: BuyRequest): Observable<any>  {
    return this.http.post(this.psp + `api/payment/buy`, buyReq, {headers: this.headers})
  }
}
