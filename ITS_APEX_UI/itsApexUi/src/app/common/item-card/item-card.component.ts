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

  onAdd() {
    this.addToCart.emit(this.product);
  }

  onBuy() {
    this.buyNow.emit(this.product);
  }
}
