import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { text } from 'stream/consumers';

@Injectable({
  providedIn: 'root'
})
export class AppServiceService {

  constructor(private http: HttpClient) { }

  routingService(routeId:String){
    const httpOptions = {
      withCredentials: true, // Important to send cookies
      responseType: 'text' as 'json'
    };
    this.http.get<String>('http://localhost:8080/home',httpOptions).subscribe(d=>console.log(d));
  }

  loginService(){
    const httpOptions = {
      withCredentials: true, // Important to send cookies
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      responseType: 'text' as 'json'
    };
    let jsonBody  = {
      "username":"ishan",
      "password":"w341324"
    }
    this.http.post<String>('http://localhost:8080/login',jsonBody,httpOptions).subscribe(
      d=>{
        console.log(d);
        this.routingService("A");
      }
    );
  }
}
