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
  editingProductId: number | null = null;
  stockEdits: Record<number, string> = {};
  updatingStockId: number | null = null;
  apiBase = 'http://localhost:8080';

  productForm = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
    category: new FormControl(''),
    tags: new FormControl(''),
    price: new FormControl(0),
    currency: new FormControl('INR'),
    sellQuantity: new FormControl(1),
    sellUnit: new FormControl('number'),
    stockQuantity: new FormControl(0),
    stockUnit: new FormControl('number'),
    imageUrl: new FormControl(''),
  });

  unitOptions = ['number', 'kg', 'g', 'mg', 'liter', 'ml', 'lb', 'oz', 'piece', 'pack', 'dozen'];

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
      next: (data) => {
        const allProducts = data || [];
        this.products = allProducts.filter((product: any) => product?.active !== false);
        this.products.forEach((product) => {
          const productId = product?.productId;
          if (productId !== null && productId !== undefined) {
            const currentStock = product?.stockQuantity ?? product?.quantityAvailable ?? 0;
            this.stockEdits[productId] = `${currentStock}`;
          }
        });
      },
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
    if (this.editingProductId) {
      this.api.updateProduct(this.editingProductId, payload).subscribe({
        next: () => {
          this.message = 'Product updated.';
          this.resetProductForm();
          this.loadProducts(this.shop.shopId);
        },
        error: () => {
          this.message = 'Unable to update product.';
        }
      });
      return;
    }
    this.api.createProduct(payload).subscribe({
      next: () => {
        this.message = 'Product saved.';
        this.resetProductForm();
        this.loadProducts(this.shop.shopId);
      },
      error: () => {
        this.message = 'Unable to save product.';
      }
    });
  }

  editProduct(product: any) {
    this.editingProductId = product?.productId ?? null;
    this.productForm.patchValue({
      name: product?.name ?? '',
      description: product?.description ?? '',
      category: product?.category ?? '',
      tags: product?.tags ?? '',
      price: product?.price ?? 0,
      currency: product?.currency ?? 'INR',
      sellQuantity: product?.sellQuantity ?? 1,
      sellUnit: product?.sellUnit ?? product?.stockUnit ?? 'number',
      stockQuantity: product?.stockQuantity ?? product?.quantityAvailable ?? 0,
      stockUnit: product?.stockUnit ?? product?.sellUnit ?? 'number',
      imageUrl: product?.imageUrl ?? ''
    });
  }

  deleteProduct(product: any) {
    if (!product?.productId) {
      return;
    }
    this.api.updateProduct(product.productId, { active: false }).subscribe({
      next: () => {
        this.message = 'Product removed.';
        this.loadProducts(this.shop.shopId);
      },
      error: () => {
        this.message = 'Unable to remove product.';
      }
    });
  }

  getProductImageUrl(product: any): string {
    const imageId = product?.productImages?.[0]?.imageId;
    if (imageId && product?.productId) {
      return `${this.apiBase}/products/${product.productId}/images/${imageId}`;
    }
    return product?.imageUrl || '/assets/OIF.jpg';
  }

  uploadProductImage(product: any, event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input?.files?.[0];
    if (!file || !product?.productId) {
      return;
    }
    this.api.uploadProductImage(product.productId, file).subscribe({
      next: () => {
        this.message = 'Image uploaded.';
        this.loadProducts(this.shop.shopId);
        input.value = '';
      },
      error: () => {
        this.message = 'Unable to upload image.';
        input.value = '';
      }
    });
  }

  setStockEdit(productId: number, value: string) {
    this.stockEdits[productId] = value;
  }

  updateStock(product: any) {
    if (!product?.productId) {
      return;
    }
    const rawValue = this.stockEdits[product.productId];
    const parsed = rawValue === undefined || rawValue === null || rawValue === '' ? null : Number(rawValue);
    if (parsed === null || Number.isNaN(parsed)) {
      this.message = 'Enter a valid stock quantity.';
      return;
    }
    const payload = {
      stockQuantity: parsed,
      stockUnit: product?.stockUnit ?? product?.sellUnit ?? 'number'
    };
    this.updatingStockId = product.productId;
    this.api.updateProduct(product.productId, payload).subscribe({
      next: () => {
        this.message = 'Stock updated.';
        this.updatingStockId = null;
        this.loadProducts(this.shop.shopId);
      },
      error: () => {
        this.message = 'Unable to update stock.';
        this.updatingStockId = null;
      }
    });
  }

  cancelEdit() {
    this.resetProductForm();
  }

  private resetProductForm() {
    this.editingProductId = null;
    this.productForm.reset({
      price: 0,
      sellQuantity: 1,
      sellUnit: 'number',
      stockQuantity: 0,
      stockUnit: 'number',
      currency: 'INR'
    });
  }
}
