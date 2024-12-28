import { NgFor } from '@angular/common';
import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import {MatTabsModule} from '@angular/material/tabs';
import { modulesObject } from '../../app.component';
import { UserGlobal } from '../../classes/user-global';
import { Router, RouterModule, RouterOutlet } from '@angular/router';




@Component({
  selector: 'app-menu-tabs',
  standalone: true,
  imports: [MatTabsModule,NgFor,RouterModule,RouterOutlet],
  templateUrl: './menu-tabs.component.html',
  styleUrl: './menu-tabs.component.css',
})
export class MenuTabsComponent implements OnInit{
  @Input({ required: true }) 
  tabsList!: Array<modulesObject>;
  usrGlbl : UserGlobal = new UserGlobal();
  constructor(private router : Router){

  }

  ngOnInit(): void {
    this.router.navigate([this.tabsList[0].id]);
  }
  onClick(event: any){
    console.log(event);
    this.router.navigate([this.tabsList[event.index].id]);
  }


}
