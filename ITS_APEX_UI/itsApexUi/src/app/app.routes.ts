import { Routes } from '@angular/router';
import { BuyerComponent } from './buyer/buyer.component';
import { CarierComponent } from './carier/carier.component';
import { HomeComponent } from './home/home.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { RetailerComponent } from './retailer/retailer.component';
import { SellerComponent } from './seller/seller.component';
import { ShopDetailComponent } from './retailer/shop-detail/shop-detail.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
    },
    {
        path: 'login',
        component: LoginPageComponent,
    },
    {
        path: 'consumer', // child route path
        component: BuyerComponent,
    },
    { // child route component that the router renders
        path: 'delivery',
        component: CarierComponent, // another child route component that the router renders
    },
    { // child route component that the router renders
        path: 'retailer',
        component: RetailerComponent, // another child route component that the router renders
    },
    {
        path: 'retailer/shop/:shopId',
        component: ShopDetailComponent,
    },
    {
        path: 'seller',
        component: SellerComponent,
    },
    {
        path: '**',
        redirectTo: ''
    }
];
  
  
