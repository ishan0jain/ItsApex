import { Component } from '@angular/core';
import { ItemCardComponent } from '../common/item-card/item-card.component';

@Component({
  selector: 'app-buyer',
  standalone: true,
  imports: [ItemCardComponent],
  templateUrl: './buyer.component.html',
  styleUrl: './buyer.component.css'
})
export class BuyerComponent {

}
