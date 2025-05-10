import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserserviceService } from '../../services/userservice.service';

@Component({
  selector: 'app-edit-form-profile',
  standalone: false,
  templateUrl: './edit-form-profile.component.html',
  styleUrl: './edit-form-profile.component.css'
})
export class EditFormProfileComponent {

  @Input() profile: any = '';
  @Input() field = '';
  @Input() icon = '';
  @Output() save = new EventEmitter<{ field: string; value: string }>();

  constructor(private userService: UserserviceService) {
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    const formData = new FormData();
    formData.append('image', file);

    this.userService.subirFotoPerfil(formData).subscribe({
      next: (res) => {
        this.profile = res.url;
        this.save.emit({ field: this.field, value: this.profile });
      },
      error: () => {
        alert('Error al subir la imagen.');
      }
    });
  }


}

