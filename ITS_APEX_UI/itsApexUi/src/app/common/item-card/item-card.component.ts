import { Component, Input } from '@angular/core';
import { Product } from '../../classes/product';
import {MatCardModule} from '@angular/material/card';

@Component({
  selector: 'app-item-card',
  standalone: true,
  imports: [MatCardModule],
  templateUrl: './item-card.component.html',
  styleUrl: './item-card.component.css'
})
export class ItemCardComponent {
}
