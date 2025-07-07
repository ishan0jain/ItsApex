import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface SellerProduct {
  name: string;
  description: string;
  price: number;
  quantity: number;
  image: string;
  profitMargin: number;
  soldLastMonth: number;
}

@Component({
  selector: 'app-seller',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './seller.component.html',
  styleUrl: './seller.component.css'
})
export class SellerComponent {
  products: SellerProduct[] = [
    {
      name: 'Apple iPhone 15 Pro',
      description: 'Latest iPhone with A17 chip, 120Hz display, and titanium frame.',
      price: 129999,
      quantity: 12,
      image: 'assets/iphone1.jpg',
      profitMargin: 18,
      soldLastMonth: 7
    },
    {
      name: 'Samsung Galaxy S24 Ultra',
      description: 'Flagship Samsung phone with 200MP camera and S Pen.',
      price: 119999,
      quantity: 8,
      image: 'assets/galaxy1.jpg',
      profitMargin: 20,
      soldLastMonth: 5
    },
    {
      name: 'Sony WH-1000XM5',
      description: 'Industry-leading noise cancelling headphones.',
      price: 29999,
      quantity: 20,
      image: 'assets/sony1.jpg',
      profitMargin: 25,
      soldLastMonth: 15
    },
    {
      name: 'Dell XPS 13',
      description: 'Ultra-portable laptop with InfinityEdge display.',
      price: 99999,
      quantity: 5,
      image: 'assets/dell1.jpg',
      profitMargin: 15,
      soldLastMonth: 2
    },
    {
      name: 'Apple Watch Series 9',
      description: 'Smartwatch with advanced health features.',
      price: 45999,
      quantity: 10,
      image: 'assets/applewatch1.jpg',
      profitMargin: 22,
      soldLastMonth: 6
    },
    {
      name: 'Canon EOS R10',
      description: 'Mirrorless camera for creators and vloggers.',
      price: 74999,
      quantity: 4,
      image: 'assets/canon1.jpg',
      profitMargin: 17,
      soldLastMonth: 1
    },
    {
      name: 'Nike Air Max 270',
      description: 'Comfortable and stylish running shoes.',
      price: 12999,
      quantity: 30,
      image: 'assets/nike1.jpg',
      profitMargin: 30,
      soldLastMonth: 18
    },
    {
      name: 'Bose SoundLink Flex',
      description: 'Portable Bluetooth speaker with deep bass.',
      price: 15999,
      quantity: 14,
      image: 'assets/bose1.jpg',
      profitMargin: 28,
      soldLastMonth: 9
    },
    {
      name: 'Fitbit Charge 6',
      description: 'Fitness tracker with heart rate and GPS.',
      price: 8999,
      quantity: 25,
      image: 'assets/fitbit1.jpg',
      profitMargin: 35,
      soldLastMonth: 12
    },
    {
      name: 'HP DeskJet 2331',
      description: 'All-in-one color printer for home use.',
      price: 4999,
      quantity: 11,
      image: 'assets/hp1.jpg',
      profitMargin: 12,
      soldLastMonth: 3
    }
  ];
  newProduct: SellerProduct = {
    name: '',
    description: '',
    price: 0,
    quantity: 0,
    image: '',
    profitMargin: 0,
    soldLastMonth: 0
  };

  showForm = false;
  selectedProduct: SellerProduct | null = null;

  openForm() {
    this.showForm = true;
  }
  closeForm() {
    this.showForm = false;
  }

  addProduct() {
    this.products.push({ ...this.newProduct });
    this.newProduct = {
      name: '',
      description: '',
      price: 0,
      quantity: 0,
      image: '',
      profitMargin: 0,
      soldLastMonth: 0
    };
    this.closeForm();
  }

  viewProduct(product: SellerProduct) {
    this.selectedProduct = product;
  }

  closeProductView() {
    this.selectedProduct = null;
  }
}
