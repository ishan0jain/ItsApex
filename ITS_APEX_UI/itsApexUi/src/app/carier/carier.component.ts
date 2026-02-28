import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { AppServiceService } from '../app-service.service';

@Component({
  selector: 'app-carier',
  standalone: true,
  imports: [ReactiveFormsModule, NgFor, NgIf],
  templateUrl: './carier.component.html',
  styleUrl: './carier.component.css'
})
export class CarierComponent implements OnInit {
  agentForm = new FormGroup({
    name: new FormControl(''),
    phone: new FormControl(''),
    zones: new FormControl(''),
    latitude: new FormControl(''),
    longitude: new FormControl('')
  });

  estimateForm = new FormGroup({
    distanceKm: new FormControl(5),
    estimatedMinutes: new FormControl(15)
  });

  assignForm = new FormGroup({
    orderId: new FormControl('')
  });

  tasks: any[] = [];
  estimate: any = null;
  message = '';

  constructor(private api: AppServiceService) {}

  ngOnInit(): void {
    this.refreshTasks();
  }

  registerAgent() {
    this.api.registerDeliveryAgent(this.agentForm.value).subscribe({
      next: () => {
        this.message = 'Delivery agent registered.';
        this.agentForm.reset();
      },
      error: () => {
        this.message = 'Unable to register agent.';
      }
    });
  }

  refreshTasks() {
    this.api.getDeliveryTasks().subscribe({
      next: (data) => (this.tasks = data || []),
      error: () => (this.tasks = [])
    });
  }

  assignTask(orderId: number) {
    this.api.assignDeliveryTask(orderId).subscribe({
      next: () => this.refreshTasks(),
      error: () => (this.message = 'Unable to assign task.')
    });
  }

  assignFromForm() {
    const orderId = Number(this.assignForm.value.orderId);
    if (!orderId) {
      this.message = 'Enter a valid order ID.';
      return;
    }
    this.assignTask(orderId);
    this.assignForm.reset();
  }

  calculateEstimate() {
    const payload: any = this.estimateForm.value;
    this.api.estimateDelivery(payload.distanceKm, payload.estimatedMinutes).subscribe({
      next: (data) => (this.estimate = data),
      error: () => (this.estimate = null)
    });
  }
}
