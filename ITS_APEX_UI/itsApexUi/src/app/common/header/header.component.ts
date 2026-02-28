import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgIf } from '@angular/common';
import { AppServiceService } from '../../app-service.service';
import { GlobalService } from '../../service/global.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  standalone: true,
  styleUrl: './header.component.css',
  providers: [],
  imports: [RouterModule, NgIf],
})
// @NgModule({
//   declarations: [FlexAlignDirective],
//   imports: [FlexAlignDirective]
// })
export class HeaderComponent {
  constructor(private appService: AppServiceService, public globalService: GlobalService) {}

  logout() {
    this.appService.logout().subscribe({
      next: () => {
        this.globalService.userDetails = null;
      },
      error: () => {
        this.globalService.userDetails = null;
      }
    });
  }
}
