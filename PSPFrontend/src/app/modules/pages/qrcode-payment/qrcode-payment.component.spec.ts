import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QRcodePaymentComponent } from './qrcode-payment.component';

describe('QRcodePaymentComponent', () => {
  let component: QRcodePaymentComponent;
  let fixture: ComponentFixture<QRcodePaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QRcodePaymentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QRcodePaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
