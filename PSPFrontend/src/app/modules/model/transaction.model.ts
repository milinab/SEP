import {Card} from "./card.model";

export class Transaction {
  paymentId: number = 0;
  pan: string = '';
  expDate: string = '';
  cvv: string = '';
  cardHolderName: string = '';

  public constructor(obj?: any) {
    if (obj) {
      this.paymentId = obj.paymentId;
      this.pan = obj.pan;
      this.expDate = obj.expDate;
      this.cvv = obj.cvv;
      this.cardHolderName = obj.cardHolderName;
    }
  }
}
