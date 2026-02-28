import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { GlobalService } from '../service/global.service';
import { AppServiceService } from '../app-service.service';
import { Router, RouterModule } from '@angular/router';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, RouterModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {
  constructor(private globalService:GlobalService, private appSrvc :AppServiceService, private router:Router){}
  fg : FormGroup = new FormGroup({
    username : new FormControl("demo"),
    password : new FormControl("demo123"),
  });
  errorMessage = '';

  onSubmit(){
    this.errorMessage = '';
    const username = this.fg.value.username || '';
    const password = this.fg.value.password || '';
    this.appSrvc.login(username, password).subscribe({
      next: (data) => {
        this.globalService.userDetails = data;
        const role = data?.userRoles?.[0]?.roleCd || 'C';
        if (role === 'S') {
          this.router.navigate(['/retailer']);
        } else if (role === 'D') {
          this.router.navigate(['/delivery']);
        } else {
          this.router.navigate(['/consumer']);
        }
      },
      error: () => {
        this.errorMessage = 'Login failed. Try the demo credentials or check your user.';
      }
    });
  }
}
