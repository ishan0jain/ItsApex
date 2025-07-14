import { Component, Input } from '@angular/core';
import {MatToolbarModule} from '@angular/material/toolbar';
import { SearchBarComponent } from '../search-bar/search-bar.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  standalone: true,
  styleUrl: './header.component.css',
  providers: [],
  imports: [MatToolbarModule,SearchBarComponent, CommonModule],
})
// @NgModule({
//   declarations: [FlexAlignDirective],
//   imports: [FlexAlignDirective]
// })
export class HeaderComponent {
  @Input() username: string = 'Ishan';
  @Input() cartCount: number = 2; // Example default
  @Input() notificationCount: number = 0;

  goHome() {
    window.location.href = '/';
  }
}
