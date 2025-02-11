import { Component } from '@angular/core';
import { GlobalService } from '../../service/global.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-shop-image-upload',
  standalone: true,
  imports: [NgIf],
  templateUrl: './shop-image-upload.component.html',
  styleUrl: './shop-image-upload.component.css'
})
export class ShopImageUploadComponent {

  constructor(private glblService:GlobalService){}

  selectedFile!: File;
  previewUrl: string | ArrayBuffer | null = null;
  uploadedImageUrl: string | null = null;

  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];

      // Show Preview
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  uploadImage() {
    if (!this.selectedFile) {
      alert("Please select an image first!");
      return;
    }

    this.glblService.uploadImage(this.selectedFile).subscribe(data=>{
      console.log(data);
    });

    // Simulate successful upload (Replace with actual API call)
    setTimeout(() => {
      this.uploadedImageUrl = this.previewUrl as string; // Normally, use response URL
      alert("Image uploaded successfully!");
    }, 1000);
  }
}