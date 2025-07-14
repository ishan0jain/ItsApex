import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { BuyerComponent } from './buyer/buyer.component';
import { CarierComponent } from './carier/carier.component';
import { RetailerComponent } from './retailer/retailer.component';
import { HomeComponent } from './home/home.component';
import { SellerComponent } from './seller/seller.component';
import { RegistrationPageComponent } from './registration-page/registration-page.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
        pathMatch: 'full',
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
        component: SellerComponent, // another child route component that the router renders
    },
    {
        path: 'register',
        component: RegistrationPageComponent
    },
];
  
  
