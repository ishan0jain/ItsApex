import { Component, OnInit } from '@angular/core';
import { RegistrationComponent } from './registration/registration.component';
import { AppServiceService } from '../app-service.service';
import { NgFor, NgIf } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-retailer',
  standalone: true,
  imports: [RegistrationComponent, NgFor, NgIf, RouterModule],
  templateUrl: './retailer.component.html',
  styleUrl: './retailer.component.css'
})
export class RetailerComponent implements OnInit {
  shops: any[] = [];

  constructor(private api: AppServiceService, private router: Router) {}

  ngOnInit(): void {
    this.loadShops();
  }

  loadShops() {
    this.api.getMyShops().subscribe({
      next: (data) => {
        this.shops = data || [];
      },
      error: () => (this.shops = [])
    });
  }

  openShop(shopId: number) {
    this.router.navigate(['/retailer/shop', shopId]);
  }
}
