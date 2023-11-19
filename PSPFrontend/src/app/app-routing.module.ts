import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { HomeComponent } from "./modules/pages/home/home.component";
import {CardPaymentComponent} from "./modules/pages/cardPayment/card-payment.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'card-payment', component: CardPaymentComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
