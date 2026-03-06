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
  cartProduct: any = null;
  carts: any[] = [];
  showCartModal = false;
  newCartName = '';
  cartBusy = false;
  cartDrawerOpen = false;
  cartLoading = false;
  activeCartId: number | null = null;
  activeCartName = '';
  cartItems: any[] = [];
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
    this.cartProduct = product;
    this.orderMessage = '';
    this.cartBusy = true;
    this.api.getCarts().subscribe({
      next: (data) => {
        this.carts = data || [];
        if (this.carts.length <= 1) {
          const targetCartId = this.carts[0]?.cartId;
          this.addItemToCart(targetCartId);
        } else {
          this.showCartModal = true;
          this.cartBusy = false;
        }
      },
      error: () => {
        this.cartBusy = false;
        this.orderMessage = 'Sign in to use carts.';
      }
    });
  }

  openCartDrawer() {
    this.cartDrawerOpen = true;
    this.cartLoading = true;
    this.api.getCarts().subscribe({
      next: (data) => {
        this.carts = data || [];
        const defaultCart = this.carts.find((cart) => cart.defaultCart);
        const activeId = defaultCart?.cartId || this.carts[0]?.cartId || null;
        this.activeCartId = activeId;
        this.activeCartName = defaultCart?.cartName || this.carts[0]?.cartName || '';
        if (activeId) {
          this.loadCart(activeId);
        } else {
          this.cartItems = [];
          this.cartLoading = false;
        }
      },
      error: () => {
        this.cartLoading = false;
        this.orderMessage = 'Sign in to use carts.';
      }
    });
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

  addItemToCart(cartId?: number) {
    if (!this.cartProduct) {
      this.cartBusy = false;
      return;
    }
    const payload = {
      productId: this.cartProduct.productId,
      quantity: 1
    };
    const request = cartId
      ? this.api.addCartItem(cartId, payload)
      : this.api.addCartItemToDefault(payload);
    request.subscribe({
      next: () => {
        this.orderMessage = 'Added to cart.';
        this.cartBusy = false;
        this.showCartModal = false;
        this.cartProduct = null;
        this.newCartName = '';
        if (this.cartDrawerOpen && (cartId || this.activeCartId)) {
          this.loadCart(cartId || this.activeCartId!);
        }
      },
      error: () => {
        this.cartBusy = false;
        this.orderMessage = 'Unable to add to cart.';
      }
    });
  }

  createCart() {
    const name = this.newCartName.trim();
    if (!name) {
      return;
    }
    this.api.createCart({ name }).subscribe({
      next: (cart) => {
        this.carts = [...this.carts, cart];
        this.newCartName = '';
        this.activeCartId = cart?.cartId || this.activeCartId;
        this.activeCartName = cart?.cartName || this.activeCartName;
        if (this.activeCartId) {
          this.loadCart(this.activeCartId);
        }
      },
      error: () => {
        this.orderMessage = 'Unable to create cart.';
      }
    });
  }

  closeCartModal() {
    this.showCartModal = false;
    this.cartProduct = null;
    this.cartBusy = false;
  }

  closeCartDrawer() {
    this.cartDrawerOpen = false;
  }

  selectCart(cartId: number) {
    this.activeCartId = cartId;
    const cart = this.carts.find((c) => c.cartId === cartId);
    this.activeCartName = cart?.cartName || '';
    this.loadCart(cartId);
  }

  loadCart(cartId: number) {
    this.cartLoading = true;
    this.api.getCart(cartId).subscribe({
      next: (cart) => {
        this.cartItems = cart?.items || [];
        this.cartLoading = false;
      },
      error: () => {
        this.cartItems = [];
        this.cartLoading = false;
      }
    });
  }

  cartItemCount() {
    const defaultCart = this.carts.find((cart) => cart.defaultCart);
    return defaultCart?.itemCount || 0;
  }

  isActiveCart(cartId: number) {
    return this.activeCartId === cartId;
  }

  truncateCartName(name: string) {
    const trimmed = (name || '').trim();
    if (!trimmed) {
      return 'Cart';
    }
    return trimmed.length > 30 ? `${trimmed.slice(0, 30)}...` : trimmed;
  }
}
