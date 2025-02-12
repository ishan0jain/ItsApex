import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopImageUploadComponent } from './shop-image-upload.component';
import { MatOption, MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule, MatHint } from '@angular/material/form-field';

describe('ShopImageUploadComponent', () => {
  let component: ShopImageUploadComponent;
  let fixture: ComponentFixture<ShopImageUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShopImageUploadComponent
      ]
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
