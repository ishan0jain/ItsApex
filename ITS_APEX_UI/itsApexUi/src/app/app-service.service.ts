import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppServiceService {
  private apiBase = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    const httpOptions = {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    return this.http.post(`${this.apiBase}/login`, { username, password }, httpOptions);
  }

  logout(): Observable<any> {
    const httpOptions = {
      withCredentials: true,
    };
    return this.http.post(`${this.apiBase}/logout`, {}, httpOptions);
  }

  getCurrentUser(): Observable<any> {
    const httpOptions = {
      withCredentials: true,
    };
    return this.http.get(`${this.apiBase}/me`, httpOptions);
  }

  getRegistrationFields(merchantType: string | null): Observable<any> {
    let params = new HttpParams();
    if (merchantType) {
      params = params.set('merchantType', merchantType);
    }
    return this.http.get(`${this.apiBase}/registration/fields`, { params, withCredentials: true });
  }

  saveRegulationAnswers(payload: any): Observable<any> {
    const httpOptions = {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    return this.http.post(`${this.apiBase}/registration/answers`, payload, httpOptions);
  }

  registerShop(payload: any): Observable<any> {
    const httpOptions = {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    return this.http.post(`${this.apiBase}/shops`, payload, httpOptions);
  }

  uploadShopImage(shopId: number, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.apiBase}/shops/${shopId}/images`, formData, {
      withCredentials: true,
    });
  }

  getShops(): Observable<any> {
    return this.http.get(`${this.apiBase}/shops`, { withCredentials: true });
  }

  getMyShops(): Observable<any> {
    return this.http.get(`${this.apiBase}/shops/mine`, { withCredentials: true });
  }

  getShop(shopId: number): Observable<any> {
    return this.http.get(`${this.apiBase}/shops/${shopId}`, { withCredentials: true });
  }

  getProducts(shopId?: number): Observable<any> {
    let params = new HttpParams();
    if (shopId) {
      params = params.set('shopId', shopId.toString());
    }
    return this.http.get(`${this.apiBase}/products`, { params, withCredentials: true });
  }

  getPublicProducts(): Observable<any> {
    return this.http.get(`${this.apiBase}/products/public`);
  }

  createProduct(payload: any): Observable<any> {
    const httpOptions = {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    return this.http.post(`${this.apiBase}/products`, payload, httpOptions);
  }

  updateInventory(productId: number, quantityAvailable: number): Observable<any> {
    const params = new HttpParams().set('quantityAvailable', quantityAvailable.toString());
    return this.http.patch(`${this.apiBase}/products/${productId}/inventory`, null, { params, withCredentials: true });
  }

  updateProduct(productId: number, payload: any): Observable<any> {
    const httpOptions = {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    return this.http.patch(`${this.apiBase}/products/${productId}`, payload, httpOptions);
  }

  uploadProductImage(productId: number, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.apiBase}/products/${productId}/images`, formData, {
      withCredentials: true,
    });
  }

  createOrder(payload: any): Observable<any> {
    const httpOptions = {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    return this.http.post(`${this.apiBase}/orders`, payload, httpOptions);
  }

  getOrders(shopId?: number): Observable<any> {
    let params = new HttpParams();
    if (shopId) {
      params = params.set('shopId', shopId.toString());
    }
    return this.http.get(`${this.apiBase}/orders`, { params, withCredentials: true });
  }

  registerDeliveryAgent(payload: any): Observable<any> {
    const httpOptions = {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    return this.http.post(`${this.apiBase}/delivery/agents`, payload, httpOptions);
  }

  getDeliveryTasks(agentId?: number): Observable<any> {
    let params = new HttpParams();
    if (agentId) {
      params = params.set('agentId', agentId.toString());
    }
    return this.http.get(`${this.apiBase}/delivery/tasks`, { params, withCredentials: true });
  }

  assignDeliveryTask(orderId: number, agentId?: number): Observable<any> {
    const httpOptions = {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    return this.http.post(`${this.apiBase}/delivery/tasks/assign`, { orderId, agentId }, httpOptions);
  }

  estimateDelivery(distanceKm: number, estimatedMinutes?: number): Observable<any> {
    const httpOptions = {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    return this.http.post(`${this.apiBase}/delivery/estimate`, { distanceKm, estimatedMinutes }, httpOptions);
  }
}
