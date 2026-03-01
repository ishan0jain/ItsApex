import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { AppServiceService } from '../../app-service.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgIf, NgFor } from '@angular/common';

@Component({
  selector: 'app-shop-detail',
  standalone: true,
  imports: [RouterModule, ReactiveFormsModule, NgIf, NgFor],
  templateUrl: './shop-detail.component.html',
  styleUrl: './shop-detail.component.css'
})
export class ShopDetailComponent implements OnInit {
  shop: any = null;
  products: any[] = [];
  orders: any[] = [];
  message = '';

  productForm = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
    category: new FormControl(''),
    tags: new FormControl(''),
    price: new FormControl(0),
    currency: new FormControl('INR'),
    quantityAvailable: new FormControl(0),
    imageUrl: new FormControl(''),
    active: new FormControl(true)
  });

  constructor(private route: ActivatedRoute, private api: AppServiceService) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const shopId = Number(params.get('shopId'));
      if (shopId) {
        this.loadShop(shopId);
        this.loadProducts(shopId);
        this.loadOrders(shopId);
      }
    });
  }

  loadShop(shopId: number) {
    this.api.getShop(shopId).subscribe({
      next: (data) => (this.shop = data),
      error: () => (this.shop = null)
    });
  }

  loadProducts(shopId: number) {
    this.api.getProducts(shopId).subscribe({
      next: (data) => (this.products = data || []),
      error: () => (this.products = [])
    });
  }

  loadOrders(shopId: number) {
    this.api.getOrders(shopId).subscribe({
      next: (data) => (this.orders = data || []),
      error: () => (this.orders = [])
    });
  }

  addProduct() {
    if (!this.shop?.shopId) {
      this.message = 'Shop not loaded.';
      return;
    }
    const payload = {
      ...this.productForm.value,
      shopId: this.shop.shopId
    };
    this.api.createProduct(payload).subscribe({
      next: () => {
        this.message = 'Product saved.';
        this.productForm.reset({ price: 0, quantityAvailable: 0, currency: 'INR', active: true });
        this.loadProducts(this.shop.shopId);
      },
      error: () => {
        this.message = 'Unable to save product.';
      }
    });
  }
}
