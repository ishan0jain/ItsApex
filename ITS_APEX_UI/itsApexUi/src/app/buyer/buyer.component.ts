import { Component, OnInit } from '@angular/core';
import { ItemCardComponent } from '../common/item-card/item-card.component';
import { AppServiceService } from '../app-service.service';
import { FormControl, FormGroup, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-buyer',
  standalone: true,
  imports: [ItemCardComponent, ReactiveFormsModule, FormsModule, NgFor, NgIf],
  templateUrl: './buyer.component.html',
  styleUrl: './buyer.component.css'
})
export class BuyerComponent implements OnInit {
  products: any[] = [];
  selectedProduct: any = null;
  searchTerm = '';
  orderForm = new FormGroup({
    buyerName: new FormControl(''),
    buyerPhone: new FormControl(''),
    deliveryAddress: new FormControl(''),
    deliveryNotes: new FormControl(''),
    quantity: new FormControl(1),
    customNote: new FormControl('')
  });
  orderMessage = '';

  constructor(private api: AppServiceService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts() {
    this.api.getPublicProducts().subscribe({
      next: (data) => (this.products = data || []),
      error: () => (this.products = [])
    });
  }

  filteredProducts() {
    if (!this.searchTerm) {
      return this.products;
    }
    const term = this.searchTerm.toLowerCase();
    return this.products.filter((p) =>
      `${p?.name || ''} ${p?.description || ''} ${p?.category || ''}`.toLowerCase().includes(term)
    );
  }

  onBuy(product: any) {
    this.selectedProduct = product;
    this.orderMessage = '';
  }

  onAdd(product: any) {
    this.selectedProduct = product;
    this.orderMessage = 'Cart feature is queued. Use quick order for now.';
  }

  placeOrder() {
    if (!this.selectedProduct) {
      return;
    }
    const formValue: any = this.orderForm.value;
    const payload = {
      shopId: this.selectedProduct.shopId || this.selectedProduct.shop?.shopId,
      buyerName: formValue.buyerName,
      buyerPhone: formValue.buyerPhone,
      deliveryAddress: formValue.deliveryAddress,
      deliveryNotes: formValue.deliveryNotes,
      items: [
        {
          productId: this.selectedProduct.productId,
          quantity: formValue.quantity || 1,
          customNote: formValue.customNote
        }
      ]
    };
    if (!payload.shopId) {
      this.orderMessage = 'Unable to place order. Shop is missing for this product.';
      return;
    }
    this.api.createOrder(payload).subscribe({
      next: () => {
        this.orderMessage = 'Order placed. You will receive delivery assignment updates shortly.';
        this.orderForm.reset({ quantity: 1 });
        this.selectedProduct = null;
        this.loadProducts();
      },
      error: () => {
        this.orderMessage = 'Order could not be created. Please check stock and try again.';
      }
    });
  }
}
