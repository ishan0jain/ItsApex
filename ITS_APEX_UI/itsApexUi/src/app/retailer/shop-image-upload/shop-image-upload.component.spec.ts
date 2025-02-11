import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopImageUploadComponent } from './shop-image-upload.component';

describe('ShopImageUploadComponent', () => {
  let component: ShopImageUploadComponent;
  let fixture: ComponentFixture<ShopImageUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShopImageUploadComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShopImageUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
