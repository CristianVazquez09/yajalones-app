package com.wolfpack.controller;

import com.wolfpack.dto.TurnoDTO;
import com.wolfpack.model.Turno;
import com.wolfpack.service.ITurnoService;
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
@RequestMapping("/turnos")
@RequiredArgsConstructor
public class TurnoController {

    //@Autowired
    private final ITurnoService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<TurnoDTO>> buscarTodos() throws Exception{
        List<TurnoDTO> list = service.buscarTodos().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTO> buscarPorId(@PathVariable("id") Integer id) throws Exception {
        Turno obj = service.buscarPorId(id);

        return ResponseEntity.ok(convertToDto(obj));
    }



    @PostMapping
    public ResponseEntity<Void> guardar(@Validated(OnCreate.class) @RequestBody TurnoDTO dto) throws Exception{
        Turno obj = service.guardar(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdTurno()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurnoDTO> actualizar(@Validated(OnUpdate.class) @PathVariable("id") Integer id, @RequestBody TurnoDTO dto) throws Exception{
        dto.setIdTurno(id);
        Turno obj = service.actualizar(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private TurnoDTO convertToDto(Turno obj){
        return modelMapper.map(obj, TurnoDTO.class);
    }

    private Turno convertToEntity(TurnoDTO dto){
        return modelMapper.map(dto, Turno.class);
    }


}
