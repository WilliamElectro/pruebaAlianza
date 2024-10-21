import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../models/client.model';


@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private baseUrl: string = 'http://localhost:8080/api/clients';

  constructor(private http: HttpClient) { }

  // Crear un nuevo cliente
  createClient(client: Client): Observable<Client> {
    return this.http.post<Client>(this.baseUrl, client, this.getHttpOptions());
  }

  // Obtener todos los clientes
  getAllClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.baseUrl);
  }

  // Buscar cliente por sharedKey
  getClientBySharedKey(sharedKey: string): Observable<Client> {
    return this.http.get<Client>(`${this.baseUrl}/search?sharedKey=${sharedKey}`);
  }

  // Buscar clientes con filtros avanzados
  searchClients(filters: any): Observable<Client[]> {
    let queryParams = `?name=${filters.name || ''}&phone=${filters.phone || ''}&email=${filters.email || ''}` +
      `&startDate=${filters.startDate || ''}&endDate=${filters.endDate || ''}`;

    return this.http.get<Client[]>(`${this.baseUrl}/searchByFilters${queryParams}`);
  }

  // Eliminar todos los clientes
  deleteAllClients(): Observable<string> {
    return this.http.delete<string>(this.baseUrl);
  }

  // Configuración para los encabezados HTTP
  private getHttpOptions() {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return { headers };
  }
}
