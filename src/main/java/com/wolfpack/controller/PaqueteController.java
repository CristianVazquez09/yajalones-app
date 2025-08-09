package com.wolfpack.controller;

import com.wolfpack.dto.*;
import com.wolfpack.model.Paquete;
import com.wolfpack.model.Pasajero;
import com.wolfpack.service.IPaqueteService;
import com.wolfpack.util.OnCreate;
import com.wolfpack.util.OnUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/paquetes")
@RequiredArgsConstructor
public class PaqueteController {

    //@Autowired
    private final IPaqueteService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<PaqueteResponseDTO>> buscarTodos() throws Exception{
        List<PaqueteResponseDTO> list = service.buscarTodos().stream().map(this::convertToDtoResponse).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<PaqueteResponseDTO>> buscarPaquetesPendientes() throws Exception{
        List<PaqueteResponseDTO> list = service.obtenerPaquetesPendientes().stream().map(this::convertToDtoResponse).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaqueteResponseDTO> buscarPorId(@PathVariable("id") Integer id) throws Exception {
        Paquete obj = service.buscarPorId(id);

        return ResponseEntity.ok(convertToDtoResponse(obj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaqueteResponseDTO> actualizar(@PathVariable("id") Integer id,@Validated(OnUpdate.class) @RequestBody PaqueteRequestDTO dto) throws Exception{
        dto.setIdPaquete(id);
        Paquete obj = service.actualizarPaquete(id, convertToEntityRequest(dto));

        return ResponseEntity.ok(convertToDtoResponse(obj));
    }

    @PostMapping
    public ResponseEntity<Void> guardar(@Validated(OnCreate.class) @RequestBody PaqueteRequestDTO dto) throws Exception{
        Paquete obj = service.guardarPaquete(convertToEntityRequest(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPaquete()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/pendiente")
    public ResponseEntity<Void> guardarPaquetePendiente(@Validated(OnCreate.class) @RequestBody PaquetePendienteRequestDTO dto) throws Exception{
        Paquete obj = service.guardarPaquetePendiente(modelMapper.map(dto, Paquete.class));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPaquete()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/confirmar/{idPaquete}/{idViaje}")
    public ResponseEntity<PaqueteResponseDTO> confirmarPaquete( @PathVariable("idPaquete") Integer idPaquete,  @PathVariable("idViaje") Integer idViaje) throws Exception{
        Paquete obj = service.confirmarPaquete(idPaquete, idViaje);

        return ResponseEntity.ok(convertToDtoResponse(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
        service.eliminarPaquete(id);
        return ResponseEntity.noContent().build();
    }

    private PaqueteResponseDTO convertToDtoResponse(Paquete obj){
        return modelMapper.map(obj, PaqueteResponseDTO.class);
    }

    private Paquete convertToEntityRequest(PaqueteRequestDTO dto){
        return modelMapper.map(dto, Paquete.class);
    }


}
