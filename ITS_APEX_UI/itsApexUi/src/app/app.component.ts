import { Component, OnInit } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { HeaderComponent } from './common/header/header.component';
import { FooterComponent } from './common/footer/footer.component';
import { HomeComponent } from './home/home.component';
import { MenuTabsComponent } from './common/menu-tabs/menu-tabs.component';
import { ItemCardComponent } from './common/item-card/item-card.component';
import { CommonModule } from '@angular/common';
import { LoginPageComponent } from './login-page/login-page.component';
import { AppServiceService } from './app-service.service';
import { GlobalService } from './service/global.service';
import { RegistrationComponent } from './retailer/registration/registration.component';


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
    LoginPageComponent,
    RegistrationComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  constructor(private router: Router, private service:AppServiceService, public globalService:GlobalService){}
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

    this.service.getUserDetails().subscribe(data=>{
      if(data!=null){
        this.globalService.userDetails = data;
        if(data.userRoles.length == 0 || data.userRoles[0].roleCd == 'C')
          this.router.navigate(['/consumer']);
        else if(data.userRoles[0].roleCd == 'S')
          this.router.navigate(['/seller']);
        else if(data.userRole[0].roleCd == 'D')
          this.router.navigate(['/deleivery']);
        else
          this.router.navigate(['/consumer']);
      }
    });
      
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