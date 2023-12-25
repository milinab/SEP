import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { HomeComponent } from './home/home.component';
import {CardPaymentComponent} from "./cardPayment/card-payment.component";
import {MatIconModule} from "@angular/material/icon";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TransactionSuccessComponent} from "./transaction-success/transaction-success.component";
import {TransactionFailedComponent} from "./transaction-failed/transaction-failed.component";
import {TransactionErrorComponent} from "./transaction-error/transaction-error.component";
import { QRcodePaymentComponent } from './qrcode-payment/qrcode-payment.component';
import {CapturePaymentComponent} from "./capture-payment-component/capture-payment-component";
import {CancelPaymentComponent} from "./cancel-payment-component/cancel-payment-component";

@NgModule({
  declarations: [
    HomeComponent,
    CardPaymentComponent,
    TransactionSuccessComponent,
    TransactionFailedComponent,
    TransactionErrorComponent,
    QRcodePaymentComponent,
    CapturePaymentComponent,
    CancelPaymentComponent
  ],
    imports: [
        CommonModule,
        AppRoutingModule,
        MatIconModule,
        ReactiveFormsModule,
        FormsModule,
    ]
})
export class PagesModule { }
