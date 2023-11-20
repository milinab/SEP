export class Card {
  pan: string = '';
  expDate: string = '';
  cvv: string = '';
  cardHolderName: string = '';

  public constructor(obj?: any) {
    if (obj) {
      this.pan = obj.pan;
      this.expDate = obj.expDate;
      this.cvv = obj.cvv;
      this.cardHolderName = obj.cardHolderName;
    }
  }
}
