import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { AppServiceService } from '../../app-service.service';
import { ShopImageUploadComponent } from '../shop-image-upload/shop-image-upload.component';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [NgFor, NgIf, ReactiveFormsModule, ShopImageUploadComponent],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent implements OnInit {
  @Output() shopCreated = new EventEmitter<void>();

  shopTypes: Array<string>  = [
    'Medical',
    'Stationary',
    'Hand Craft',
    'Restaurant',
    'Bakery',
    'Grocery',
    'Clothes',
    'Sports',
    'Toy Shop',
    'Vegetable Market'
  ];
  dynamicFields: any[] = [];
  dynamicKeys: string[] = [];
  selectedFiles: File[] = [];
  message = '';

  retailerForm: FormGroup = new FormGroup({
    retailerFirstName: new FormControl(''),
    retailerLastName: new FormControl(''),
    shopName: new FormControl(''),
    shopType: new FormControl('Grocery'),
    description: new FormControl(''),
    tags: new FormControl(''),
    address: new FormControl(''),
    landmark: new FormControl(''),
    city: new FormControl(''),
    state: new FormControl(''),
    pinCode: new FormControl(''),
    contactPhone: new FormControl(''),
    latitude: new FormControl(''),
    longitude: new FormControl(''),
    radius: new FormControl(0)
  });

  constructor(private api: AppServiceService){}

  ngOnInit(): void {
    this.loadFields(this.retailerForm.value.shopType || 'Grocery');
    this.retailerForm.get('shopType')?.valueChanges.subscribe(value => {
      this.loadFields(value);
    });
  }

  loadFields(shopType: string) {
    this.api.getRegistrationFields(shopType).subscribe({
      next: (fields) => {
        this.dynamicFields = fields || [];
        this.dynamicKeys.forEach(key => this.retailerForm.removeControl(key));
        this.dynamicKeys = [];
        this.dynamicFields.forEach((field: any) => {
          if (!this.retailerForm.contains(field.fieldKey)) {
            this.retailerForm.addControl(field.fieldKey, new FormControl(''));
            this.dynamicKeys.push(field.fieldKey);
          }
        });
      },
      error: () => {
        this.dynamicFields = [];
      }
    });
  }

  onFilesSelected(files: File[]) {
    this.selectedFiles = files;
  }

  submit() {
    this.message = '';
    const value: any = this.retailerForm.value;
    const payload = {
      retaileFirstName: value.retailerFirstName,
      retailerLastName: value.retailerLastName,
      shopName: value.shopName,
      shopType: value.shopType,
      description: value.description,
      address: value.address,
      landmark: value.landmark,
      city: value.city,
      state: value.state,
      pinCode: value.pinCode,
      contactPhone: value.contactPhone,
      latitude: value.latitude,
      longitude: value.longitude,
      radius: value.radius,
      tags: value.tags
    };
    this.api.registerShop(payload).subscribe({
      next: (shop) => {
        const answers = this.dynamicFields.map(field => ({
          fieldKey: field.fieldKey,
          fieldValue: value[field.fieldKey]
        }));
        if (answers.length > 0) {
          this.api.saveRegulationAnswers({
            shopId: shop.shopId,
            answers
          }).subscribe();
        }
        if (this.selectedFiles.length > 0) {
          this.selectedFiles.forEach(file => {
            this.api.uploadShopImage(shop.shopId, file).subscribe();
          });
        }
        this.message = 'Shop registered. Inventory setup is ready.';
        this.shopCreated.emit();
        this.retailerForm.reset({ shopType: 'Grocery' });
        this.selectedFiles = [];
        this.loadFields('Grocery');
      },
      error: () => {
        this.message = 'Unable to register shop. Please review the form.';
      }
    });
  }
}
