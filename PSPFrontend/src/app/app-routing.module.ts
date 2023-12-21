import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { HomeComponent } from "./modules/pages/home/home.component";
import {CardPaymentComponent} from "./modules/pages/cardPayment/card-payment.component";
import {TransactionSuccessComponent} from "./modules/pages/transaction-success/transaction-success.component";
import {TransactionFailedComponent} from "./modules/pages/transaction-failed/transaction-failed.component";
import {TransactionErrorComponent} from "./modules/pages/transaction-error/transaction-error.component";
import { QRcodePaymentComponent } from "./modules/pages/qrcode-payment/qrcode-payment.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'card-payment', component: CardPaymentComponent },
  { path: 'transaction-success', component: TransactionSuccessComponent },
  { path: 'transaction-failed', component: TransactionFailedComponent },
  { path: 'transaction-error', component: TransactionErrorComponent },
  { path: 'qrcode-payment', component: QRcodePaymentComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
