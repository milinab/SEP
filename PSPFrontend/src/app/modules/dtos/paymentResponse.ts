import {PaymentService} from "../services/payment.service";
import {PaymentStatus} from "../enums/paymentStatus.enum";

export interface PaymentResponse {
  merchantOrderId: number
  acquirerOrderId: number
  acquirerTimestamp: Date
  paymentStatus: string
}
