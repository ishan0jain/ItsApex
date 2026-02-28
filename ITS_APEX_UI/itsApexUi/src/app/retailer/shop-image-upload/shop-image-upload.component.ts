import { Component, EventEmitter, Output } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-shop-image-upload',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './shop-image-upload.component.html',
  styleUrl: './shop-image-upload.component.css'
})
export class ShopImageUploadComponent {
  @Output() filesSelected = new EventEmitter<File[]>();

  previewUrls: string[] = [];
  selectedFiles: File[] = [];

  onFileSelected(event: any) {
    const files = Array.from(event.target.files || []) as File[];
    if (files.length === 0) {
      return;
    }
    this.selectedFiles = files;
    this.previewUrls = [];
    files.forEach(file => {
      const reader = new FileReader();
      reader.onload = () => {
        if (reader.result) {
          this.previewUrls.push(reader.result as string);
        }
      };
      reader.readAsDataURL(file);
    });
    this.filesSelected.emit(this.selectedFiles);
  }
}
