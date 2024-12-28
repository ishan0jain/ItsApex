import { Component, NgModule } from '@angular/core';
import {MatToolbarModule} from '@angular/material/toolbar';
import { SearchBarComponent } from '../search-bar/search-bar.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  standalone: true,
  styleUrl: './header.component.css',
  providers: [],
  imports: [MatToolbarModule,SearchBarComponent],
  
})
// @NgModule({
//   declarations: [FlexAlignDirective],
//   imports: [FlexAlignDirective]
// })
export class HeaderComponent {

}
