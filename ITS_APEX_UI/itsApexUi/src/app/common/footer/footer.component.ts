import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  standalone: true,
  styleUrl: './footer.component.css',
  imports: [CommonModule]
})
export class FooterComponent {
  popUp: boolean =false;

  onclick(){
    console.log("clicked")
    this.popUp =!this.popUp;
  }
  
}

