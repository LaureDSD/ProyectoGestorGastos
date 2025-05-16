import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserserviceService } from '../../services/userservice.service';
import { environment } from '../../environments/environment';
import { Tick } from 'chart.js';
import { TicketService } from '../../services/ticket.service';
import { SpentService } from '../../services/spent.service';
import { SpentDto } from '../../models/api-models/api-models.component';

@Component({
  selector: 'app-edit-form-profile',
  standalone: false,
  templateUrl: './edit-form-profile.component.html',
  styleUrl: './edit-form-profile.component.css'
})
export class EditFormProfileComponent {


  server : string = `${environment.apiUrl}/`;
  profile: string = "";
  @Input() img: string = '';
  @Input() field = '';
  @Input() defaultImg = '/icon/icons8-upload-100.png';
  @Input() icon = '';
  @Input() spent : number | null = null;
  @Output() save = new EventEmitter<{ field: string; value: string }>();

  constructor(
    private userService: UserserviceService,
    private spentService: SpentService) {
  }

  onFileSelected(event: Event): void { 
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    const formData = new FormData();
    formData.append('image', file);

    if(this.spent == null) {
      this.userService.subirFotoPerfil(formData).subscribe({
        next: (res) => {
          this.profile = res.url;
          this.save.emit({ field: this.field, value: this.profile });
        },
        error: () => {
          alert('Error al subir la imagen.');
        }
      });
    }else {
    this.spentService.subirFotoGasto(this.spent, file).subscribe({
      next: (res) => {
        console.log("Subiendo imagen con gasto ID:", this.spent);

        this.profile = res.url;
        this.save.emit({ field: this.field, value: this.profile });
      },
      error: () => {
        alert('Error al subir la imagen.');
      }
    });
    }
  }


  isImageModalOpen = false;

  openImageModal() {
    this.isImageModalOpen = true;
  }

  closeImageModal() {
    this.isImageModalOpen = false;
  }



}

