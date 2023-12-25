import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder} from "@angular/forms";
import {PaymentService} from "../../services/payment.service";
import {CompletedOrder} from "../../dtos/completedOrder";

@Component({
  selector: 'app-capture-payment',
  templateUrl: './capture-payment-component.html',
  styleUrls: ['./capture-payment-component.css']
})
export class CapturePaymentComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private paymentService: PaymentService,
              private router: Router) { }

  completedOrder: CompletedOrder | undefined
  ngOnInit(): void {
    const currentUrl = new URL(window.location.href);
    const token = currentUrl.searchParams.get('token');
    this.paymentService.completePayment(token).subscribe(
      (response) => {
        this.completedOrder = response;
        if("success" == this.completedOrder?.status) {
          this.router.navigate(['/transaction-success'])
        } else {
          this.router.navigate(['/transaction-failed'])
        }
      },
      (error) => {
        console.log('Account data not valid.');
      }
    );
  }
}





