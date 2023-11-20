import {Card} from "./card.model";

export class Transaction {
  paymentId: number = 0;
  card: Card = new Card();

  public constructor(obj?: any) {
    if (obj) {
      this.paymentId = obj.paymentId;
      this.card = obj.card;
    }
  }
}
