import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './common/header/header.component';
import { FooterComponent } from './common/footer/footer.component';
import { CommonModule } from '@angular/common';
import { AppServiceService } from './app-service.service';
import { GlobalService } from './service/global.service';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,
    HeaderComponent,
    FooterComponent,
    CommonModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  constructor(private service:AppServiceService, public globalService:GlobalService){}
  title = 'itsApexUi';
  ngOnInit(){
    this.service.getCurrentUser().subscribe({
      next: (data: any) => {
        if (data) {
          this.globalService.userDetails = data;
        }
      },
      error: () => {
        this.globalService.userDetails = null;
      }
    });
  }

}
