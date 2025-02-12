import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { BuyerComponent } from './buyer/buyer.component';
import { CarierComponent } from './carier/carier.component';
import { RetailerComponent } from './retailer/retailer.component';

export const routes: Routes = [
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
];
  
  
