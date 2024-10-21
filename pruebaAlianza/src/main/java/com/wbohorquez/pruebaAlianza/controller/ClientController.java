package com.wbohorquez.pruebaAlianza.controller;

import com.wbohorquez.pruebaAlianza.model.Client;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author WI-LL
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private List<Client> clients = new ArrayList<>();
    // Logger para la clase
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    // Crear nuevo cliente
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        logger.info("Recibiendo solicitud para crear cliente con sharedKey: {}", client.getSharedKey());

        boolean exists = clients.stream()
                .anyMatch(existingClient -> existingClient.getSharedKey().equals(client.getSharedKey()));
        if (exists) {
            logger.error("Error al crear cliente con sharedKey: {}. El cliente ya existe.", client.getSharedKey());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        clients.add(client);
        logger.info("Cliente creado exitosamente con sharedKey: {}", client.getSharedKey());
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    // Listar todos los clientes
    @GetMapping
    public List<Client> getAllClients() {
        logger.info("Solicitud para listar todos los clientes. Total clientes: {}", clients.size());
        return clients;
    }

    // Buscar cliente por Shared Key
    @GetMapping("/search")
    public Client getClientBySharedKey(@RequestParam String sharedKey) {
        logger.info("Solicitud para buscar cliente con sharedKey: {}", sharedKey);
        Client foundClient = clients.stream()
                .filter(client -> client.getSharedKey().equals(sharedKey))
                .findFirst()
                .orElse(null);

        if (foundClient != null) {
            logger.info("Cliente encontrado con sharedKey: {}", sharedKey);
        } else {
            logger.warn("Cliente con sharedKey: {} no encontrado.", sharedKey);
        }

        return foundClient;
    }

    // Buscar cliente por filtros avanzados
    @GetMapping("/searchByFilters")
    public List<Client> searchClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        logger.info("Solicitud para buscar clientes con filtros - Nombre: {}, Teléfono: {}, Correo: {}, Fecha inicio: {}, Fecha fin: {}",
                name, phone, email, startDate, endDate);

        // Filtrar por cada criterio si se proporciona
        List<Client> filteredClients = clients.stream()
                .filter(client -> name == null || client.getName().equalsIgnoreCase(name))
                .filter(client -> phone == null || client.getPhone().equals(phone))
                .filter(client -> email == null || client.getEmail().equalsIgnoreCase(email))
                .filter(client -> (startDate == null && endDate == null)
                || (client.getDateAdded() != null && !client.getDateAdded().before(startDate) && !client.getDateAdded().after(endDate)))
                .collect(Collectors.toList());

        logger.info("Número de clientes encontrados: {}", filteredClients.size());
        return filteredClients;
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllClients() {
        logger.info("Solicitud para eliminar todos los clientes. Total clientes antes de eliminar: {}", clients.size());
        clients.clear();
        logger.info("Todos los clientes han sido eliminados.");
        return ResponseEntity.ok("All clients have been deleted.");
    }

}
