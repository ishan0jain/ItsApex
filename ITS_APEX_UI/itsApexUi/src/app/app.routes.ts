import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { BuyerComponent } from './buyer/buyer.component';
import { CarierComponent } from './carier/carier.component';

export const routes: Routes = [
    {
        path: 'app-buyer', // child route path
        component: BuyerComponent,
    },
    { // child route component that the router renders
        path: 'app-carier',
        component: CarierComponent, // another child route component that the router renders
    },
];
  
  
