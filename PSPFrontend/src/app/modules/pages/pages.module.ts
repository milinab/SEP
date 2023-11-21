import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { HomeComponent } from './home/home.component';
import {CardPaymentComponent} from "./cardPayment/card-payment.component";
import {MatIconModule} from "@angular/material/icon";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    HomeComponent,
    CardPaymentComponent
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
