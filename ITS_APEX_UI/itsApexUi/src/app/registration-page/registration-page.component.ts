import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { LoginPageComponent } from '../login-page/login-page.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registration-page',
  standalone: true,
  imports: [ReactiveFormsModule, LoginPageComponent, CommonModule],
  templateUrl: './registration-page.component.html',
  styleUrl: './registration-page.component.css'
})
export class RegistrationPageComponent {
  showRegistrationDialog = false;

  registrationForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    confirmPassword: new FormControl('', [Validators.required])
  });

  openRegistrationDialog() {
    this.showRegistrationDialog = true;
  }

  closeRegistrationDialog() {
    this.showRegistrationDialog = false;
  }

  onRegister() {
    if (this.registrationForm.valid) {
      // Here you would send the registrationForm.value to your backend
      console.log('Registration data:', this.registrationForm.value);
      // Optionally close dialog or show success message
      this.closeRegistrationDialog();
    } else {
      this.registrationForm.markAllAsTouched();
    }
  }

  get name() { return this.registrationForm.get('name'); }
  get email() { return this.registrationForm.get('email'); }
  get password() { return this.registrationForm.get('password'); }
  get confirmPassword() { return this.registrationForm.get('confirmPassword'); }
}
