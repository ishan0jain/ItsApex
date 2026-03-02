import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgIf } from '@angular/common';


@Component({
  selector: 'app-item-card',
  standalone: true,
  imports: [NgIf],
  templateUrl: './item-card.component.html',
  styleUrl: './item-card.component.css'
})
export class ItemCardComponent {
  @Input() product: any;
  @Output() addToCart = new EventEmitter<any>();
  @Output() buyNow = new EventEmitter<any>();
  apiBase = 'http://localhost:8080';

  onAdd() {
    this.addToCart.emit(this.product);
  }

  onBuy() {
    this.buyNow.emit(this.product);
  }

  getProductImageUrl(): string {
    const imageId = this.product?.productImages?.[0]?.imageId;
    if (imageId && this.product?.productId) {
      return `${this.apiBase}/products/${this.product.productId}/images/${imageId}`;
    }
    return this.product?.imageUrl || '../../../assets/OIF.jpg';
  }
}
