import { ComponentFixture, TestBed } from '@angular/core/testing';
import {CapturePaymentComponent} from "./capture-payment-component";


describe('CapturePaymentComponent', () => {
  let component: CapturePaymentComponent;
  let fixture: ComponentFixture<CapturePaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CapturePaymentComponent ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CapturePaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
