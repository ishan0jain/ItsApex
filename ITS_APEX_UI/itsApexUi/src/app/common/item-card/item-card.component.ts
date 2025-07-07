import { Component, Input } from '@angular/core';
import { Product } from '../../classes/product';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-item-card',
  standalone: true,
  imports: [MatCardModule,MatButtonModule,CommonModule],
  templateUrl: './item-card.component.html',
  styleUrl: './item-card.component.css'
})
export class ItemCardComponent {
  @Input() name: string = '';
  @Input() description: string = '';
  @Input() rating: number = 0;
  @Input() shop: string = '';
  @Input() price: number = 0;
  @Input() images: string[] = [];

  currentImageIndex = 0;
  get currentImage() {
    return this.images && this.images.length > 0 ? this.images[this.currentImageIndex] : '';
  }

  nextImage() {
    if (this.images.length > 0) {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.images.length;
    }
  }
  prevImage() {
    if (this.images.length > 0) {
      this.currentImageIndex = (this.currentImageIndex - 1 + this.images.length) % this.images.length;
    }
  }
}
