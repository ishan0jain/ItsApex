import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  cards = [
    {
      title: 'Sell Products',
      description: 'List your products for sale and manage your inventory as a retailer.',
      route: '/retailer'
    },
    {
      title: 'Buy Products',
      description: 'Browse and purchase products from a wide range of categories as a consumer.',
      route: '/consumer'
    },
    {
      title: 'Deliver Products',
      description: 'Join as a carrier to deliver products and manage delivery tasks.',
      route: '/delivery'
    }
  ];

  constructor(private router: Router) {}

  goTo(route: string) {
    this.router.navigate([route]);
  }
}
