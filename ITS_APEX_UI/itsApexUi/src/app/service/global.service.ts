import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GlobalService {
  uploadImage(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('imageData',"123");

    return this.http.post<string>('http://localhost:8080/uploadImage', formData, {
        withCredentials: true, // If authentication is required
        responseType: 'text' as 'json'
    });
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
