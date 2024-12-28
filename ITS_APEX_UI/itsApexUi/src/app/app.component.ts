import { Component, OnInit } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { HeaderComponent } from './common/header/header.component';
import { FooterComponent } from './common/footer/footer.component';
import { HomeComponent } from './home/home.component';
import { MenuTabsComponent } from './common/menu-tabs/menu-tabs.component';
import { ItemCardComponent } from './common/item-card/item-card.component';
import { CommonModule } from '@angular/common';
import { LoginPageComponent } from './login-page/login-page.component';
import { AppServiceService } from './app-service.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,
    HeaderComponent,
    FooterComponent,
    RouterModule,
    HomeComponent,
    MenuTabsComponent,
    ItemCardComponent,
    CommonModule,
    LoginPageComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  constructor(private service:AppServiceService){}
  title = 'itsApexUi';
  tabList : Array<modulesObject> =[];
  a:modulesObject = {
    id: "app-carier",
    name: "Carier"
  }
  b:modulesObject = {
    id: "app-buyer",
    name: "Buyer"
  }
  popUp: boolean =false;
  ngOnInit(){
    this.service.loginService();
    // this.service.routingService("A");
    
    this.tabList.push(this.a);
    this.tabList.push(this.b);
    this.tabList.push(this.b);
    this.tabList.push(this.b);
    this.tabList.push(this.b);
    this.tabList.push(this.b);
    this.tabList.push(this.b);
    
  }

}
export class modulesObject{
  name!: String;
  id! :String;
}