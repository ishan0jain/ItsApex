import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GlobalService {
  uploadImage(selectedFile: File) {
    const httpOptions = {
      withCredentials: true, // Important to send cookies
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      responseType: 'text' as 'json'
    };
    let jsonBody  = {
      "imageId":1,
      "image":selectedFile
    }
    return this.http.post<String>('http://localhost:8080/uploadImage',jsonBody,httpOptions);
  }

  constructor(private http: HttpClient) { }
  userDetails: any = null;

  registerShop(value: any) : Observable<any>{
      const httpOptions = {
        withCredentials: true, // Important to send cookies
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
        }),
      }
        return this.http.post('http://localhost:8080/registerShop',value,httpOptions);
    }

  

  

}
