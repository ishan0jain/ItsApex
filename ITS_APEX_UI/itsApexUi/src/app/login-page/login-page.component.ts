import { HttpClient } from '@angular/common/http';
import { Component, OnInit, NgZone, Inject, PLATFORM_ID, Output, EventEmitter } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { GlobalService } from '../service/global.service';
import { AppServiceService } from '../app-service.service';
import { Router } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';

declare global {
  interface Window {
    google?: any;
  }
}

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent implements OnInit {
  @Output() registerClick = new EventEmitter<void>();
  constructor(
    private http:HttpClient,
    private globalService:GlobalService,
    private appSrvc :AppServiceService,
    private router:Router,
    private ngZone: NgZone,
    @Inject(PLATFORM_ID) private platformId: Object
  ){}
  fg : FormGroup = new FormGroup({
    username : new FormControl(""),
    password : new FormControl(""),
  })

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const interval = setInterval(() => {
        if (window.google && window.google.accounts && window.google.accounts.id) {
          clearInterval(interval);
          window.google.accounts.id.initialize({
            client_id: 'YOUR_GOOGLE_CLIENT_ID.apps.googleusercontent.com', // <-- Replace with your client ID
            callback: (response: any) => this.handleCredentialResponse(response)
          });
          window.google.accounts.id.renderButton(
            document.getElementById('google-signin-btn'),
            { theme: 'outline', size: 'large', width: 250 }
          );
        }
      }, 100);
    }
  }

  handleCredentialResponse(response: any) {
    const idToken = response.credential;
    this.http.post('/api/auth/google', { idToken }).subscribe(
      (res: any) => {
        this.ngZone.run(() => {
          alert('Google login successful!');
          // Example: localStorage.setItem('token', res.token);
          // Redirect as needed
        });
      },
      (err) => {
        alert('Google login failed!');
      }
    );
  }
  onSubmit(){
    this.appSrvc.loginService().subscribe((data: any)=>{
      this.globalService.userDetails = {
        'user':'ishan'
      }
      data = JSON.parse(data);
      if(data!=null){
        this.globalService.userDetails = data;
        const userRoles: any[] = data.userRoles;
        const userRole: any[] = data.userRole;
        if(userRoles && userRoles.length == 0 || (userRoles && userRoles[0] && userRoles[0].roleCd == 'C'))
          this.router.navigate(['/consumer']);
        else if(userRoles && userRoles[0] && userRoles[0].roleCd == 'S')
          this.router.navigate(['/seller']);
        else if(userRole && userRole[0] && userRole[0].roleCd == 'D')
          this.router.navigate(['/deleivery']);
        else
          this.router.navigate(['/consumer']);
      }
    })
    
    console.log(this.fg.value);
    this.http.post("http:localhost:8080/login",{params:this.fg.value});
  }
}
