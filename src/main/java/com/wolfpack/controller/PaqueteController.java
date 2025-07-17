package com.wolfpack.controller;

import com.wolfpack.dto.PaqueteDTO;
import com.wolfpack.dto.PaqueteDTO;
import com.wolfpack.model.Paquete;
import com.wolfpack.model.Paquete;
import com.wolfpack.service.IPaqueteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PaqueteDTO>> buscarTodos() throws Exception{
        List<PaqueteDTO> list = service.buscarTodos().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaqueteDTO> buscarPorId(@PathVariable("id") Integer id) throws Exception {
        Paquete obj = service.buscarPorId(id);

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaqueteDTO> actualizar(@Valid @PathVariable("id") Integer id, @RequestBody PaqueteDTO dto) throws Exception{
        dto.setIdPaquete(id);
        Paquete obj = service.actualizar(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PostMapping
    public ResponseEntity<Void> guardar(@Valid @RequestBody PaqueteDTO dto) throws Exception{
        Paquete obj = service.guardarPaquete(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPaquete()).toUri();

        return ResponseEntity.created(location).build();
    }

    private PaqueteDTO convertToDto(Paquete obj){
        return modelMapper.map(obj, PaqueteDTO.class);
    }

    private Paquete convertToEntity(PaqueteDTO dto){
        return modelMapper.map(dto, Paquete.class);
    }


}
