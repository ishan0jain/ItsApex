import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {
  constructor(private http:HttpClient){}
  fg : FormGroup = new FormGroup({
    username : new FormControl(""),
    password : new FormControl(""),
  })
  onSubmit(){
    console.log(this.fg.value);
    this.http.post("http:localhost:8080/login",{params:this.fg.value});
  }
}
