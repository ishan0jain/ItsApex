import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { GlobalService } from '../service/global.service';
import { AppServiceService } from '../app-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {
  constructor(private http:HttpClient, private globalService:GlobalService, private appSrvc :AppServiceService, private router:Router){}
  fg : FormGroup = new FormGroup({
    username : new FormControl(""),
    password : new FormControl(""),
  })
  onSubmit(){
    this.appSrvc.loginService().subscribe(data=>{
      this.globalService.userDetails = {
        'user':'ishan'
      }
      data = JSON.parse(data);
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
    })
    
    console.log(this.fg.value);
    this.http.post("http:localhost:8080/login",{params:this.fg.value});
  }
}
