import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatOption, MatSelectModule} from '@angular/material/select';
import { NgFor } from '@angular/common';
import { GlobalService } from '../../service/global.service';
import { ShopImageUploadComponent } from '../shop-image-upload/shop-image-upload.component';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [NgFor,ReactiveFormsModule,MatFormFieldModule,ShopImageUploadComponent, MatInputModule,MatButtonModule,MatSelectModule,MatOption],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {

  constructor(private glblServie:GlobalService){}

  shopTypes: Array<String>  = Object.keys(ShopType)
  retailerForm: FormGroup = new FormGroup({
    retailerFirstName: new FormControl(''),
    retailerLastName: new FormControl(''),
    shopName: new FormControl(''),
    shopDesc: new FormControl(''),
    shopType: new FormControl(''),
    address: new FormControl(''),
    landmark: new FormControl(''),
    city: new FormControl(''),
    state: new FormControl(''),
    pinCode: new FormControl('')
  });

  
  onClick(){
    this.glblServie.registerShop(this.retailerForm.value).subscribe(data=>{

    })
  }
  onSubmit(){
  }
}
enum ShopType{
  'Medical',
  'Stationary',
  'Hand Craft',
  'Restraurant',
  'Bakery',
  'Grocery',
  'Clothes',
  'Sports',
  'Toy Shop',
  'Vegetable Market'
}
