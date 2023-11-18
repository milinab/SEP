import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, map, Observable} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})

export class OfferService {

  headers: HttpHeaders = new HttpHeaders({'Content-Type': 'application/json'});
  constructor(private http: HttpClient, private router: Router) {
  }

  private apiUrl = 'http://localhost:8080/api/payment';

  // calculateAmount(agencyAmount: number): Observable<any> {
  //   return this.http.post(`${this.apiUrl}/calculateAmount`, agencyAmount).pipe(
  //     catchError(error => {
  //       console.error('Error:', error);
  //       throw error;
  //     })
  //   );
  // } SALJE NA BE PSP DOBRO

  calculateAmount(agencyAmount: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/calculateAmount`, agencyAmount, { observe: 'response' })
      .pipe(
        map((response) => {
          if (response.status === 302) {
            const redirectUrl = response.headers.get('Location');
            console.log("redirectttttt:",redirectUrl);
            window.location.replace(redirectUrl!);
          }
        }),
        catchError((error) => {
          console.error('Error:', error);
          throw error;
        })
      );
  }

}
