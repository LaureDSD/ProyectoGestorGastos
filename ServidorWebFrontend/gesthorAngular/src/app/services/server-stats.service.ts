import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ServerInfoDto } from '../models/api-models/api-models.component';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ServerStatsService {

  private baseUrl = `${environment.apiUrl}/api/server`;

  constructor(private http: HttpClient) {}

  getServerInfo(): Observable<ServerInfoDto> {
    return this.http.get<ServerInfoDto>(this.baseUrl+`/info`);
  }
}