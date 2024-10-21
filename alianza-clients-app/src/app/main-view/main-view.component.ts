import { Component } from '@angular/core';
import { ClientService } from '../services/client.service';
import { Client } from '../models/client.model';


const ELEMENT_DATA: Client[] = [
  { sharedKey: 'jgutierrez', businessID: 'Juliana Gutierrez', email: 'jgutierrez@gmail.com', phone: '3219876543', dateAdded: new Date('2019-05-20') },
  { sharedKey: 'mmartinez', businessID: 'Manuel Martinez', email: 'mmartinez@gmail.com', phone: '3219876543', dateAdded: new Date('2019-05-20') },
  // Añadir más datos aquí...
];

@Component({
  selector: 'app-main-view',
  templateUrl: './main-view.component.html',
  styleUrl: './main-view.component.css'
})
export class MainViewComponent {
  // Variable para controlar la vista activa
  activeView: string = 'clients';
  displayedColumns: string[] = ['sharedKey', 'businessID', 'email', 'phone', 'dateAdded', 'actions'];
  dataSource: Client[] = [];

  constructor(private clientService: ClientService) { }

  ngOnInit(): void {
    this.loadClients();
  }

  // Método para cargar los clientes desde el servicio y concatenarlos con los datos estáticos
  loadClients(): void {
    this.clientService.getAllClients().subscribe(
      (data: Client[]) => {
        this.dataSource = [...ELEMENT_DATA, ...data];
      },
      (error) => {
        console.error('Error loading clients', error);
      }
    );
  }

  // Método para cambiar la vista activa
  selectView(view: string) {
    this.activeView = view;
  }
}
