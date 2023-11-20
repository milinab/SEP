import {PaymentType} from "../enums/paymentType.enum";

export interface BuyRequest {
  merchantId: string
  amount: number
  merchantOrderId: number
  paymentType: PaymentType
}
