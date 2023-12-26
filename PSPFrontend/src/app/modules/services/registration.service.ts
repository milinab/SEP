import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private psp = 'http://localhost:8080/';
  headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  registrate(client: any): Observable<any>  {
    return this.http.post(this.psp + `api/payment/payment-methods`, client)
  }

  getClient(merchantId: string): Observable<any> {
    return this.http.post(this.psp + `api/payment/getClient`, merchantId)
  }
}
